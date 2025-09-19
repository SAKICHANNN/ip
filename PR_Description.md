# 🚀 Hhvrfn - Your Personal Task Manager

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-11+-blue.svg)](https://openjfx.io/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](#)

> *"Your mind is for having ideas, not holding them."* – David Allen ([source](https://gettingthingsdone.com/))

Hhvrfn is a **fast**, **intuitive**, and **reliable** personal task manager that frees your mind from having to remember things you need to do.

## 📋 Table of Contents

- [✨ Features](#-features)
- [🚀 Quick Start](#-quick-start)
- [📖 Usage Guide](#-usage-guide)
- [🛠️ Technical Architecture](#️-technical-architecture)
- [🔧 Installation](#-installation)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

## ✨ Features

### 🎯 Core Task Management
- [x] **Todo Tasks** - Simple task tracking with `todo` command
- [x] **Deadline Tasks** - Tasks with due dates using `deadline` command
- [x] **Event Tasks** - Time-based events with `event` command
- [x] **Task Operations** - Mark, unmark, delete, and find tasks
- [x] **Smart Search** - Find tasks by keyword with `find` command
- [x] **Task Rescheduling** - Reschedule deadlines with `snooze` command
- [x] **Data Persistence** - Automatic saving to local file

### 🛡️ Error Handling & User Experience
- [x] **Comprehensive Error Handling** - User-friendly error messages with helpful suggestions
- [x] **Input Validation** - Smart validation for commands, dates, and indices
- [x] **Contextual Help** - Built-in `help` command with examples
- [x] **Graceful Recovery** - Handles missing files, permission issues, and corrupted data
- [x] **Date Validation** - Ensures dates are within reasonable bounds (1900-2100)
- [x] **Length Limits** - Prevents issues with overly long inputs

### 🔧 Technical Features
- [x] **Dual Interface** - Both CLI and GUI (JavaFX) versions available
- [x] **Comprehensive Logging** - File-based logging with timestamps for debugging
- [x] **Cross-Platform** - Runs on Windows, macOS, and Linux
- [x] **Robust File Operations** - Handles various file system scenarios
- [x] **Clean Architecture** - Well-structured codebase following SOLID principles

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- JavaFX 11+ (for GUI version)

### Installation
1. **Download** the latest release from [here](https://github.com/yourusername/ip/releases)
2. **Run** the application:
   ```bash
   java -jar hhvrfn.jar
   ```
3. **Start managing** your tasks! 🎉

### First Steps
```bash
# See available commands
help

# Add your first task
todo read a book

# Add a deadline
deadline submit assignment /by 2024-12-25

# List all tasks
list
```

## 📖 Usage Guide

### 📝 Basic Commands

<details>
<summary>Click to expand command reference</summary>

#### Task Management
| Command | Format | Example |
|---------|--------|---------|
| `todo` | `todo DESCRIPTION` | `todo read a book` |
| `deadline` | `deadline DESCRIPTION /by YYYY-MM-DD` | `deadline submit assignment /by 2024-12-25` |
| `event` | `event DESCRIPTION /from TIME /to TIME` | `event team meeting /from 2pm /to 4pm` |
| `list` | `list` | Lists all tasks |
| `find` | `find KEYWORD` | `find book` |

#### Task Operations
| Command | Format | Example |
|---------|--------|---------|
| `mark` | `mark INDEX` | `mark 1` |
| `unmark` | `unmark INDEX` | `unmark 1` |
| `delete` | `delete INDEX` | `delete 2` |
| `snooze` | `snooze INDEX /to YYYY-MM-DD` | `snooze 1 /to 2024-12-31` |

#### Utility
| Command | Description |
|---------|-------------|
| `help` | Show comprehensive help with examples |
| `bye` | Exit the application |

</details>

### 🎯 Task Types

| Type | Symbol | Description | Example |
|------|---------|-------------|---------|
| **Todo** | `[T]` | Simple tasks without dates | `[T][✓] read book` |
| **Deadline** | `[D]` | Tasks with due dates | `[D][ ] submit report (by: Dec 25 2024)` |
| **Event** | `[E]` | Time-based activities | `[E][ ] team meeting (from: 2pm to: 4pm)` |

### 🚨 Error Handling Examples

Hhvrfn provides intelligent error handling with helpful suggestions:

```bash
# Invalid command
invalidcommand
# ❌ Error: Unknown command. Try: list, todo, deadline, event...
# 💡 Tip: Type 'list' to see your tasks, 'todo <description>' to add a task

# Empty description
todo
# ❌ Error: Todo needs a non-empty description.
# 💡 Tip: Make sure to provide a description for your task.

# Invalid date
deadline test /by invalid-date
# ❌ Error: Invalid date. Use yyyy-MM-dd, e.g., 2019-10-15.
# 💡 Tip: Use the format yyyy-MM-dd for dates (e.g., 2024-12-25).
```

### 📱 Sample Session

<details>
<summary>Click to see a complete interaction example</summary>

```
____________________________________________________________
Hello! I'm hhvrfn
What can I do for you?
____________________________________________________________

todo read book
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

deadline return book /by 2019-10-15
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________

list
____________________________________________________________
 Here are the tasks in your list:
 1. [T][ ] read book
 2. [D][ ] return book (by: Oct 15 2019)
____________________________________________________________

mark 1
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
```

</details>

## 🛠️ Technical Architecture

### 🏗️ Core Components

```java
public class Hhvrfn {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public static void main(String[] args) {
        new Hhvrfn("./data/hhvrfn.txt").run();
    }
}
```

### 📦 Class Structure

| Component | Responsibility |
|-----------|----------------|
| `Hhvrfn` | Main application controller |
| `Parser` | Command parsing and validation |
| `TaskList` | Task collection management |
| `Storage` | File I/O operations |
| `Ui` | User interface handling |
| `Logger` | Application logging |

### 🗂️ Data Format

Tasks are stored in a simple pipe-separated format:
```
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 0 | project meeting | Mon 2pm to 4pm
```

### 🛡️ Error Handling Capabilities

<details>
<summary>Comprehensive error scenario coverage</summary>

#### User Input Errors ✅
- Invalid or unknown commands
- Empty task descriptions
- Overly long descriptions (>1000 characters)
- Invalid date formats or out-of-range dates
- Invalid task indices or non-numeric input

#### File System Errors ✅
- Missing data files (creates new ones automatically)
- Permission denied (provides resolution steps)
- Insufficient disk space (actionable advice)
- Corrupted data files (graceful recovery)

#### Edge Cases ✅
- Very long input strings
- Multiple whitespace characters
- Integer overflow in indices
- Concurrent file access scenarios

</details>

### 📊 Logging System

- **File-based logging** with timestamps for debugging
- **Multiple log levels** (INFO, WARN, ERROR)
- **Automatic log management** with graceful fallback
- **Application lifecycle tracking** for monitoring

## 🔧 Installation

### From Source

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ip.git
   cd ip
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   java -cp build/classes/java/main hhvrfn.Hhvrfn
   ```

### System Requirements

- **Java**: 11 or higher
- **Memory**: 50MB RAM minimum
- **Storage**: 10MB available space
- **OS**: Windows 10+, macOS 10.14+, or Linux with GUI support

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Development Setup

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass (`./gradlew test`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Built with ❤️ and Java for CS2103T Individual Project**

[⬆ Back to top](#-hhvrfn---your-personal-task-manager)

</div>
