import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
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

  public static Path changeDirectory(String path, Path currentDirectory) {
    Path newPath = currentDirectory.resolve(path).normalize();
    if (Files.exists(newPath) && Files.isDirectory(newPath)) {
      return newPath;
    } else {
      System.out.println("The system cannot find the path specified");
    }
    return currentDirectory;
  }

  public static void overWrite(
    ArrayList<String> contents,
    String fileName,
    Path currentDirectory
  ) {
    Path newPath = currentDirectory.resolve(fileName).normalize();
    if (true) {
      try {
        File fileController = new File(newPath.toString());
        fileController.createNewFile();
        FileWriter myWriter = new FileWriter(newPath.toString());
        contents.forEach(line -> {
          try {
            myWriter.write(line+"\n");
          } catch (IOException ex) {
            System.out.println("The specified path is invalid");
          }
        });
        myWriter.close();
      } catch (IOException e) {
        System.out.println("The specified path is invalid");
      }
    }
  }

  public static void main(String[] args) {
    // CMD cmd = new CMD();
    Path currentDirectory = Paths.get(System.getProperty("user.dir"));
    Scanner input = new Scanner(System.in);
    boolean run = true;
    ArrayList<String> content = new ArrayList<>();
    content.add("line_1");
    content.add("line_2");
    content.add("line_3");
    overWrite(content, "file.txt", currentDirectory);
    while (run) {
      System.out.println(currentDirectory);
      System.out.print(" >");
      String line = input.nextLine();
      String[] commandParts = line.split("\\s+");
      String command = commandParts[0].toLowerCase();
      ArrayList<String> commandArgs = new ArrayList<>();
      String handleQutations = "";
      //handle arguments
      for (int i = 1; i < commandParts.length; i++) {
        String currentString = commandParts[i];
        if (currentString.charAt(0) != '\"' && handleQutations.isEmpty()) {
          commandArgs.add(currentString);
        } else if (
          currentString.charAt(0) == '\"' || !handleQutations.isEmpty()
        ) {
          handleQutations += " " + currentString;
        }
        if (handleQutations.endsWith("\"")) {
          commandArgs.add(
            handleQutations.substring(2, handleQutations.length() - 1) // Deletet first space and " and last "
          );
          handleQutations = "";
        }
      }

      switch (command) {
        // Mohamed
        case "ls" -> {
          // System.out.println(commandArgs.toString());
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
          if (commandArgs.isEmpty()) {
            System.out.println(currentDirectory);
          } else if (commandArgs.size() != 1) {
            System.out.println("The system cannot find the path specified.");
          } else {
            currentDirectory =
              changeDirectory(commandArgs.get(0), currentDirectory);
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
    input.close();
  }
}
