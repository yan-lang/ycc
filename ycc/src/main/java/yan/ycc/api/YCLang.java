package yan.ycc.api;

import yan.common.CommonFormatterFactory;
import yan.common.CommonTaskFactory;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.compiler.frontend.lex.formatter.XMLTokenFormatter;
import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Formatter;
import yan.foundation.driver.lang.Language;
import yan.foundation.driver.lang.Target;
import yan.ycc.api.formatter.CSTreeFormatter;
import yan.ycc.api.formatter.NameTreeFormatter;
import yan.ycc.api.formatter.ParseTreeFormatter;
import yan.ycc.api.formatter.TypeTreeFormatter;

import java.util.ArrayList;
import java.util.List;

public class YCLang extends Language {
    List<Target<Code, ?>> targets = new ArrayList<>();

    @Override
    public List<Target<Code, ?>> getTargets() { return targets; }

    @Override
    public String version() { return "1.0"; }

    @Override
    public String name() { return "ycc"; }

    @Override
    public String extension() { return "c"; }

    public YCLang(CommonTaskFactory<YCTree.TranslationUnit> t) {
        this(t, new DefaultFormatterFactory());
    }

    public YCLang(CommonTaskFactory<YCTree.TranslationUnit> t, CommonFormatterFactory<YCTree.TranslationUnit> f) {
        t.lex().ifPresent(phase -> targets.add(new Target.Builder<Code, List<Token>>()
                                                       .name("lex")
                                                       .phase(phase)
                                                       .compatibility(Target.Compatibility.BOTH)
                                                       .cformatter(f.clex())
                                                       .iformatter(f.ilex())
                                                       .build()));
        t.parse().ifPresent(phase -> targets.add(new Target.Builder<Code, YCTree.TranslationUnit>()
                                                         .name("parse")
                                                         .phase(phase)
                                                         .compatibility(Target.Compatibility.BOTH)
                                                         .cformatter(f.parse())
                                                         .iformatter(f.parse())
                                                         .build()));
        t.checkControlStructure().ifPresent(phase -> targets.add(new Target.Builder<Code, YCTree.TranslationUnit>()
                                                                         .name("cs")
                                                                         .phase(phase)
                                                                         .compatibility(Target.Compatibility.BOTH)
                                                                         .cformatter(f.cs())
                                                                         .iformatter(f.cs())
                                                                         .build()));
        t.resolveName().ifPresent(phase -> targets.add(new Target.Builder<Code, YCTree.TranslationUnit>()
                                                               .name("name_resolve")
                                                               .phase(phase)
                                                               .compatibility(Target.Compatibility.BOTH)
                                                               .cformatter(f.nameResolve())
                                                               .iformatter(f.nameResolve())
                                                               .build()));
        t.checkType().ifPresent(phase -> targets.add(new Target.Builder<Code, YCTree.TranslationUnit>()
                                                             .name("typecheck")
                                                             .phase(phase)
                                                             .compatibility(Target.Compatibility.BOTH)
                                                             .cformatter(f.typeCheck())
                                                             .iformatter(f.typeCheck())
                                                             .build()));
    }

    public static class DefaultFormatterFactory implements CommonFormatterFactory<YCTree.TranslationUnit> {
        @Override
        public Formatter<List<Token>> clex() {
            return new XMLTokenFormatter();
        }

        @Override
        public Formatter<List<Token>> ilex() {
            return new SimpleTokenFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> parse() {
            return new ParseTreeFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> cs() {
            return new CSTreeFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> nameResolve() {
            return new NameTreeFormatter();
        }

        @Override
        public Formatter<YCTree.TranslationUnit> typeCheck() {
            return new TypeTreeFormatter();
        }
    }
}
