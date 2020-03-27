package yan.ycc.v1.api;

import yan.ycc.v1.api.YCTree.*;

// @formatter:off
public interface YCVisitor<R> {
    default R visitOthers(YCTree that) { return null;}
    default R visit(Program that)      { return visitOthers(that); }
    default R visit(VarDecl that)      { return visitOthers(that); }
    default R visit(FuncDecl that)     { return visitOthers(that); }
    default R visit(Block that)        { return visitOthers(that); }
    default R visit(ExprStmt that)     { return visitOthers(that); }
    default R visit(IfStmt that)       { return visitOthers(that); }
    default R visit(WhileStmt that)    { return visitOthers(that); }
    default R visit(ReturnStmt that)   { return visitOthers(that); }
    default R visit(EmptyStmt that)    { return visitOthers(that); }
    default R visit(ContinueStmt that) { return visitOthers(that); }
    default R visit(BreakStmt that)    { return visitOthers(that); }
    default R visit(BinaryExpr that)   { return visitOthers(that); }
    default R visit(UnaryExpr that)    { return visitOthers(that); }
    default R visit(TypeCastExpr that) { return visitOthers(that); }
    default R visit(FunCall that)      { return visitOthers(that); }
    default R visit(Type that)         { return visitOthers(that); }
    default R visit(Id that)           { return visitOthers(that); }
    default R visit(Literal that)      { return visitOthers(that); }
}
