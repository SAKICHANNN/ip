# Hhvrfn - Your Personal Task Manager

> "Your mind is for having ideas, not holding them." – David Allen ([source](https://gettingthingsdone.com/))

Hhvrfn frees your mind of having to remember things you need to do. It's,

- **text-based** 
- *easy to learn*
- ~~complex~~ **FAST** ⚡ SUPER FAST to use

All you need to do is,

1. Download it from [here](https://github.com/yourusername/ip)
2. Double-click the `hhvrfn.jar` file
3. Add your tasks using simple commands
4. Let it manage your tasks for you 😉

And it is **FREE**!

## Features

- [x] Managing ***todo*** tasks
- [x] Managing **deadlines** with dates (yyyy-MM-dd format)
- [x] Managing ***events*** with time ranges  
- [x] Marking tasks as done/undone with `mark`/`unmark`
- [x] Deleting tasks with `delete`
- [x] Listing all tasks with `list`
- [x] Finding tasks by keyword with `find`
- [x] Data persistence to local file
- [ ] Task priorities (coming soon)
- [ ] Reminders (coming soon)

## Command Examples

Here are the available commands you can try:

### Basic Task Management
```bash
# Add a simple todo task
todo read book

# Add a deadline task  
deadline return book /by 2019-10-15

# Add an event with time range
event project meeting /from Mon 2pm /to 4pm

# List all tasks
list

# Find tasks containing keyword
find book
```

### Task Operations
```bash
# Mark task as done (using 1-based index)
mark 1

# Mark task as not done
unmark 1

# Delete a task
delete 2

# Exit the application
bye
```

## Sample Interaction

Here's what a typical session looks like:

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
```

## Technical Architecture

If you are a Java programmer, you can use it to practice Java too. Here's the main method:

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

The application follows clean architecture principles with:

- **Separation of concerns** between `Ui`, `Parser`, `Storage`, and `TaskList`
- *Robust error handling* with custom `HhvrfnException`
- File-based persistence using a simple pipe-separated format:
  - `T | 1 | read book` (Todo task, done)
  - `D | 0 | return book | 2019-10-15` (Deadline task, not done)
  - `E | 0 | project meeting | Mon 2pm to 4pm` (Event task, not done)

## Task Types Supported

The application supports three types of tasks:

| Type | Symbol | Format | Example |
|------|---------|---------|---------|
| Todo | `[T]` | `todo DESCRIPTION` | `todo read book` |
| Deadline | `[D]` | `deadline DESCRIPTION /by YYYY-MM-DD` | `deadline return book /by 2019-10-15` |
| Event | `[E]` | `event DESCRIPTION /from TIME /to TIME` | `event meeting /from 2pm /to 4pm` |

---

*Built with ❤️ and Java for CS2103T Individual Project*
