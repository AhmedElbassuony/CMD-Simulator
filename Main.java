import java.nio.file.*;
import java.util.Scanner;

public class Main {

  public static void displayHelp() {
    System.out.println("Available commands:");
    System.out.println("cd [directory]       : Change the current directory.");
    System.out.println(
      "pwd                 : Print the current working directory."
    );
    System.out.println(
      "ls                  : List files in the current directory."
    );
    System.out.println(
      "ls -a               : List all files, including hidden ones."
    );
    System.out.println("ls -r               : List files in reverse order.");
    System.out.println("mkdir [directory]   : Create a new directory.");
    System.out.println("rmdir [directory]   : Remove an empty directory.");
    System.out.println("touch [file]        : Create a new file.");
    System.out.println(
      "mv [src] [dest]     : Move or rename a file or directory."
    );
    System.out.println("rm [file]           : Remove a file.");
    System.out.println("cat [file]          : Display the contents of a file.");
    System.out.println(
      "> [file]            : Redirect output to a file, overwriting."
    );
    System.out.println(
      ">> [file]           : Redirect output to a file, appending."
    );
    System.out.println(
      "|                   : Pipe the output of one command to another."
    );
    System.out.println("exit                : Exit the CLI.");
    System.out.println("help                : Display this help message.");
  }

  // not commplete
  public static Path changeDirectory(String path) {
    Path newPath = Paths.get(path);
    newPath = newPath.toAbsolutePath().normalize();
    return newPath;
  }

  public static void main(String[] args) {
    // CMD cmd = new CMD();
    Path currentDirectory = Paths.get(System.getProperty("user.dir"));
    Scanner input = new Scanner(System.in);
    boolean run = true;
    while (run) {
      System.out.println(currentDirectory);
      System.out.print(" >");
      String line = input.nextLine();
      String[] commandParts = line.split("\\s+");
      String command = commandParts[0].toLowerCase();
      String[] commandArgs = new String[commandParts.length - 1];
      for (int i = 0; i < commandArgs.length; i++) {
        commandArgs[i] = commandParts[i + 1];
      }
      switch (command) {
        // Mohamed
        case "ls" -> {
          // You Will Put The Function and handle 3 casses
          // first arrgument is -a
          // first arrgument is -r
          // normal
        }
        
        // mustafa
        case "mkdir" -> {
          // You Will Put The Function and handle 3 casses
        }
        case "rmdir" -> {
          // You Will Put The Function and handle 3 casses
        }
        case "rm" -> {
          // You Will Put The Function and handle 3 casses
        }
        
        // mahmoud
        // case "touch" -> {}
        // case "mv" -> {}
        
        // Tolba
        // case "cat" -> {}
        case "pwd" -> {
          System.out.println(currentDirectory);
        }
        case "cd" -> {
          if (commandArgs.length == 0) {
            System.out.println(currentDirectory);
          } else if (commandArgs.length != 1) {
            System.out.println("The system cannot find the path specified.");
          } else {
            currentDirectory = changeDirectory(commandArgs[0]);
          }
        }
        case "help" -> {
          displayHelp();
        }
        case "exit" -> {
          run = false;
        }
        default -> {
          System.out.printf(
            "\'%s\'is not recognized as an internal command\n",
            command
          );
        }
      }
    }
  }
}
