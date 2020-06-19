---
title: 词法规范
weight: 1
---

# 词法规范

{{< hint warning >}}
待编写
{{< /hint >}}

## YC1

以下是YC1的词法规则，使用正则表达式表示。

| 名称       | 符号                       |
| ---------- | -------------------------- |
| IDENTIFIER | `[A-Za-z_][A-Za-z0-9_]*`   |
| INT_LIT    | `(0|[1-9])[0-9]*`          |
| FLOAT_LIT  | `((0|[1-9])[0-9]*).[0-9]*` |

| 名称   | 符号 | 名称        | 符号       | 名称      | 符号 |
| ------ | ---- | ----------- | ---------- | --------- | ---- |
| ADD    | `+`  | KW_INT      | `int`      | LPAREN    | `(`  |
| MINUS  | `-`  | KW_DOUBLE   | `double`   | RPAREN    | `)`  |
| MULTI  | `*`  | KW_VOID     | `void`     | LBRACE    | `{`  |
| DIV    | `/`  | KW_IF       | `if`       | RBRACE    | `}`  |
| GT     | `>`  | KW_ELSE     | `else`     | SEMICOLON | `;`  |
| GTE    | `>=` | KW_WHILE    | `while`    | COMMA     | `,`  |
| LT     | `<`  | KW_CONTINUE | `continue` | REL_AND   | `&&` |
| LTE    | `<=` | KW_BREAK    | `break`    | REL_OR    | `||` |
| EQ     | `==` | KW_RETURN   | `return`   | REL_NOT   | `!`  |
| NEQ    | `!=` |             |            |           |      |
| ASSIGN | `=`  |             |            |           |      |

## YC2

新增单词类型如下

| 名称     | 符号  |
| -------- | ----- |
| LBRACKET | `[`   |
| RBRACKET | `]`   |
| KW_FOR   | `for` |
