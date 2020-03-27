package yan.ycc.v1.api;

import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Vocabulary;

public abstract class AbstractYCLexer extends AbstractLexer implements YCTokens {

    public AbstractYCLexer() {
        this.name = "YCLexer";
        this.vocabulary = new Vocabulary(tokenNames);
    }

}
