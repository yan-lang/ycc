---
title: 词法规范
weight: 1
---

# 词法规范

{{< hint warning >}}
待编写
{{< /hint >}}

## YC0

以下是YC0的词法规则，使用正则表达式表示。

| 名称       | 符号                     |
| ---------- | ------------------------ |
| IDENTIFIER | `[A-Za-z_][A-Za-z0-9_]*` |
| INT_LIT    | `0|[1-9][0-9]*`          |
| FLOAT_LIT  | `(0|[1-9][0-9]*).[0-9]*` |


| 名称  | 符号 | 名称        | 符号       | 名称   | 符号 |
| ----- | ---- | ----------- | ---------- | ------ | ---- |
| ADD   | `+`  | KW_INT      | `int`      | LPAREN | `(`  |
| MINUS | `-`  | KW_FLOAT    | `float`    | RPAREN | `)`  |
| MULTI | `*`  | KW_VOID     | `void`     | LBRACE | `{`  |
| DIV   | `/`  | KW_IF       | `if`       | RBRACE | `}`  |
| GT    | `>`  | KW_ELSE     | `else`     | COLUMN | `;`  |
| GTE   | `>=` | KW_WHILE    | `while`    | COMMA  | `,`  |
| LT    | `<`  | KW_CONTINUE | `continue` |        |      |
| LTE   | `<=` | KW_BREAK    | `break`    |        |      |
| EQ    | `==` | KW_RETURN   | `return`   |        |      |
| NEQ   | `!=` |             |            |        |      |

## YC1

