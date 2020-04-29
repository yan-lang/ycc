package yan.ycc.api.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.ycc.api.YCTree;

public class CSTreeFormatter implements Formatter<YCTree.TranslationUnit>, YCTree.VoidVisitor {
    StringBuilder builder;
    final String template = "line %d: %s found, %s\n";
    final String detailTemplate = "attached %s at line %d";

    @Override
    public String format(YCTree.TranslationUnit program) {
        builder = new StringBuilder();
        program.accept(this);
        return builder.toString();
    }

    @Override
    public void visit(YCTree.ReturnStmt that) {
        String detail = that.func == null ? "invalid" : String.format(detailTemplate, "func", that.func.start.line);
        builder.append(String.format(template, that.start.line, "return", detail));
    }

    @Override
    public void visit(YCTree.ContinueStmt that) {
        String detail = that.attachedLoop == null ? "invalid" :
                String.format(detailTemplate, "loop", that.attachedLoop.start.line);
        builder.append(String.format(template, that.start.line, "continue", detail));
    }

    @Override
    public void visit(YCTree.BreakStmt that) {
        String detail = that.attachedLoop == null ? "invalid" :
                String.format(detailTemplate, "loop", that.attachedLoop.start.line);
        builder.append(String.format(template, that.start.line, "break", detail));
    }

    @Override
    public void visit(YCTree.TranslationUnit that) {
        that.decls.forEach(decl -> decl.accept(this));
    }

    @Override
    public void visit(YCTree.FuncDecl that) {
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
