package yan.ycc.api.abc;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.foundation.compiler.frontend.parse.BaseParserDiagnostic;
import yan.foundation.driver.log.Diagnostic;
import yan.ycc.api.YCTokens;
import yan.ycc.api.YCTree;

public abstract class AbstractYCParser extends AbstractParser<YCTree.TranslationUnit> implements YCTokens {

    protected YCTree.Factory F = new YCTree.Factory(tokens);

    public AbstractYCParser() {
        super("YCParser");
    }

    protected void recovery() {
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;
            if (previous().type == LBRACE) return;
            advance();
        }
    }

    @Override
    protected TokenTypeStringMapper getTokenTypeStringMapper() {
        return tokenNames::get;
    }

    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors extends BaseParserDiagnostic.IErrors {

        default Diagnostic ConsecutiveStatements(Tree tree) {
            Diagnostic d = Diagnostic.Error("consecutive statements must be separated by ';'");
            d.line = tree.end.line;
            d.column = tree.end.col + tree.end.stop - tree.end.start;
            d.sourceName = tree.end.source.getSourceName();
            d.context = tree.end.source.get(d.line);
            return d;
        }

        default Diagnostic InvalidAssignmentTarget(Tree tree) {
            Diagnostic d = Diagnostic.Error("invalid assignment target");
            fillRangePosition(tree, d);
            return d;
        }

        default Diagnostic InvalidFunctionName(Tree tree) {
            Diagnostic d = Diagnostic.Error("invalid function name");
            fillRangePosition(tree, d);
            return d;
        }

        private void fillRangePosition(Tree tree, Diagnostic d) {
            d.line = tree.start.line;
            d.column = tree.start.col;
            d.length = tree.end.stop - tree.start.start;
            d.sourceName = tree.start.source.getSourceName();
            d.context = tree.start.source.get(d.line);
        }

    }
}
