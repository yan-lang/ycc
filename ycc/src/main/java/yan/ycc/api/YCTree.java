package yan.ycc.api;

import yan.foundation.frontend.ast.Tree;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.semantic.v1.Scope;
import yan.foundation.frontend.semantic.v1.Symbol;
import yan.foundation.frontend.semantic.v1.Type;
import yan.foundation.frontend.semantic.v1.symbol.MethodSymbol;
import yan.foundation.frontend.semantic.v1.symbol.VarSymbol;

import java.util.List;
import java.util.Optional;

public abstract class YCTree extends Tree {

    public abstract <R> R accept(Visitor<R> visitor);

    public abstract void accept(VoidVisitor visitor);

    /**
     * 翻译单元(一个C源文件将编译成一个{@code TranslationUnit})
     * <pre>
     *     (VarDecl | FuncDecl) *
     * </pre>
     */
    public static class TranslationUnit extends YCTree {
        public List<YCTree> decls;

        /**
         * 全局符号表
         */
        public Scope scope;

        public TranslationUnit(List<YCTree> decls) {
            this.decls = decls;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    /**
     * 变量定义
     * <pre>
     *     type identifier [= expression]
     * </pre>
     */
    public static class VarDecl extends Stmt {
        public TypeTree type;
        public Id id;
        public Expr init;

        /**
         * 类型检查时可能需要用到（确认初始化表达式类型与定义类型是否兼容）
         */
        public VarSymbol symbol;

        public VarDecl(TypeTree type, Id id) {
            this(type, id, null);
        }

        public VarDecl(TypeTree type, Id id, Expr init) {
            this.type = type;
            this.id = id;
            this.init = init;
        }

        public Optional<Expr> init() {
            if (init == null) return Optional.empty();
            else return Optional.of(init);
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    /**
     * 函数定义
     * <p>YC1函数定义和实现必须一起。</p>
     * <pre>
     *     type identifier(parameters) {
     *         body
     *     }
     * </pre>
     */
    public static class FuncDecl extends YCTree {
        public TypeTree returnType;
        public Id id;
        public List<VarDecl> params;
        public Block body;

        /**
         * 检查return语句的返回值是否和函数定义兼容可能会用到
         */
        public MethodSymbol symbol;

        public FuncDecl(TypeTree returnType, Id id, List<VarDecl> params, Block body) {
            this.returnType = returnType;
            this.id = id;
            this.params = params;
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    public static abstract class Stmt extends YCTree {}

    /**
     * 代码块
     * <pre>
     *     {
     *         body
     *     }
     * </pre>
     */
    public static class Block extends Stmt {
        public List<Stmt> stmts;

        public Block(List<Stmt> stmts) {
            this.stmts = stmts;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 表达式语句
     * <p>一个表达式接一个分号。通常会产生副作用，如变量赋值{@code a=10}。</p>
     * <pre>
     *     expr;
     * </pre>
     */
    public static class ExprStmt extends Stmt {
        public Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * If语句
     * <p>可能有{@code else}, 不支持{@code else if}。</p>
     * <pre>
     *     if(expression) {
     *         body
     *     } else {
     *         body
     *     }
     * </pre>
     */
    public static class IfStmt extends Stmt {
        public Expr cond;
        public Block thenBody;
        public Block elseBody;

        public IfStmt(Expr cond, Block thenBody, Block elseBody) {
            this.cond = cond;
            this.thenBody = thenBody;
            this.elseBody = elseBody;
        }

        public Optional<Block> elseBody() {
            if (elseBody == null) return Optional.empty();
            else return Optional.of(elseBody);
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    public static abstract class LoopStmt extends Stmt {

    }

    /**
     * While循环
     * <pre>
     *     while(expression) {
     *         body
     *     }
     * </pre>
     */
    public static class WhileStmt extends LoopStmt {
        public Expr cond;
        public Block body;

        public WhileStmt(Expr cond, Block body) {
            this.cond = cond;
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * Return语句
     * <pre>
     *     'return' expression? ';'
     * </pre>
     */
    public static class ReturnStmt extends Stmt {
        public Expr value;

        /**
         * 这个Return语句相关联的函数, 应当在语义分析的时候求出来
         */
        public FuncDecl func;

        public ReturnStmt(Expr value) {
            this.value = value;
        }

        public Optional<Expr> value() {
            if (value == null) return Optional.empty();
            else return Optional.of(value);
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 空语句
     * <pre>
     *     ;
     * </pre>
     */
    public static class EmptyStmt extends Stmt {
        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * Continue语句
     * <pre>
     *     continue;
     * </pre>
     */
    public static class ContinueStmt extends Stmt {
        /**
         * 这个Break语句相关联的循环语句, 应当在语义分析的时候求出来
         */
        public LoopStmt attachedLoop;

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * Break语句
     * <pre>
     *     break;
     * </pre>
     */
    public static class BreakStmt extends Stmt {
        /**
         * 这个Break语句相关联的循环语句, 应当在语义分析的时候求出来
         */
        public LoopStmt attachedLoop;

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    public static abstract class Expr extends YCTree {
        /**
         * 每个表达式都有一个类型，这个类型需要在类型检查的时候算出来
         */
        public Type evalType;
    }

    public static class Operator {
        public enum Tag {
            MULTI, DIV, ADD, MINUS,
            GT, GTE, LT, LTE,
            EQ, NEQ,
            ASSIGN,
            REL_NOT, REL_AND, REL_OR;
        }

        public Tag tag;
        public Token token;

        public Operator(Tag tag, Token token) {
            this.tag = tag;
            this.token = token;
        }

        public static Operator of(Tag tag) { return new Operator(tag, null); }

        public static Operator of(Tag tag, Token token) { return new Operator(tag, token); }

        public static Operator from(Token token) { return new Operator(getTagFromToken(token), token); }

        // @formatter:off
        public static Tag getTagFromToken(Token token) {
            switch (token.type) {
                case YCTokens.MULTI: return Tag.MULTI;
                case YCTokens.DIV: return Tag.DIV;
                case YCTokens.ADD: return Tag.ADD;
                case YCTokens.MINUS: return Tag.MINUS;
                case YCTokens.GT: return Tag.GT;
                case YCTokens.GTE: return Tag.GTE;
                case YCTokens.LT: return Tag.LT;
                case YCTokens.LTE: return Tag.LTE;
                case YCTokens.EQ: return Tag.EQ;
                case YCTokens.NEQ: return Tag.NEQ;
                case YCTokens.ASSIGN: return Tag.ASSIGN;
                case YCTokens.REL_NOT: return Tag.REL_NOT;
                case YCTokens.REL_AND: return Tag.REL_AND;
                case YCTokens.REL_OR: return Tag.REL_OR;
                default:
                    throw new IllegalStateException("Unexpected value: " + token.type);
            }
        }
        // @formatter:on

        @Override
        public String toString() {
            return tag.toString();
        }
    }

    /**
     * 运算符表达式
     * <p>{@code OperatorExpr}是任何含有运算符的表达式的基类。</p>
     */
    public static abstract class OperatorExpr extends Expr {
        public Operator operator;

        public OperatorExpr(Operator operator) {
            this.operator = operator;
        }
    }

    /**
     * 双目运算表达式
     */
    public static class BinaryExpr extends OperatorExpr {
        public Expr lhs;
        public Expr rhs;

        public BinaryExpr(Operator operator, Expr lhs, Expr rhs) {
            super(operator);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 单目运算表达式
     */
    public static class UnaryExpr extends OperatorExpr {
        public Expr arg;

        public UnaryExpr(Operator operator, Expr arg) {
            super(operator);
            this.arg = arg;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    /**
     * 类型转换表达式
     * <pre>
     *     (type)expression
     *     (float)11 + 11.5
     * </pre>
     */
    public static class TypeCastExpr extends Expr {
        public TypeTree type;
        public Expr expr;

        public TypeCastExpr(TypeTree type, Expr expr) {
            this.type = type;
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 函数调用表达式
     * <pre>
     *     name(args)
     *     f(a,b,c)
     * </pre>
     */
    public static class FunCall extends Expr {
        public Id funcID;
        public List<Expr> args;

        public FunCall(Id funcID, List<Expr> args) {
            this.funcID = funcID;
            this.args = args;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 所有数据类型语法树的基类
     * <p>常见的数据类型包括: (1) 原始类型， (2) 数组类型，(3) 聚合类型(结构体, 类)，(4) 函数指针等
     * </p>
     */
    public abstract static class TypeTree extends YCTree {}

    /**
     * 基础数据类型
     * <pre>
     *     int, float, void
     * </pre>
     */
    public static class PrimitiveType extends TypeTree {
        public enum Tag { INT, FLOAT, VOID,}

        public Tag type;

        public PrimitiveType(Tag type) {
            this.type = type;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 标识符(Id -> Identifier)
     */
    public static class Id extends Expr {
        public String name;

        /**
         * 该标识符对应的符号, 需要在名字解析(name resolve)阶段算出
         */
        public Symbol symbol;

        public Id(String name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 字面量
     * <pre>
     *     1.34, true / false, 12394
     * </pre>
     */
    public static class Literal extends Expr {
        public PrimitiveType.Tag type;
        public Object value;

        public Literal(PrimitiveType.Tag type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    public static class Factory {
        protected int start;
        protected int end;

        protected List<Token> tokens;

        public Factory(List<Token> tokens) {
            this.tokens = tokens;
        }

        public Factory at(int start, int end) {
            this.start = start;
            this.end = end;
            return this;
        }

        public Factory at(int pos) {
            this.start = pos;
            this.end = pos;
            return this;
        }

        public TranslationUnit TranslationUnit(List<YCTree> decls) {
            return setToken(new TranslationUnit(decls));
        }

        public VarDecl VarDecl(TypeTree type, Id id) {
            return setToken(new VarDecl(type, id));
        }

        public VarDecl VarDecl(TypeTree type, Id id, Expr init) {
            return setToken(new VarDecl(type, id, init));
        }

        public FuncDecl FuncDecl(TypeTree returnType, Id id, List<VarDecl> params, Block body) {
            return setToken(new FuncDecl(returnType, id, params, body));
        }

        public Block Block(List<Stmt> stmts) {
            return setToken(new Block(stmts));
        }

        public ExprStmt ExprStmt(Expr expr) {
            return setToken(new ExprStmt(expr));
        }

        public IfStmt IfStmt(Expr cond, Block thenBody, Block elseBody) {
            return setToken(new IfStmt(cond, thenBody, elseBody));
        }

        public WhileStmt WhileStmt(Expr cond, Block body) {
            return setToken(new WhileStmt(cond, body));
        }

        public ReturnStmt ReturnStmt(Expr expr) {
            return setToken(new ReturnStmt(expr));
        }

        public EmptyStmt EmptyStmt() {
            return setToken(new EmptyStmt());
        }

        public ContinueStmt ContinueStmt() {
            return setToken(new ContinueStmt());
        }

        public BreakStmt BreakStmt() {
            return setToken(new BreakStmt());
        }

        public BinaryExpr BinaryExpr(Operator operator, Expr lhs, Expr rhs) {
            return setToken(new BinaryExpr(operator, lhs, rhs));
        }

        public UnaryExpr UnaryExpr(Operator operator, Expr arg) {
            return setToken(new UnaryExpr(operator, arg));
        }

        public TypeCastExpr TypeCastExpr(TypeTree type, Expr expr) {
            return setToken(new TypeCastExpr(type, expr));
        }

        public FunCall FunCall(Id funcID, List<Expr> args) {
            return setToken(new FunCall(funcID, args));
        }

        public PrimitiveType PrimitiveType(PrimitiveType.Tag type) {
            return setToken(new PrimitiveType(type));
        }

        public Id Id(String name) {
            return setToken(new Id(name));
        }

        public Literal Literal(PrimitiveType.Tag type, Object value) {
            return setToken(new Literal(type, value));
        }

        protected <T extends Tree> T setToken(T tree) {
            tree.start = tokens.get(start);
            tree.end = tokens.get(end);
            return tree;
        }
    }

    // @formatter:off
    public interface VoidVisitor {
        default void visitOthers(YCTree that) {}
        default void visit(TranslationUnit that)      { visitOthers(that); }
        default void visit(VarDecl that)      { visitOthers(that); }
        default void visit(FuncDecl that)     { visitOthers(that); }
        default void visit(Block that)        { visitOthers(that); }
        default void visit(ExprStmt that)     { visitOthers(that); }
        default void visit(IfStmt that)       { visitOthers(that); }
        default void visit(WhileStmt that)    { visitOthers(that); }
        default void visit(ReturnStmt that)   { visitOthers(that); }
        default void visit(EmptyStmt that)    { visitOthers(that); }
        default void visit(ContinueStmt that) { visitOthers(that); }
        default void visit(BreakStmt that)    { visitOthers(that); }
        default void visit(BinaryExpr that)   { visitOthers(that); }
        default void visit(UnaryExpr that)    { visitOthers(that); }
        default void visit(TypeCastExpr that) { visitOthers(that); }
        default void visit(FunCall that)      { visitOthers(that); }
        default void visit(PrimitiveType that)         { visitOthers(that); }
        default void visit(Id that)           { visitOthers(that); }
        default void visit(Literal that)      { visitOthers(that); }
    }

    // @formatter:off
    public interface Visitor<R> {
        default R visitOthers(YCTree that) { return null;}
        default R visit(TranslationUnit that)      { return visitOthers(that); }
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
        default R visit(PrimitiveType that)         { return visitOthers(that); }
        default R visit(Id that)           { return visitOthers(that); }
        default R visit(Literal that)      { return visitOthers(that); }
    }
}
