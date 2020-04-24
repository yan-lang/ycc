package yan.ycc.api;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.semantic.v1.Type;

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
        public Type type;
        public Id name;
        public Expr init;

        public VarDecl(Type type, Id name) {
            this(type, name, null);
        }

        public VarDecl(Type type, Id name, Expr init) {
            this.type = type;
            this.name = name;
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
        public Type returnType;
        public Id name;
        public List<VarDecl> params;
        public Block body;

        public FuncDecl(Type returnType, Id name, List<VarDecl> params, Block body) {
            this.returnType = returnType;
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    public static abstract class Stmt extends YCTree {
    }

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

    /**
     * While循环
     * <pre>
     *     while(expression) {
     *         body
     *     }
     * </pre>
     */
    public static class WhileStmt extends Stmt {
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
        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    public static abstract class Expr extends YCTree {

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
        public Type type;
        public Expr expr;

        public TypeCastExpr(Type type, Expr expr) {
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
        public Id name;
        public List<Expr> args;

        public FunCall(Id name, List<Expr> args) {
            this.name = name;
            this.args = args;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}

    }

    /**
     * 数据类型
     * <pre>
     *     int, float, void
     * </pre>
     */
    public static class Type extends YCTree {
        public TypeTag type;

        public Type(TypeTag type) {
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
        public TypeTag type;
        public Object value;

        public Literal(TypeTag type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(VoidVisitor visitor) { visitor.visit(this);}
    }

    public enum TypeTag {
        INT,
        FLOAT,
        VOID,
    }

    public enum Operator {
        MULTI,
        DIV,
        PLUS,
        MINUS,
        GT,
        GTE,
        LT,
        LTE,
        EQ,
        NEQ,
        ASSIGN,
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

        public VarDecl VarDecl(Type type, Id name) {
            return setToken(new VarDecl(type, name));
        }

        public VarDecl VarDecl(Type type, Id name, Expr init) {
            return setToken(new VarDecl(type, name, init));
        }

        public FuncDecl FuncDecl(Type returnType, Id name, List<VarDecl> params, Block body) {
            return setToken(new FuncDecl(returnType, name, params, body));
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

        public TypeCastExpr TypeCastExpr(Type type, Expr expr) {
            return setToken(new TypeCastExpr(type, expr));
        }

        public FunCall FunCall(Id name, List<Expr> args) {
            return setToken(new FunCall(name, args));
        }

        public Type Type(TypeTag type) {
            return setToken(new Type(type));
        }

        public Id Id(String name) {
            return setToken(new Id(name));
        }

        public Literal Literal(TypeTag type, Object value) {
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
        default void visit(Type that)         { visitOthers(that); }
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
        default R visit(Type that)         { return visitOthers(that); }
        default R visit(Id that)           { return visitOthers(that); }
        default R visit(Literal that)      { return visitOthers(that); }
    }
}
