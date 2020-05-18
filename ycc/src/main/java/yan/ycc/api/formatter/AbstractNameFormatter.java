package yan.ycc.api.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.ycc.api.YCTree;

public abstract class AbstractNameFormatter implements Formatter<YCTree.TranslationUnit>, YCTree.VoidVisitor {

    @Override
    abstract public void visit(YCTree.VarDecl that);

    @Override
    abstract public void visit(YCTree.FuncDecl that);

    @Override
    abstract public void visit(YCTree.FunCall that);

    @Override
    abstract public void visit(YCTree.Id that);

    @Override
    public void visit(YCTree.TranslationUnit that) {
        that.decls.forEach(decl -> decl.accept(this));
    }

    @Override
    public void visit(YCTree.ReturnStmt that) {
        if (that.value != null) that.value.accept(this);
    }

    @Override
    public void visit(YCTree.IfStmt that) {
        that.cond.accept(this);
        that.thenBody.accept(this);
        if (that.elseBody != null) that.elseBody.accept(this);
    }

    @Override
    public void visit(YCTree.WhileStmt that) {
        that.cond.accept(this);
        that.body.accept(this);
    }

    @Override
    public void visit(YCTree.ExprStmt that) {
        that.expr.accept(this);
    }

    @Override
    public void visit(YCTree.Block that) {
        that.stmts.forEach(stmt -> stmt.accept(this));
    }

    @Override
    public void visit(YCTree.AssignExpr that) {
        that.assignee.accept(this);
        that.value.accept(this);
    }

    @Override
    public void visit(YCTree.UnaryExpr that) {
        that.arg.accept(this);
    }

    @Override
    public void visit(YCTree.BinaryExpr that) {
        that.lhs.accept(this);
        that.rhs.accept(this);
    }

    @Override
    public void visit(YCTree.TypeCastExpr that) {
        that.expr.accept(this);
    }
}
