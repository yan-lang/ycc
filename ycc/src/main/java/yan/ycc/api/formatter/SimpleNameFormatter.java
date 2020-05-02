package yan.ycc.api.formatter;

import yan.ycc.api.YCTree;

public class SimpleNameFormatter extends AbstractNameFormatter {

    StringBuilder builder = new StringBuilder();

    @Override
    public String format(YCTree.TranslationUnit program) {
        builder = new StringBuilder();
        program.accept(this);
        return builder.toString();
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
