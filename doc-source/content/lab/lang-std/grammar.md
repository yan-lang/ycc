---
title: 语法规范
weight: 2
---

# 语法规范

{{< hint warning >}}
编写中
{{< /hint >}}

我们使用扩展BNF范式描述我们的文法。

> 以下内容修改自[Decaf语言规范](https://decaf-lang.gitbook.io/decaf-book/spec#wen-fa-gui-fan)。
>
> - `*` 表示一个符号出现任意多次，包括零次
> - `+` 表示一个符号出现至少一次
> - `?` 表示一个符号可选，即至多出现一次
> - `|` 分隔多个产生式右部，无顺序
> - `ε` 表示空，即不存在任何符号
>
> 所有终结符要么以单引号字符串的形式直接出现，或者用以全大写字母的标识符代替，非终结符对应的标识符均以大写字母开头,并使用驼峰命名法。此外，我们还使用 () 来显式限定上述元符号作用的符号或者符号串。

## YC1

终结符的词法规则见[词法规范 | YC1]({{< relref "lexical#yc1" >}})。

为了让文法尽可能的简单，我们没有将许多语义显示的注入文法当中。对于详细的语义要求，如表达式优先级等，请查看[语义说明 | YC1]({{< relref "semantic#yc1" >}})。

```BNF
/* 程序结构 */
Program  ::=  Decl*
Decl     ::=  VarDecl
           |  FuncDecl

Block     ::=  Stmt*
Stmt      ::=  VarDecl
            |  ExprStmt
            |  IfStmt
            |  WhileStmt
            |  ReturnStmt
            |  EmptyStmt

EmptyStmt  ::= ';'

/* 变量 */
VarDecl  ::=  Type Id '=' Expr ';'
Type    ::=  'int' | 'float' | 'void'

/* 表达式 */
Expr  ::= '(' Expr ')'         // 括号表达式
        | Literal              // 常量, 如1.34, 10
        | Id                   // 标识符也是表达式
        | FunCall              // 函数调用
        | UnaryExpr            // 一元表达式
        | BinaryExpr           // 二元表达式
        | TypeCastExpr         // 类型转换

Literal   ::=  INT_LIT | FLOAT_LIT
Id        ::=  IDENTIFIER

FunCall   ::=  Id '(' ArgList ')'
ArgList   ::=  Expr ((',' Expr)* | ε)  // 实参列表

UnaryExpr     ::= UnaryOp Expression
UnaryOp       ::=  '-'

BinaryExpr    ::=  Expr BinaryOp Expr
BinaryOp      ::=  '*' | '/' | '+' | '-' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '='

TypeCastExpr  ::=  '(' Type ')' Expression

/* 分支 */
IfStmt  ::=  'if' '(' Expr ')' Block ( 'else' Block )?

/* 循环 */
WhileStmt     ::=  'while' '(' Expr ')' Block
ContinueStmt  ::=  'continue' ';'
BreakStmt     ::=  'break' ';'

/* 函数 */
FuncDecl    ::=  Type Id '(' ParaList ')' Block
ParaList    ::=  Para ((',' Para)* | ε)           // 形参列表
Para        ::=  Type Id
ReturnStmt  ::=  'return' Expression? ';'

```

## YC2

