package yan.ycc.api.abc;

import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.ycc.api.YCTokens;
import yan.ycc.api.YCTree;

public abstract class AbstractYCParser extends AbstractParser<YCTree.TranslationUnit> implements YCTokens {

    public AbstractYCParser() {
        super("parse");
    }

    protected void recovery() {
        while (!isAtEnd()) {
            if (previous().type == COLON) return;
            if (previous().type == LBRACE) return;
            advance();
        }
    }

    @Override
    protected TokenTypeStringMapper getTokenTypeStringMapper() {
        return tokenNames::get;
    }
}
