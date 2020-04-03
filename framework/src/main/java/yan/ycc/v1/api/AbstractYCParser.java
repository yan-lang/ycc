package yan.ycc.v1.api;

import yan.foundation.compiler.frontend.parse.AbstractParser;

public abstract class AbstractYCParser extends AbstractParser<YCTree.Program> {

    public AbstractYCParser() {
        super("parse");
    }

}
