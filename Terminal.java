
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


class Parser
{
    String commandName;
    String[] args;
    // This method parses the user input to separate the command name and arguments.
    public boolean parse(String input)
    {
        //split the input line into words when finding a space
        String[] word = input.split("\\s+");
        //when there is no input
        if (word.length < 1)
        {
            return false;
        }
        //the first word is always the command name
        commandName = word[0];
        if (word.length > 1)
        {
            // Handle special cases like "ls -r" or "cp -r" by modifying the command name.
            // Store the remaining words as arguments.
            if (commandName.equals("ls") && word[1].equals("-r"))
            {
                commandName += " -r";
            }
            else if (commandName.equals("cp") && word[1].equals("-r"))
            {
                commandName += " -r";
                args = new String[word.length - 2];
                //the loop starts with i=2 because i=0 "cp" and i=1 "-r" are commands name
                for (int i = 2; i < word.length; i++)
                {
                    args[i - 2] = word[i];
                }
            }
            // Other commands have arguments without special handling.
            else
            {
                args = new String[word.length - 1];
                for (int i = 1; i < word.length; i++)
                {
                    args[i - 1] = word[i];
                }
            }
        }
        else
        {
            args = new String[0];
        }
        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}


public class Terminal {
    Parser parser;
    //command history is a list of string putting it in a dynamic array
    private List<String> commandHistory = new ArrayList<>();

    public Terminal()
    {
        parser = new Parser();
    }

    //Takes 1 argument and prints it.
    public void echo(String[] args)
    {
        // Check if the "echo" command has exactly one argument.
        if (args.length == 1)
        {
            // If the argument count is correct, print the argument.
            System.out.println(args[0]);
        }
        else
        {
            // If there are more or fewer arguments than expected, display an error message.
            System.out.println("ERROR must be written: echo <word>");
        }
        // Add the "echo" command to the command history list.
        commandHistory.add("echo");
    }

    //Takes no arguments and prints the current path.
    public String pwd() {
        // Get the current directory path as a string.
        String curr_path = System.getProperty("user.dir");
        // Create a File object using the current directory path.
        File curr_directory = new File(curr_path);
        // Check if the current directory exists and is a directory.
        if (curr_directory.exists() && curr_directory.isDirectory())
        {
            // If the directory is valid, add "pwd" to the command history and return the path.
            commandHistory.add("pwd");
            return curr_path;
        }
        else
        {
            // If the directory is invalid, add "pwd" to the command history and return an error message.
            commandHistory.add("pwd");
            return "ERROR: this current directory does not exist";
        }

    }

    //changes the current path may take no argument or only 1
    public void cd(String[] args)
    {
        if (args.length == 0)
        {
            // Get the home directory path as a string.
            String home_dir = System.getProperty("user.home");
            // Change the current directory to the user's home directory.
            System.setProperty("user.dir", home_dir);
        }
        else if (args.length == 1)
        {
            String path = args[0];
            if (path.equals(".."))
            {
                // Get the current directory path as a string.
                String curr_directory = System.getProperty("user.dir");
                // Navigate to the parent directory by modifying the current directory path.
                Path parent_directory = Paths.get(curr_directory).getParent();
                String parent_dir = parent_directory.toString();
                System.setProperty("user.dir", parent_dir);
            }
            else
            {
                // Check if the specified path is a valid directory and change the current directory if it is.
                File new_dir = new File(path);
                if (new_dir.isDirectory())
                {
                    System.setProperty("user.dir", path);
                }
                else
                {
                    System.out.println("ERROR: directory does not exist");
                }

            }
        }
        else
        {
            // Display an error message if the user provides incorrect input.
            System.out.println("ERROR must be written: cd <directory>");
        }
        commandHistory.add("cd");
    }

    //Takes no arguments and lists the contents of the current directory sorted alphabetically.
    public void ls()
    {
        // Create a File object using the current directory path.
        File curr_directory = new File(System.getProperty("user.dir"));
        // Create an array File object putting in it the current directory content.
        File[] dir_content = curr_directory.listFiles();
        if (dir_content != null)
        {
            //sort the content alphabetically.
            Arrays.sort(dir_content);
            //display the content of the directory
            for (int i = 0; i < dir_content.length; i++)
            {
                System.out.print(dir_content[i].getName() + " ");
            }
        }
        System.out.println();
        commandHistory.add("ls");
    }

    //Takes no arguments and lists the contents of the current directory in reverse order.
    public void ls_r()
    {
        // Create a File object using the current directory path.
        File curr_directory = new File(System.getProperty("user.dir"));
        // Create an array File object putting in it the current directory content.
        File[] dir_content = curr_directory.listFiles();
        if (dir_content != null)
        {
            //sort the content alphabetically.
            Arrays.sort(dir_content);
            //display the content of the directory in reverse
            for (int i = dir_content.length - 1; i >= 0; i--)
            {
                System.out.print(dir_content[i].getName() + " ");
            }
        }
        System.out.println();
        commandHistory.add("ls -r");
    }

    //Takes 1 or more arguments and creates a directory for each argument.
    public void mkdir(String[] args)
    {
        if (args.length == 0)
        {
            // Display an error message when no directory names are provided.
            System.out.println("ERROR must be written: mkdir <directory1> <directory2> <directory3> ...");
        }
        else
        {
            for (int i = 0; i < args.length; i++)
            {
                String argument = args[i];
                // Get the path as a string.
                String path = System.getProperty("user.dir");

                // Check if the argument is a directory name or a path
                File new_directory;
                if (argument.contains(File.separator))
                {
                    // If the argument contains path separators, consider it as a full path.
                    // Create a File object using the path entered.
                    new_directory = new File(argument);
                }
                else
                {
                    // create a new directory relative to the current directory.
                    new_directory = new File(path, argument);
                }

                if (!new_directory.exists())
                {
                    // Check if the directory doesn't exist and create it if possible.
                    if (!new_directory.mkdir())
                    {
                        System.out.println("ERROR this " + argument + " directory cannot be created: ");
                    }
                }
                else
                {
                    System.out.println("ERROR this " + argument + " directory already exists");
                }
            }
        }
        commandHistory.add("mkdir");
    }

    //takes 1 argument and removes empty directories
    public void rmdir(String[] args)
    {
        int c = 0;
        if (args.length != 1)
        {
            // Display an error message if the user provides an incorrect number of arguments.
            System.out.println("ERROR must be written: rmdir <*> or rmdir <path>");
        }
        else
        {
            if (args[0].equals("*"))
            {
                // Remove all empty directories in the current directory when '*' is provided as an argument.
                // Create a File object using the current directory path.
                File curr_directory = new File(System.getProperty("user.dir"));
                // Create an array File object putting in it the current directory content.
                File[] dir_content = curr_directory.listFiles();
                if (dir_content != null)
                {
                    for (int i = 0; i < dir_content.length; i++)
                    {
                        //check if it is a directory and if it is empty
                        if (dir_content[i].isDirectory() && dir_content[i].length() == 0)
                        {
                            dir_content[i].delete();
                            //this count is used for knowing whether there is an empty directory or not
                            c++;
                        }
                    }
                    if (c == 0)
                    {
                        System.out.println("ERROR there is no empty files to be removed");
                    }
                }

            }
            else
            {
                File selected_dir = new File(args[0]);
                //check if the selected directory exists and is a normal directory
                if (selected_dir.exists() && selected_dir.isDirectory())
                {
                    // Create an array File object putting in it the selected directory content.
                    File[] dir_content = selected_dir.listFiles();

                    if (dir_content.length == 0)
                    {
                        // The directory is empty, so it can be removed
                        selected_dir.delete();
                    }
                    else
                    {
                        System.out.println("ERROR the selected directory is not empty, so it cannot be removed");
                    }
                }
                else if (selected_dir.exists() && selected_dir.isFile())
                {
                    System.out.println("ERROR this is a file. ");
                }
                else {
                    System.out.println("ERROR this directory does not exist");
                }

            }
        }
        commandHistory.add("rmdir");
    }

    //Takes 1 argument and creates this file.
    public void touch(String[] args)
    {
        if (args.length == 1)
        {
            // Check if the correct number of arguments is provided.
            File newFile = new File(args[0]);
            if (newFile.exists())
            {
                // Check if the file already exists and display an error message.
                System.out.println("ERROR this file already exists ");
            }
            else
            {
                try {
                    if (newFile.createNewFile())
                    {
                        // Attempt to create a new file. If successful, add the "touch" command to the history.
                        commandHistory.add("touch");
                        // Exit the function after successfully creating the file.
                        return;
                    }

                }
                catch (IOException e)
                {
                    System.out.println("ERROR cant create this file ");
                }
            }
        }
        else
        {
            System.out.println("ERROR must be written: touch <file> ");
        }
    }

    //Takes 1 argument which is a file name and removes this file.
    public void rm(String[] args)
    {
        // Check if the correct number of arguments is provided.
        if (args.length == 1)
        {
            //Creates a File object for the file specified by the first argument 'args[0]' within the current working directory
            File selected_file = new File(System.getProperty("user.dir"), args[0]);
            // Check if the file exists and is a regular file, then delete it.
            if (selected_file.exists() && selected_file.isFile())
            {
                selected_file.delete();
            }
            else if (selected_file.exists() && selected_file.isDirectory())
            {
                // Display an error message if the selected file is a directory.
                System.out.println("ERROR this is a directory ");
            }
            else
            {
                // Display an error message if the file does not exist.
                System.out.println("ERROR there is no file found by that name ");
            }
        } else
        {
            System.out.println("ERROR must be written: rm <file> ");
        }
        commandHistory.add("rm");
    }

    //Takes 2 arguments, both are files and copies the first onto the second.
    public void cp(String[] args)
    {
        // Check if the correct number of arguments is provided.
        if (args.length == 2)
        {
            //Creates File objects for source and destination
            File src_file = new File(args[0]);
            File dest_file = new File(args[1]);
            // Check if both source and destination files exist and are regular files.
            if (src_file.exists() && src_file.isFile() && dest_file.exists() && dest_file.isFile())
            {
                // Attempt to copy the content of 'src_file' to 'dest_file'
                try (InputStream source = new FileInputStream(src_file);// Open an input stream for the source file
                     FileOutputStream destination = new FileOutputStream(dest_file, true))// Open an output stream for the destination file (append mode)
                {

                    int data_elements;
                    // Read data from the source file and write it to the destination file
                    while ((data_elements = source.read()) != -1)
                    {
                        destination.write(data_elements);
                    }

                }
                catch (IOException e)
                {
                    System.out.println("ERROR copying the file.");
                }
            }
            // Check if both source and destination files exist and are directories.
            else if (src_file.exists() && src_file.isDirectory() || dest_file.exists() && dest_file.isDirectory())
            {
                System.out.println("ERROR this is a directory ");
            }
            else
            {
                System.out.println("ERROR there is no file found by that name");
            }
        }
        else
        {
            System.out.println("ERROR must be written: cp <source_file> <destination_file>");
        }
        commandHistory.add("cp");
    }

    //Takes 2 arguments, both are directories and copies the first directory into the second one.
    public void cp_r(String[] args)
    {
        // Check if the correct number of arguments is provided.
        if (args.length == 2)
        {
            //Creates File objects for source and destination
            File src_directory = new File(args[0]);
            File dest_directory = new File(args[1]);
            // Check if both source directory exist and is regular directories.
            if (src_directory.exists() && src_directory.isDirectory())
            {
                //if destination directory doesnt exist create it using mkdir function
                if (!dest_directory.exists())
                {
                    dest_directory.mkdirs();
                }
                // If both source and destination directories exist, start the recursive copy.
                if (dest_directory.exists() && dest_directory.isDirectory())
                {
                    copy_dir_content(src_directory, dest_directory);
                }
                else if (dest_directory.exists() && dest_directory.isFile())
                {
                    System.out.println("ERROR this is a file not a directory");
                }
                else
                {
                    System.out.println("ERROR there is no directory found by that name.");
                }
            }
            else if (src_directory.exists() && src_directory.isFile())
            {
                System.out.println("ERROR this is a file not a directory");
            }
            else
            {
                System.out.println("ERROR there is no directory found by that name.");
            }

        }
        else
        {
            System.out.println("ERROR must be written: cp -r <source_directory> <destination_directory>");

        }
        commandHistory.add("cp -r");

    }
    // Recursively copy the contents of a source directory to a destination directory a function that helps cp -r.
    public void copy_dir_content(File src_directory, File dest_directory)
    {
        // Create an array File object putting in it the source directory content.
        File[] src_dir_content = src_directory.listFiles();

        if (src_dir_content != null)
        {
            for (int i = 0; i < src_dir_content.length; i++)
            {
                if (src_dir_content[i].isDirectory())
                {
                    // If the item is a subdirectory, create a new directory in the destination and recursively copy its content.
                    File new_dir = new File(dest_directory, src_dir_content[i].getName());
                    new_dir.mkdirs();
                    copy_dir_content(src_dir_content[i], new_dir);
                }
                else
                {
                    // If the item is a file, create a new file in the destination and copy its content.
                    File new_file = new File(dest_directory, src_dir_content[i].getName());
                    if (!new_file.exists())
                    {
                        try (InputStream source = new FileInputStream(src_dir_content[i]);//Open an input stream for the source file
                             OutputStream destination = new FileOutputStream(new_file))// Open an output stream for the destination file
                        {
                            int data_elements;
                            // Read data from the source file and write it to the destination file.
                            while ((data_elements = source.read()) != -1) {
                                destination.write(data_elements);
                            }

                        }
                        catch (IOException e)
                        {
                            System.out.println("ERROR copying this file");
                        }
                    }
                }
            }
        }
    }

    //Takes 1 argument and prints the file’s content or takes 2 arguments and concatenates the content of the 2 files and prints it.
    public void cat(String[] args)
    {
        // Check if only one argument is provided.
        if (args.length == 1)
        {
            File f = new File(args[0]);
            // Check if the provided argument is a valid file.
            if (f.exists() && f.isFile())
            {
                try (InputStream f_data = new FileInputStream(f))//Open an input stream for the selected file
                {
                    int data_elements;
                    // Read data from the selected file and display it.
                    while ((data_elements = f_data.read()) != -1)
                    {
                        System.out.print((char) data_elements);
                    }
                }
                catch (IOException e)
                {
                    System.out.println("ERROR reading that file " );
                }
                System.out.println();
            }
            else if (f.exists() && f.isDirectory())
            {
                System.out.println("ERROR this is a directory not a file");
            }
            else
            {
                System.out.println("ERROR there is no file found by that name.");
            }
        }
        // Check if two arguments are provided.
        else if (args.length == 2)
        {
            File f1 = new File(args[0]);
            File f2 = new File(args[1]);
            // Check if both provided arguments are valid files.
            if (f1.exists() && f1.isFile() && f2.exists() && f2.isFile())
            {
                try (InputStream f1_data = new FileInputStream(f1);//Open an input stream for the first file
                     InputStream f2_data = new FileInputStream(f2))//Open an input stream for the second file
                {
                    int data_elements;
                    // Read data from the first file and display it.
                    while ((data_elements = f1_data.read()) != -1)
                    {
                        System.out.print((char) data_elements);
                    }
                    System.out.println();
                    // Read data from the second file and display it.
                    while ((data_elements = f2_data.read()) != -1)
                    {
                        System.out.print((char) data_elements);
                    }
                }
                catch (IOException e)
                {
                    System.out.println("ERROR reading that file.");
                }
                System.out.println();
            }
            else if (f1.exists() && f1.isDirectory()|| f2.exists() && f2.isDirectory())
            {
                System.out.println("ERROR this is a directory not a file");
            }
            else
            {
                System.out.println("ERROR there is no file found by that name.");
            }
        }
        else
        {
            System.out.println("ERROR must be written: cat <file> or cat <file1> <file2>");
        }
        commandHistory.add("cat");
    }

    //wc is mainly used for counting purpose.
    public void wc(String[] args)
    {
        // Check if only one argument is provided.
        if (args.length == 1)
        {
            String file_name = args[0];
            File curr_file = new File(file_name);
            // Check if the provided argument is a valid file.
            if (curr_file.exists() && curr_file.isFile())
            {
                try (Scanner reader = new Scanner(curr_file))
                {
                    int line_cnt = 0;
                    int word_cnt = 0;
                    int char_cnt = 0;
                    // Read the file line by line
                    while (reader.hasNextLine())
                    {
                        String line = reader.nextLine();
                        line_cnt++;
                        // Update character count based on the line's length
                        char_cnt += line.length();
                        // Split the line into words when finding a whitespace
                        String[] words = line.split("\\s+");
                        word_cnt += words.length;
                    }
                    System.out.println(line_cnt + " " + word_cnt + " " + char_cnt + " " + file_name);
                }
                catch (IOException e)
                {
                    System.out.println("ERROR reading the file.");
                }
            }
            else if (curr_file.exists() && curr_file.isDirectory())
            {
                System.out.println("ERROR this is a directory not a file");
            }
            else
            {
                System.out.println("ERROR there is no file found by that name.");
            }

        }
        else
        {
            System.out.println("ERROR must be written: wc <file>");

        }
        commandHistory.add("wc");
    }

    // Display the command history with line numbers.
    public void history() {
        int i;
        for ( i = 0; i < commandHistory.size(); i++) {
            System.out.println((i + 1) + "-" + commandHistory.get(i));
        }
        System.out.println((i + 1) + "-history");
    }

    public void commands(){
        System.out.println("echo   -> takes 1 argument and prints it..");
        System.out.println("pwd    -> takes no arguments and return an absolute (full) path.");
        System.out.println("cd     -> change the directory.");
        System.out.println("ls     -> takes no arguments and lists the contents of the current directory sorted alphabetically");
        System.out.println("ls -r  -> takes no arguments and lists the contents of the current directory in reverse order.");
        System.out.println("mkdir  -> takes one or more arguments and make a new directory.");
        System.out.println("rmdir  -> takes 1 argument and delete a directory.");
        System.out.println("touch  -> takes 1 argument and create a file ");
        System.out.println("rm     -> takes 1 argument and delete a file");
        System.out.println("cat    -> takes 1 argument and Prints all contents in files.");
        System.out.println("wc     -> takes file name and used for counting data 'lines ,words ,chars ,filename'.");
        System.out.println("history   -> displays an enumerated list with the commands you’ve used");
        System.out.println("commands   -> display all command to help you.");
        System.out.println("exit   -> stop program.");
        commandHistory.add("commands");
    }
    public void chooseCommandAction(String commandName, String[] args)
    {
        switch (commandName)
        {
            case "echo":
                echo(args);
                break;
            case "pwd":
                System.out.println(pwd());
                break;
            case "cd":
                cd(args);
                break;
            case "ls":
                ls();
                break;
            case "ls -r":
                ls_r();
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "rmdir":
                rmdir(args);
                break;
            case"touch":
                touch(args);
                break;
            case"cp":
                cp(args);
                break;
            case"cp -r":
                cp_r(args);
                break;
            case"rm":
                rm(args);
                break;
            case"cat":
                cat(args);
                break;
            case"wc":
                wc(args);
                break;
            case"history":
                history();
                break;
            case "commands":
                commands();
                break;
            default:
                System.out.println("Unknown command: " + commandName+" choose a command from the CLI menu you can enter 'commands' to display the menu");
        }
    }

    public static void main(String[] args)
    {
        Parser parser = new Parser();
        Terminal terminal = new Terminal();
        String command;
        String[] arguments;
        String input_string;
        boolean flag = true ;
        //creates a new Scanner object  that reads input from the standard input stream
        Scanner input = new Scanner(System.in);
        // Display the available commands to the user.
        System.out.println("COMMAND LINE INTERPRETER (CLI)");
        System.out.println("here is the list of commands that you can excute: ");
        System.out.println(" 1)echo \n 2)pwd \n 3)cd \n 4)ls \n 5)ls -r \n 6)mkdir \n 7)rmdir \n 8)touch \n 9)cp \n 10)cp -r \n 11)rm \n 12)cat \n 13)wc \n 14)history \n 15)commands \n 16)exit");
        while (flag)
        {
            System.out.print("> ");
            //It reads a line of text (the command) from the user's input and store it in the input_string variable.
            input_string = input.nextLine();
            if (input_string.equals("exit"))
            {
                flag = false ;
                break ;
            }
            // Parse the user input to separate the command name and arguments.
            if(parser.parse(input_string) )
            {
                command = parser.getCommandName();
                arguments = parser.getArgs();
                // Execute the appropriate action based on the parsed command.
                terminal.chooseCommandAction(command ,arguments);
            }
        }
        System.out.println("exit");
        input.close();
    }
}
