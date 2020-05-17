package yan.ycc.api;

import yan.common.CommonFormatterFactory;
import yan.common.CommonLang;
import yan.common.CommonTaskFactory;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.frontend.lex.formatter.XMLTokenFormatter;
import yan.foundation.driver.lang.Formatter;
import yan.ycc.api.formatter.*;

import java.util.List;

public class YCLang extends CommonLang<YCTree.TranslationUnit> {

    @Override
    public String version() { return "1.0"; }

    @Override
    public String name() { return "ycc"; }

    @Override
    public String extension() { return "c"; }

    public YCLang(TaskFactory t) { super(t, new DefaultFormatterFactory()); }

    public YCLang(TaskFactory t, FormatterFactory f) { super(t, f); }

    public interface TaskFactory extends CommonTaskFactory<YCTree.TranslationUnit> {}

    public interface FormatterFactory extends CommonFormatterFactory<YCTree.TranslationUnit> {}

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
    }
}
