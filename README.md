# cmd-terminal-OS
implementation of cmd terminal OS  java code
# Command Line Interpreter (CLI)

This is a simple command line interpreter (CLI) program written in Java. It provides basic functionalities similar to a Unix-like terminal.

## Getting Started

To run the program, compile the `Terminal.java` file and execute the generated class file. Use the following commands:

```bash
javac Terminal.java
java Terminal
List of Commands
echo: Takes 1 argument and prints it.
pwd: Takes no arguments and prints the current directory's absolute path.
cd: Changes the current directory.
ls: Takes no arguments and lists the contents of the current directory sorted alphabetically.
ls -r: Takes no arguments and lists the contents of the current directory in reverse order.
mkdir: Takes one or more arguments and creates a new directory.
rmdir: Takes 1 argument and deletes a directory.
touch: Takes 1 argument and creates a file.
rm: Takes 1 argument and deletes a file.
cat: Takes 1 argument and prints the contents of a file or concatenates the contents of two files and prints them.
wc: Takes file name and used for counting lines, words, chars, and filename.
history: Displays an enumerated list of the commands you’ve used.
commands: Displays all commands available.
exit: Stops the program.
Usage Examples
To list the contents of the current directory:

bash
Copy code
ls
To change the directory:

bash
Copy code
cd <directory_name>
To create a new directory:

arduino
Copy code
mkdir <directory_name>
To delete a directory:

arduino
Copy code
rmdir <directory_name>
To create a new file:

bash
Copy code
touch <file_name>
To delete a file:

bash
Copy code
rm <file_name>
To print the contents of a file:

bash
Copy code
cat <file_name>
To display the command history:

bash
Copy code
history
To display all available commands:

Copy code
commands
To exit the program:

bash
Copy code
exit
Note
This CLI supports both absolute and relative paths.
Some commands support additional options as shown in the list above.
