# touch-event-gradle-plugin

gradle插件项目。

## 如何开发插件

### 简单示例

1、先移除项目对插件的依赖 将根目录的`gradle.properties`文件中`APPLY_GRADLE_PLUGIN`的值，改为非"false"即可，然后`sync`即可

```groovy
APPLY_GRADLE_PLUGIN = false
```

2、删除本地的local_repo目录

3、发布插件到本地仓库：

通过gradle命令行，执行发布的task即可。也可在AS的Gradle视图中执行对应的Task(推荐)。

v4:

```
执行发布到本地仓库的任务：
./gradlew touch-event-gradle-plugin:uploadArchives
# 或
./gradlew clean touch-event-gradle-plugin:uploadArchives
```

v7:

```
# 执行发布到本地仓库的任务：
./gradlew touch-event-gradle-plugin-v7:publishToLocalRepoPublicationToMavenRepository
# 或
./gradlew clean touch-event-gradle-plugin-v7:publishToLocalRepoPublicationToMavenRepository
```
```

4、增加app模块对插件的依赖。

将`APPLY_GRADLE_PLUGIN`的值，改为非"true"即可，然后`sync`即可

5、(可选)如果app依赖的jar、aar等也依赖了插件，则需要按照依赖顺序，重新编译jar包和aar包。具体实际请见下方。

6、(可选)查看插件的日志。打开Gradle视图，双击执行`app/Tasks/build/assemble`任务(任何能触发构建的Task均可)，在`Run`输出中搜索`Plugin-`
前缀，即可看到对应日志。

```
①这里推荐通过双击gradle视图中的Task方式。通过`/gradlew tasks`的方式，可能会出现gradle环境下jdk版本不一致导致的问题。
②插件中的日志输出，依赖的是`System.out.print(log)`;
```

7、运行app。

```
保险起见，也可以先删除app，重新安装。
adb uninstall com.hook.touch.event
```

8、Logcat中查看日志输出：`TouchEventDispatcher`

### 更新aar及其依赖的jar包

#### 如果app依赖jar包，更新jar包：jar-module

由于`app`模块依赖的jar包来自于`jar-module`，如果`touch-event-gradle-plugin`插件有更新，则需要重新生成jar包。

①确保`jar-module`依赖了最新版本的`touch-event-gradle-plugin`插件。

②执行`build`任务，生成jar包：Gradle视图 --> jar-module --> Tasks --> build --> build(双击执行任务)，任务执行完毕后会生成jar包

③[可选]执行`deleteJar`任务，删除上次生成的jar包（`jar-module/libs/jar-module-output.jar`）：Gradle视图 --> jar-module
--> Tasks --> other --> deleteJar(双击执行任务)

④执行`createJar`任务，copy jar到指定目录：Gradle视图 --> jar-module --> Tasks --> other --> createJar(双击执行任务)
，任务执行完毕后会生成jar包

⑤找到目标文件`jar-module/libs/jar-module-output.jar`，copy到`app/libs`目录下，重新运行即可

#### 如果app依赖aar包，更新aar包：aar-module

由于`app`模块依赖的aar包来自于`aar-module`，如果`touch-event-gradle-plugin`插件有更新，则需要重新生成aar包。

①确保`aar-module`依赖了最新版本的`touch-event-gradle-plugin`插件。 如果依赖的是jar包，确保libs目录下是最新的jar包

②执行`build`任务，生成aar包：Gradle视图 --> aar-module --> Tasks --> build --> build(双击执行任务)，任务执行完毕后会生成aar包

③找到目标文件`aar-module/build/outputs/aar/aar-module-debug.aar`，copy到`app/libs`目录下，重新运行即可

## 发布到maven仓库（本地仓库/远程仓库）

现在支持发布到本地自定义仓库，方便开发。

如需发布到maven仓库,
1、v4请自行修改插件源码的build.gradle文件中的uploadArchives方法。
2、v7请自行修改插件源码的build.gradle文件中的publishing方法。

### 如何发布到本地仓库

1、自定义仓库的对应配置： touch-event-gradle-plugin/build.gradle中，发布到了根目录下的local-repo目录中。

```groovy
def groupIdStr = 'touch.event.gradle.plugin'
def artifactIdStr = 'touch-event-gradle-plugin'
def versionStr = "${PLUGIN_VERSION_CODE}" // 0.0.1

//这两个是配置信息，后续会用到，命名自取
group = groupIdStr
version = versionStr

uploadArchives {
    repositories {
        mavenDeployer {
            // 本地的 Maven 地址设置
            // 部署到本地，也就是项目的根目录下
            // 部署成功会创建一个 local-repo 文件夹，命名自取
            repository(url: uri("${rootProject.projectDir}/${LOCAL_REPO_PATH}"))
        }
    }
}
```

2、通过gradle命令行，执行发布的task即可。也可在AS的Gradle视图中执行对应的Task(推荐)。

v4:

```
执行发布到本地仓库的任务：
./gradlew touch-event-gradle-plugin:uploadArchives
# 或
./gradlew clean touch-event-gradle-plugin:uploadArchives
```

v7:

```
# 执行发布到本地仓库的任务：
./gradlew touch-event-gradle-plugin-v7:publishToLocalRepoPublicationToMavenRepository
# 或
./gradlew clean touch-event-gradle-plugin-v7:publishToLocalRepoPublicationToMavenRepository
```

tips:需要保证`JAVA_HOME`是1.8，就是gradle的版本依赖的jdk版本是1.8

```
查看gradle版本
./gradlew -v

查看jdk版本
javac -version
```

### 如何从maven仓库引用

1、项目根目录的build.gradle中：

```groovy
    // 对应 resources/META-INF.gradle-plugins/xxx.properties 的文件名
apply plugin: 'touch.event.gradle.plugin'
buildscript {
    repositories {
        google()
        jcenter()
        // 如果是发布到MavenLocal就是用mavenLocal()
        //         mavenLocal()
        //自定义插件仓库地址
        maven {
            url uri("${rootProject.projectDir}/${LOCAL_REPO_PATH}")
        }
        // 已经发布到jcenter，不需要自己的仓库了
        // maven { url('https://dl.bintray.com/xxx/maven')}
    }
    dependencies {
        //加载自定义插件 group + module + version
        classpath "touch.event.gradle.plugin:touch-event-gradle-plugin:${PLUGIN_VERSION_CODE}"
    }
}
```

PLUGIN_VERSION_CODE定义在gradle.properties中，主要是为了调试方便。

```properties
# touch-event-gradle-plugin start
PLUGIN_VERSION_CODE=0.0.1
# touch-event-gradle-plugin end
```

## Transform场景适配

| 场景 | 复写 | 没有复写 | 备注 |
| --- | --- | --- | --- |
| app模块中的代码 | 支持 | 支持 |  |
| kotlin | 支持 | 支持 |  |
| module | 支持 | 支持 | 需要TransformManager.PROJECT_ONLY和每个module都apply插件才行 |
| aar | 支持 | 支持 | 默认不支持，需要aar也apply插件 |
| jar | 支持 | 支持 | app直接引入jar包。默认不支持，需要jar也apply插件。 |
| module + aar | 支持 | 支持 | module中需要用api引用aar，aar需要apply插件 |
| module + jar | 支持 | 支持 | 默认不支持。module中需要用api引用jar，需要jar也apply插件。 |
| aar + jar | 支持 | 支持 | 默认不支持。aar中需要用api引用jar，需要jar也apply插件。 |

## 切换v4和v7

| gradle插件版本 | gradle-sdk版本 | module |备注 |
| --- | --- | --- | --- |
| 4.2.0 | 6.7.1 | touch-event-gradle-plugin | 默认配置 |
| 7.2.0 | 7.3.3 | touch-event-gradle-plugin | 需要手动指定 USE_GRADLE_V7 为true |

### 切换v7:

①修改 `gradle.properties` 文件中的 `USE_GRADLE_V7` 为非"true"；

②然后修改gradle-sdk版本为7.3.3，去 `gradle-wrapper.properties` 文件中修改即可；

③切换到jdk11 Preferences → Build, Execution, Deployment → Build Tools → Gradle → *Gradle JDK

报错：Android Gradle plugin requires Java 11 to run. You are currently using Java 1.8. because Gradle 7
require java 11。

④然后 `sync`

### 切换v4:

①修改 `gradle.properties` 文件中的 `USE_GRADLE_V7` 为"false"；

②然后修改gradle-sdk版本为6.7.1，去 `gradle-wrapper.properties` 文件中修改即可；

③切换到jdk8。 Preferences → Build, Execution, Deployment → Build Tools → Gradle → *Gradle JDK

④然后 `sync`

## 外部项目如何引入插件：

### 1、classpath配置

```groovy
buildscript {
    dependencies {
        // 加载自定义插件 groupId + artifactId + version
        classpath "touch.event.gradle.plugin:touch-event-gradle-plugin:4.0.0.1-SNAPSHOT"
    }
}
```

这里的插件版本，针对不同的gradle tool版本是不同的。比如v4使用`4.0.0.1-SNAPSHOT`,v7使用`7.0.0.1-SNAPSHOT`

### 2、apply插件

```groovy
apply plugin: 'touch.event.gradle.plugin'
```

接着sync即可

### 3、app中引入配套的sdk
```groovy
api project(':touch-event-sdk')
```