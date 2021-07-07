package yan.ycc.api.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.ycc.api.YCTree;

public abstract class AbstractCSFormatter implements Formatter<YCTree.TranslationUnit>, YCTree.VoidVisitor {

    @Override
    abstract public void visit(YCTree.ReturnStmt that);

    @Override
    abstract public void visit(YCTree.ContinueStmt that);

    @Override
    abstract public void visit(YCTree.BreakStmt that);

    @Override
    public void visit(YCTree.TranslationUnit that) {
        that.decls.forEach(decl -> decl.accept(this));
    }

    @Override
    public void visit(YCTree.FuncDecl that) {
        that.body.accept(this);
    }

    @Override
    public void visit(YCTree.ForStmt that) {
        that.body.accept(this);
    }

    @Override
    public void visit(YCTree.WhileStmt that) {
        that.body.accept(this);
    }

    @Override
    public void visit(YCTree.IfStmt that) {
        that.thenBody.accept(this);
        if (that.elseBody != null) that.elseBody.accept(this);
    }

    @Override
    public void visit(YCTree.Block that) {
        that.stmts.forEach(stmt -> stmt.accept(this));
    }

}
