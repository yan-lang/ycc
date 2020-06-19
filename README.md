# YCC 编译原理实验

Work in progress.

## 快速开始

1. **引入依赖**： “言”编译器框架和 YCC 实验框架均使用 Java 开发，学生可以使用 Maven 进行 依赖管理。Maven 的依赖导入方式是在 Maven 项目中的 pom.xml 添加以下内容，其 中 ycc 为 YCC 实验框架，yan-foundation 和 yan-common 属于编译器框架。

```xml
<dependencies>
    <dependency>
        <groupId>com.github.yan-lang</groupId>
        <artifactId>ycc</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.github.yan-lang</groupId>
        <artifactId>yan-foundation</artifactId>
        <version>0.1.3-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.github.yan-lang</groupId>
        <artifactId>yan-common</artifactId>
        <version>0.1.3-SNAPSHOT</version>
    </dependency>
</dependencies>  
```

2. **程序入口**：导入依赖之后，构建一个可执行但没有任何编译目标的编译器非常简单，只需要创建一个Main文件，填入下列内容。

```java
import yan.foundation.driver.Launcher;
import yan.ycc.api.YCLang;

public class Main {
    public static void main(String[] args) {
        YCLang lang = new YCLang(new TaskFactoryImpl());
        Launcher.instance(lang).commandName("ycc").launch(args);
    }
    static class TaskFactoryImpl implements YCLang.TaskFactory {}
}
```

3. **编译并在命令行运行**，得到代码所示输出，说明程序正确运行了，但是没有传入正确的命令行参数（缺少输入文件），由于没有可用的编译目标，Target的默认值是null。

```shell
Missing required parameter: <inputFile>
Usage: ycc [-hV] [-o=<outputFile>] [-t=<targetName>] <inputFile>
      <inputFile>   The file to be processed.
  -h, --help        Show this help message and exit.
  -o, --output=<outputFile>
                    Output file for result
  -t, --target=<targetName>
                    The stage you want to compile to. 
                    Valid values: . Default:
                      null
  -V, --version     Print version information and exit.
```

4. **构建词法分析器**：使用框架构建一个词法分析器，并整合进刚才实现的编译器中也十分简单。首先，编写一个词法分析器。该步骤需要创建一个类，并继承AbstractYCLexer，重写nextToken函数，该函数每被调用一次都应返回当前代码中的下一个单词。

```java
public class Lexer extends AbstractYCLexer {
    @Override
    public Token nextToken() {
        // buffer为文本缓存区, 定义在父类中, 
        // makeToken也是父类提供的辅助函数。

        if(buffer.current() == '+') { return makeToken(ADD);}
        //....
    }
}
```

5. 随后，重写TaskFactoryImpl中的lex函数，创建一个刚刚创建的词法分析器对象，以代码\ref{code:ycc-lexer-task}所示的形式返回。

```java
 public static class TaskFactoryImpl implements YCLang.TaskFactory {
     @Override
     public Optional<Phase<Code, List<Token>>> lex(boolean isInterpreting)
     {
         return Optional.of(new Lexer());
     }
}
```

6. 完成编写之后，编译生成jar包，可以使用两种方式运行，REPL方式和编译方式。

```shell
❯ java -jar --enable-preview ycc.jar 
Welcome to ycc version 1.0(default, Wed May 27 21:09:58 CST 2020)
Type :help for assistance.
  1> :target lex
Successfully set target to lex
  2> int main() {}
0.  int  kw int            loc=stdin.c:1:1;0:3    val="null" 
1.  main identifier        loc=stdin.c:1:5;4:8    val="main" 
2.  (    left parenthesis  loc=stdin.c:1:9;8:9    val="null" 
3.  )    right parenthesis loc=stdin.c:1:10;9:10  val="null" 
4.  {    left brace        loc=stdin.c:1:12;11:12 val="null" 
5.  }    right brace       loc=stdin.c:1:13;12:13 val="null" 
6.       EOF               loc=stdin.c:1:14;13:13 val="null" 

java -jar --enable-preview ycc.jar simple.c -o simple.xml -t lex
```

7. 提交之前需要将程序打包成jar包。然后通过评测网站的词法分析页面提交这个jar包。评测网站返回的评测报告将类似于下方所展示的样子。


```shell
Lexer Grading Report
--------------------
Date: 2020-04-02 11:49:10.885866
File: keyword.xml
Preview:
- Student total: 19
- Gold total: 19
- correct: 19/19
- error: 0/19
- redundant: 0/19
- missing: 0/19

Detail:
1. [correct] - Token(KW_INT)
2. [correct] - Token(KW_FLOAT)
3. [correct] - Token(KW_VOID)
4. [correct] - Token(KW_IF)
...
```

