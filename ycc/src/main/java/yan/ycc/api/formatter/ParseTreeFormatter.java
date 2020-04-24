package yan.ycc.api.formatter;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.driver.lang.Formatter;
import yan.foundation.utils.printer.XMLPrinter;
import yan.ycc.api.YCTree;

public class ParseTreeFormatter implements Formatter<YCTree.TranslationUnit>, YCTree.VoidVisitor {
    private XMLPrinter printer = new XMLPrinter();

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
        printer.pushAttribute("from", String.valueOf(node.range().from));
        printer.pushAttribute("to", String.valueOf(node.range().to));
        if (detailPrinter != null) detailPrinter.print();
        printer.closeElement();
    }

    @Override
    public void visit(YCTree.TranslationUnit that) {
        printer.openElement("TranslationUnit");
        printer.pushAttribute("from", String.valueOf(that.range().from));
        printer.pushAttribute("to", String.valueOf(that.range().to));

        that.decls.forEach(decl -> decl.accept(this));

        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
    }

    @Override
    public void visit(YCTree.VarDecl that) {
        print(that, () -> {
            that.name.accept(this);
            that.type.accept(this);
            that.init().ifPresent(init -> init.accept(this));
        });
    }

    @Override
    public void visit(YCTree.FuncDecl that) {
        print(that, () -> {
            that.name.accept(this);
            that.returnType.accept(this);
            that.params.forEach(param -> param.accept(this));
            that.body.accept(this);
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
            that.cond.accept(this);
            that.thenBody.accept(this);
            that.elseBody().ifPresent(b -> b.accept(this));
        });
    }

    @Override
    public void visit(YCTree.WhileStmt that) {
        print(that, () -> {
            that.cond.accept(this);
            that.body.accept(this);
        });
    }

    @Override
    public void visit(YCTree.ReturnStmt that) {
        print(that, () -> {
            that.value().ifPresent(v -> v.accept(this));
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
    public void visit(YCTree.BinaryExpr that) {
        print(that, () -> {
            printer.pushAttribute("op", that.operator.toString());
            that.lhs.accept(this);
            that.rhs.accept(this);
        });
    }

    @Override
    public void visit(YCTree.UnaryExpr that) {
        print(that, () -> {
            printer.pushAttribute("op", that.operator.toString());
            that.arg.accept(this);
        });
    }

    @Override
    public void visit(YCTree.TypeCastExpr that) {
        print(that, () -> {
            that.type.accept(this);
            that.expr.accept(this);
        });
    }

    @Override
    public void visit(YCTree.FunCall that) {
        print(that, () -> {
            that.name.accept(this);
            that.args.forEach(arg -> arg.accept(this));
        });
    }

    @Override
    public void visit(YCTree.Type that) {
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
