package yan.ycc.api.formatter;

import yan.foundation.utils.printer.XMLPrinter;
import yan.ycc.api.YCTree;

public class NameFormatter extends AbstractNameFormatter {

    XMLPrinter printer;

    @Override
    public String format(YCTree.TranslationUnit translationUnit) {
        printer = new XMLPrinter("nameResolve");
        translationUnit.accept(this);
        return printer.flush();
    }

    @Override
    public void visit(YCTree.VarDecl that) {
        printer.openElement("def");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("type", "var");
        printer.pushSimpleElement("name", that.id.name);
        printer.closeElement();
        that.init().ifPresent(init -> init.accept(this));
    }

    @Override
    public void visit(YCTree.FuncDecl that) {
        printer.openElement("def");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("type", "func");
        printer.pushSimpleElement("name", that.id.name);
        printer.closeElement();
        that.params.forEach(param -> param.accept(this));
        that.body.accept(this);
    }

    @Override
    public void visit(YCTree.FunCall that) {
        printer.openElement("ref");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("type", "func");
        printer.pushSimpleElement("name", that.funcID.name);
        printer.pushSimpleElement("refLine", that.funcID.symbol.tree == null ? "null" :
                String.valueOf(that.funcID.symbol.tree.start.line));
        printer.closeElement();
        that.args.forEach(arg -> arg.accept(this));
    }

    @Override
    public void visit(YCTree.Id that) {
        printer.openElement("ref");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("type", "var");
        printer.pushSimpleElement("name", that.symbol.name);
        printer.pushSimpleElement("refLine", String.valueOf(that.symbol.tree.start.line));
        printer.closeElement();
    }
}
