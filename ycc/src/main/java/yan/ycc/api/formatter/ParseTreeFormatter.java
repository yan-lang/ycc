package yan.ycc.api.formatter;

import yan.foundation.frontend.ast.Tree;
import yan.foundation.driver.lang.Formatter;
import yan.foundation.utils.printer.XMLPrinter;
import yan.ycc.api.YCTree;

public class ParseTreeFormatter implements Formatter<YCTree.TranslationUnit>, YCTree.VoidVisitor {
    private boolean verbose;
    private XMLPrinter printer = new XMLPrinter();

    public ParseTreeFormatter(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public String format(YCTree.TranslationUnit translationUnit) {
        translationUnit.accept(this);
        return flush();
    }

    public String flush() {
        String result = printer.flush();
        printer = new XMLPrinter();
        return result;
    }

    private interface DetailPrinter {
        void print();
    }

    private <T extends Tree> void print(T node, DetailPrinter detailPrinter) {
        print(node, false, detailPrinter);
    }

    private <T extends Tree> void print(T node, boolean compactMode, DetailPrinter detailPrinter) {
        printer.openElement(node.getClass().getSimpleName(), compactMode);
//        printer.pushAttribute("from", String.valueOf(node.range().from));
//        printer.pushAttribute("to", String.valueOf(node.range().to));
        if (detailPrinter != null) detailPrinter.print();
        printer.closeElement();
    }

    private void printField(String name, DetailPrinter detailPrinter) {
        if (verbose) printer.openElement(name);
        detailPrinter.print();
        if (verbose) printer.closeElement();
    }

    @Override
    public void visit(YCTree.TranslationUnit that) {
        printer.openElement("TranslationUnit");
//        printer.pushAttribute("from", String.valueOf(that.range().from));
//        printer.pushAttribute("to", String.valueOf(that.range().to));

        that.decls.forEach(decl -> decl.accept(this));

        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
    }

    @Override
    public void visit(YCTree.VarDecl that) {
        print(that, () -> {
            printField("name", () -> that.id.accept(this));
            printField("type", () -> that.type.accept(this));
            that.init().ifPresent(init -> printField("init", () -> init.accept(this)));
        });
    }

    @Override
    public void visit(YCTree.FuncDecl that) {
        print(that, () -> {
            printField("name", () -> that.id.accept(this));
            printField("returnType", () -> that.returnType.accept(this));
            printField("params", () -> that.params.forEach(param -> printField("param", () -> param.accept(this))));
            printField("body", () -> that.body.accept(this));
        });
    }

    @Override
    public void visit(YCTree.Block that) {
        print(that, () -> {
            that.stmts.forEach(stmt -> stmt.accept(this));
        });
    }

    @Override
    public void visit(YCTree.ExprStmt that) {
        print(that, () -> {
            that.expr.accept(this);
        });
    }

    @Override
    public void visit(YCTree.IfStmt that) {
        print(that, () -> {
            printField("cond", () -> that.cond.accept(this));
            printField("thenBody", () -> that.thenBody.accept(this));
            that.elseBody().ifPresent(b -> printField("elseBody", () -> b.accept(this)));
        });
    }

    @Override
    public void visit(YCTree.ForStmt that) {
        print(that, () -> {
            printField("decl", () -> that.decl.accept(this));
            printField("cond", () -> that.cond.accept(this));
            printField("action", () -> that.action.accept(this));
            printField("body", () -> that.body.accept(this));
        });
    }

    @Override
    public void visit(YCTree.WhileStmt that) {
        print(that, () -> {
            printField("cond", () -> that.cond.accept(this));
            printField("body", () -> that.body.accept(this));
        });
    }

    @Override
    public void visit(YCTree.ReturnStmt that) {
        print(that, () -> {
            that.value().ifPresent(v -> printField("value", () -> v.accept(this)));
        });
    }

    @Override
    public void visit(YCTree.EmptyStmt that) {

    }

    @Override
    public void visit(YCTree.ContinueStmt that) {
        print(that, () -> {});
    }

    @Override
    public void visit(YCTree.BreakStmt that) {
        print(that, () -> {});
    }

    @Override
    public void visit(YCTree.AssignExpr that) {
        print(that, () -> {
            printField("assignee", () -> that.assignee.accept(this));
            printField("value", () -> that.value.accept(this));
        });
    }

    @Override
    public void visit(YCTree.BinaryExpr that) {
        print(that, () -> {
            printer.pushAttribute("op", that.operator.toString());
            printField("lhs", () -> that.lhs.accept(this));
            printField("rhs", () -> that.rhs.accept(this));
        });
    }

    @Override
    public void visit(YCTree.UnaryExpr that) {
        print(that, () -> {
            printer.pushAttribute("op", that.operator.toString());
            printField("arg", () -> that.arg.accept(this));
        });
    }

    @Override
    public void visit(YCTree.TypeCastExpr that) {
        print(that, () -> {
            printField("type", () -> that.type.accept(this));
            printField("expr", () -> that.expr.accept(this));
        });
    }

    @Override
    public void visit(YCTree.FunCall that) {
        print(that, () -> {
            printField("funcName", () -> that.funcID.accept(this));
            printField("args", () -> that.args.forEach(arg -> printField("arg", () -> arg.accept(this))));
        });
    }

    @Override
    public void visit(YCTree.PrimitiveType that) {
        print(that, true, () -> {
            printer.pushText(that.type.toString());
        });
    }

    @Override
    public void visit(YCTree.Id that) {
        print(that, true, () -> {
            printer.pushText(that.name);
        });
    }

    @Override
    public void visit(YCTree.Literal that) {
        print(that, true, () -> {
            printer.pushAttribute("type", that.type.toString());
            printer.pushText(that.value.toString());
        });
    }
}
