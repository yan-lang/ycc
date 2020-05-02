package yan.ycc.api.formatter;

import yan.foundation.utils.printer.XMLPrinter;
import yan.ycc.api.YCTree;

public class CSFormatter extends AbstractCSFormatter {

    XMLPrinter printer;

    @Override
    public String format(YCTree.TranslationUnit translationUnit) {
        printer = new XMLPrinter("controlStructure");
        translationUnit.accept(this);
        return printer.flush();
    }

    @Override
    public void visit(YCTree.ReturnStmt that) {
        printer.openElement("return");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("attachedFunction",
                                  that.func == null ? "null" : String.valueOf(that.func.start.line));
        printer.closeElement();
    }

    @Override
    public void visit(YCTree.ContinueStmt that) {
        printer.openElement("continue");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("attachedLoop",
                                  that.attachedLoop == null ? "null" : String.valueOf(that.attachedLoop.start.line));
        printer.closeElement();
    }

    @Override
    public void visit(YCTree.BreakStmt that) {
        printer.openElement("break");
        printer.pushSimpleElement("line", String.valueOf(that.start.line));
        printer.pushSimpleElement("attachedLoop",
                                  that.attachedLoop == null ? "null" : String.valueOf(that.attachedLoop.start.line));
        printer.closeElement();
    }

}
