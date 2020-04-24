package yan.ycc.api.abc;

import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Vocabulary;
import yan.ycc.api.YCTokens;

public abstract class AbstractYCLexer extends AbstractLexer implements YCTokens {

    public AbstractYCLexer() {
        this.name = "YCLexer";
        this.vocabulary = new Vocabulary(tokenNames);
    }

}
