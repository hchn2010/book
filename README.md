# 📖 墨香书影

> 一款专为爱书人设计的轻量级阅读记录工具。告别繁琐，帮你轻松管理书架，沉淀每一刻的阅读时光。

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-7F52FF.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-6.0+-3DDC84.svg)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Build APK](https://github.com/hchn2010/book/actions/workflows/build.yml/badge.svg)](https://github.com/hchn2010/book/actions/workflows/build.yml)

---

## 📱 功能特色

### 📚 智能书架管理
- 支持手动录入书籍信息（书名、作者、封面）
- 自定义“在读 / 想读 / 已读”三种状态，书架分类一目了然
- 打造专属于你的视觉书库

### ⏱️ 阅读计时与进度追踪
- 阅读计时器：记录每次沉浸阅读的时长
- 进度打卡：轻点即可更新当前阅读页数或百分比
- 阅读日历：直观展示每日阅读活跃度，让坚持看得见

### ✍️ 灵感笔记与摘抄
- 阅读过程中随时记录灵感随笔或金句摘抄
- 笔记与具体书籍关联，方便日后回顾

### 📊 数据统计看板
- 自动汇总：累计阅读时长、本月读完书籍、年度阅读报告
- 帮你复盘阅读习惯，见证从量变到质变的成长

---

## 📸 截图

> 🖼️ 待补充：App 界面截图（建议放 3-5 张核心界面截图）

| 书架 | 阅读计时 | 数据统计 |
| :---: | :---: | :---: |
| ![书架](screenshots/shelf.png) | ![计时](screenshots/timer.png) | ![统计](screenshots/stats.png) |

---

## 📦 下载

请从右侧 **Releases** 页面下载最新版本的 APK 安装包。

👉 [前往 Releases 下载](https://github.com/hchn2010/book/releases)

| 版本 | 下载 |
| :--- | :--- |
| v1.0.0 | [app-debug.apk](https://github.com/hchn2010/book/releases/tag/v1.0.0) |

> 📌 当前版本为调试版（Debug），仅供测试体验。正式版将在后续发布。

---

## 🛠️ 技术栈

- **语言**：Kotlin 2.0.0
- **最低 SDK**：Android 6.0（API 23）
- **目标 SDK**：Android 14（API 34）
- **架构**：MVVM
- **依赖注入**：待定
- **数据库**：Room
- **网络请求**：Retrofit（如需要）

---

## 🚀 本地构建

如果你想要自行编译，请按以下步骤操作：

```bash
# 1. 克隆项目
git clone https://github.com/hchn2010/book.git

# 2. 进入项目目录
cd book

# 3. 使用 Gradle 构建 Debug APK
./gradlew assembleDebug

# 4. 构建产物位于
app/build/outputs/apk/debug/app-debug.apk
