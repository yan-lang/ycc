---
bookCollapseSection: true
title: Java
---

# Java框架指导

在本次课程的一系列实验中，你不需要也不应该自行编写整个编译器，我们提供一个编译器框架供你使用。

{{< hint warning >}}
目前只有通过这个框架编写的编译器才能够通过我们提供的自动评测系统完成评测，所以现阶段你必须使用该框架完成编译器的构建。
{{< /hint >}}

## 准备资料

该框架使用Java编写，以第三方库的形式发布，为了使用框架完成本次实验，你需要安装以下软件：

- **JDK11或更高版本**：框架使用Java11编写，因此你需要安装JDK11或更高版本。
- **Maven**：实验提供的模板项目使用Maven管理依赖，如果你选择使用模板的话你需要安装这个。
- **Git**：推荐使用Git进行版本控制，不是强制要求。
- **Intellij IDEA**：推荐使用IDEA作为你的集成开发环境，社区版即可。这不是强制要求，你也可以使用eclipse。

## 快速开始

{{< hint info >}}
下载IDEA模板项目：
{{< /hint >}}

模板项目是使用Idea创建的Maven项目，该模板是帮你写好了Maven的配置文件，然后写了一个主类。你下载下来的项目应该有下面这样的文件结构：

```shell
.
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── yan
│   │   │       └── ycc
│   │   │           └── impl
│   │   │               └── Main.java
│   │   └── resources
│   │       └── META-INF
│   │           └── MANIFEST.MF
│   └── test
│       └── java
└── ycc-template.iml
```

打开`Main.java`，你会发现里面已经写了一些代码了。这三行代码是使用我们的实验框架的最基础的代码。通过这三行代码，你可以得到一个具有命令行解析能力以及多功能解释器（REPL）的编译器&解释器（虽然还不能执行任何编译任务）。

```java
package yan.ycc.impl;

import yan.foundation.driver.Launcher;
import yan.ycc.api.YCLang;

public class Main {
    public static void main(String[] args) {
        YCLang lang = new YCLang(new TaskFactoryImpl());
        Launcher.instance(lang).commandName("ycc").launch(args);
    }
    public static class TaskFactoryImpl implements YCLang.TaskFactory {}
}
```

如果你使用IDEA，你可以点击Main旁边的绿色箭头编译运行整个项目。然后你会在控制台看到以下信息（提示缺少输入文件，事实上就是代码）。

```shell
Missing required parameter: <inputFile>
Usage: ycc [-hV] [-o=<outputFile>] [-t=<targetName>] <inputFile>
      <inputFile>   The file to be processed.
  -h, --help        Show this help message and exit.
  -o, --output=<outputFile>
                    Output file for result
  -t, --target=<targetName>
                    The stage you want to compile to. Valid values: . Default:
                      null
  -V, --version     Print version information and exit.
```

如果你以命令行的方式运行编译