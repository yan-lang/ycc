package yan.ycc.api;

import yan.common.CommonFormatterFactory;
import yan.common.CommonTaskFactory;
import yan.foundation.driver.lang.*;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.frontend.lex.formatter.XMLTokenFormatter;
import yan.foundation.ir.Module;
import yan.ycc.api.formatter.*;
import yan.ycc.api.phase.Interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * YCLang是对YC语言编译器的抽象。
 */
public class YCLang extends Language {

    List<CompilerTarget<Code, ?>> compilerTargets = new ArrayList<>();
    List<InterpreterTarget<Code, ?>> interpreterTargets = new ArrayList<>();

    public YCLang(TaskFactory t) {
        this(t, new DefaultFormatterFactory());
    }

    public YCLang(TaskFactory t, FormatterFactory f) {
        this.buildTargets(t, f, false, new BuildTargetAction() {
            public <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter) {
                compilerTargets.add(new CompilerTarget<>(name, phase, formatter));
            }
        });
        this.buildTargets(t, f, true, new BuildTargetAction() {
            public <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter) {
                interpreterTargets.add(new InterpreterTarget<>(name, phase, formatter));
            }
        });
    }


    //===----------------------------------------------------------------------===//
    //                    Getter for language fields
    //===----------------------------------------------------------------------===//


    @Override
    public String version() { return "1.0"; }

    @Override
    public String name() { return "ycc"; }

    @Override
    public String extension() { return "c"; }

    public List<CompilerTarget<Code, ?>> getCompilerTargets() {
        return this.compilerTargets;
    }

    public List<InterpreterTarget<Code, ?>> getInterpreterTargets() {
        return this.interpreterTargets;
    }


    //===----------------------------------------------------------------------===//
    //       Factory interface for the implementation of this language
    //===----------------------------------------------------------------------===//


    public interface TaskFactory {
        default Optional<Phase<Code, List<Token>>> lex(boolean isInterpreting) { return Optional.empty(); }

        default Optional<Phase<Code, YCTree.TranslationUnit>> parse(boolean isInterpreting) { return Optional.empty(); }

        default Optional<Phase<Code, YCTree.TranslationUnit>> checkControlStructure(boolean isInterpreting) { return Optional.empty(); }

        default Optional<Phase<Code, YCTree.TranslationUnit>> resolveName(boolean isInterpreting) { return Optional.empty(); }

        default Optional<Phase<Code, YCTree.TranslationUnit>> checkType(boolean isInterpreting) { return Optional.empty(); }

        default Optional<Phase<Code, Module>> emitIR(boolean isInterpreting) { return Optional.empty(); }

        default Optional<Phase<Code, Object>> interpret(boolean isInterpreting) {
            var phase = emitIR(isInterpreting);
            return phase.map(codeModulePhase -> codeModulePhase.pipe(new Interpreter()));
        }
    }

    public interface FormatterFactory extends CommonFormatterFactory<YCTree.TranslationUnit> {

        Formatter<Module> emitIR(boolean isInterpreting);

        Formatter<Object> interpret(boolean isInterpreting);

    }


    //===----------------------------------------------------------------------===//
    //                    Helper for constructors
    //===----------------------------------------------------------------------===//


    protected void buildTargets(TaskFactory t, FormatterFactory f, boolean isInterpreting, BuildTargetAction action) {
        t.lex(isInterpreting).ifPresent((phase) -> action.exec("lex", phase, f.lex(isInterpreting)));
        t.parse(isInterpreting).ifPresent((phase) -> action.exec("parse", phase, f.parse(isInterpreting)));
        t.checkControlStructure(isInterpreting).ifPresent((phase) -> action.exec("cs", phase, f.cs(isInterpreting)));
        t.resolveName(isInterpreting).ifPresent((phase) -> action.exec("name", phase, f.nameResolve(isInterpreting)));
        t.checkType(isInterpreting).ifPresent((phase) -> action.exec("type", phase, f.typeCheck(isInterpreting)));
        t.emitIR(isInterpreting).ifPresent((phase) -> action.exec("ir", phase, f.emitIR(isInterpreting)));
        t.interpret(isInterpreting).ifPresent((phase) -> action.exec("interpret", phase, f.interpret(isInterpreting)));
    }

    protected interface BuildTargetAction {
        <out> void exec(String name, Phase<Code, out> phase, Formatter<out> formatter);
    }

    public static class DefaultFormatterFactory implements FormatterFactory {
        @Override
        public Formatter<List<Token>> lex(boolean isInterpreting) {
            return isInterpreting ? new SimpleTokenFormatter() : new XMLTokenFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> parse(boolean isInterpreting) {
            return new ParseTreeFormatter(!isInterpreting);
        }

        @Override
        public Formatter<YCTree.TranslationUnit> cs(boolean isInterpreting) {
            return isInterpreting ? new SimpleCSFormatter() : new CSFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> nameResolve(boolean isInterpreting) {
            return isInterpreting ? new SimpleNameFormatter() : new NameFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> typeCheck(boolean isInterpreting) {
            return new TypeTreeFormatter(!isInterpreting);
        }

        @Override
        public Formatter<Module> emitIR(boolean isInterpreting) {
            return Module::dump;
        }

        @Override
        public Formatter<Object> interpret(boolean isInterpreting) {
            return Object::toString;
        }
    }
}
