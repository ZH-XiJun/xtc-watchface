# 小天才表盘逆向工程

## 项目简介

小天才手表的表盘文件存放在 `/vendor/res/theme/` 目录下，以 `.cl` 为扩展名。这些 `.cl` 文件实质上是 Android APK——厂商仅修改了后缀名，内部结构（AndroidManifest.xml、classes.dex、resources.arsc、res/）与标准 APK 完全一致。

本项目的逆向流程：

1. 从手表中提取目标表盘的 `.cl` 文件，将后缀改为 `.apk`
2. 解压 APK，从中提取出 `wonderland.aar`（AAR 是 Android Library 的打包格式，包含编译后的 class 和资源）
3. 使用 jadx 等反编译工具将 AAR 中的 `classes.jar` 反编译为 Java 源码
4. 将反编译后的 Java 文件、资源文件（PNG/XML 等）整理到标准 Android 项目中，修复依赖和编译问题
5. 保留一个最小化的 AAR（仅含 `com.xtc.common` 等闭源 SDK 类，不含已提取的 `com.xtc.wonderland` 类和冗余的 R 类），作为编译时依赖

最终项目结构：Java 源码可自由修改，闭源 SDK 类仍以 AAR 形式引入，可直接在 Android Studio 中编译运行。

## 使用方法

TODO
