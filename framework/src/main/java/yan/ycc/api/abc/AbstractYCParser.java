package yan.ycc.api.abc;

import yan.foundation.compiler.frontend.parse.AbstractParser;
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
}
