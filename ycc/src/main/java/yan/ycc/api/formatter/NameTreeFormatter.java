package yan.ycc.api.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.ycc.api.YCTree;

public class NameTreeFormatter implements Formatter<YCTree.TranslationUnit>, YCTree.VoidVisitor {

    StringBuilder builder = new StringBuilder();

    @Override
    public String format(YCTree.TranslationUnit program) {
        builder = new StringBuilder();
        program.accept(this);
        return builder.toString();
    }

    @Override
    public void visit(YCTree.TranslationUnit that) {
        that.decls.forEach(decl -> decl.accept(this));
    }

    @Override
    public void visit(YCTree.VarDecl that) {
        builder.append(String.format("line %d: def var '%s'\n",
                                     that.start.line, that.id.name));
        if (that.init != null) that.init.accept(this);
    }

    @Override
    public void visit(YCTree.FuncDecl that) {
        builder.append(String.format("line %d: def func '%s'\n", that.start.line, that.id.name));
        that.params.forEach(param -> param.accept(this));
        that.body.accept(this);
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

    @Override
    public void visit(YCTree.FunCall that) {
        builder.append(String.format("line %d: ref func '%s' at line %d\n",
                                     that.start.line, that.funcID.symbol.name, that.funcID.symbol.tree.start.line));
        that.args.forEach(arg -> arg.accept(this));
    }

    @Override
    public void visit(YCTree.Id that) {
        builder.append(String.format("line %d: ref var '%s' at line %d\n",
                                     that.start.line, that.symbol.name, that.symbol.tree.start.line));
    }
}
