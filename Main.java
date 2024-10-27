import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

  public static void mv(ArrayList<String> commandArgs, String currentDir) {
      if(commandArgs.size() == 1) {
          System.out.println("mv: missing destination file operand after '" + commandArgs.get(0) + "'");
          return;
      }
      if(commandArgs.size() > 2) {
          System.out.println("mv: target '" + commandArgs.getLast() + "' is not a directory");
          return;
      }
      String src = commandArgs.get(0);
      String dist = commandArgs.get(1);
      if (!Paths.get(src).isAbsolute()) {
          src = currentDir + "\\" + src;
      }
      if (!Paths.get(dist).isAbsolute()) {
          dist = currentDir + "\\" + dist;
      }
      if(!Files.exists(Paths.get(src))) {
          System.out.println("mv: cannot stat '" + commandArgs.get(0) + "': No such file or directory");
          return;
      }

      try {
          Path srcP = Paths.get(src);
          Path distP = Paths.get(dist);
          Files.move(srcP, distP, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
          System.out.println("mv: cannot move '" + commandArgs.get(0) + "'to '" + commandArgs.get(1) + "': No such file or directory");
      }
      
  }

  public static void touch(ArrayList<String> commandArgs, String currentDir) {
      for(String f:commandArgs) {
            String file = f;
            if (!Paths.get(f).isAbsolute()) {
                file = currentDir + "\\" + f;
            }
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < file.length(); i++) {
                s.append(file.charAt(i));
                if(file.charAt(i) == '\\') s = new StringBuilder();
            }
            if(s.toString().length() > 255) {
                System.out.println("touch: cannot touch '" + s.toString() + " ': File name too long");
                continue;
            }
            Path p = Paths.get(file);
          try {
              if(Files.exists(p)) Files.delete(p);
              Files.createFile(p);
          } catch(IOException e) {
              System.out.println("touch: cannot touch '" + p + "': No such file or directory");
          }  
      }
  }

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
    try {
      Path newPath = currentDirectory.resolve(path).normalize();
      if (Files.exists(newPath) && Files.isDirectory(newPath)) {
        return newPath;
      }
      throw new FileNotFoundException();
    } catch (Exception e) {
      System.out.println("The system cannot find the path specified");
    }
    return currentDirectory;
  }

  public static ArrayList<String> readFile(
    Path newPath,
    ArrayList<String> array
  ) {
    try {
      File fileController = new File(newPath.toString());
      Scanner fileContent = new Scanner(fileController);
      while (fileContent.hasNextLine()) {
        array.add(fileContent.nextLine());
      }
      fileContent.close();
    } catch (FileNotFoundException e) {
      return array;
    }

    return array;
  }

  public static void overWrite(
    ArrayList<String> contents,
    String fileName,
    Path currentDirectory
  ) {
    try {
      Path newPath = currentDirectory.resolve(fileName).normalize();
      // Create File If There is file nothing will happend
      File fileController = new File(newPath.toString());
      fileController.createNewFile();
      // write on file
      FileWriter myWriter = new FileWriter(newPath.toString());
      contents.forEach(line -> {
        try {
          myWriter.write(line + "\n");
        } catch (IOException ex) {
          System.out.println("The specified path is invalid");
        }
      });
      myWriter.close();
    } catch (IOException e) {
      System.out.println("The specified path is invalid");
    }
  }

  public static void appendWrite(
    ArrayList<String> contents,
    String fileName,
    Path currentDirectory
  ) {
    try {
      Path newPath = currentDirectory.resolve(fileName).normalize();
      // Create File If There is file nothing will happend
      File fileController = new File(newPath.toString());
      fileController.createNewFile();
      // write on file
      FileWriter myWriter = new FileWriter(newPath.toString(), true);
      contents.forEach(line -> {
        try {
          myWriter.write(line + "\n");
        } catch (IOException ex) {
          System.out.println("The specified path is invalid");
        }
      });
      myWriter.close();
    } catch (IOException e) {
      System.out.println("The specified path is invalid");
    }
  }

  public static void makeDirectory(String dirName, Path currentDirectory) {
    Path newDir = currentDirectory.resolve(dirName).normalize();
    try {
      Files.createDirectory(newDir);
      System.out.println("Directory created: " + newDir);
    } catch (FileAlreadyExistsException e) {
      System.out.println("The directory already exists.");
    } catch (IOException e) {
      System.out.println("Failed to create the directory.");
    }
  }

  public static void removeDirectory(String dirName, Path currentDirectory) {
    Path dirPath = currentDirectory.resolve(dirName).normalize();
    try {
      Files.delete(dirPath);
      System.out.println("Directory removed: " + dirPath);
    } catch (NoSuchFileException e) {
      System.out.println("The directory does not exist.");
    } catch (DirectoryNotEmptyException e) {
      System.out.println("The directory is not empty.");
    } catch (IOException e) {
      System.out.println("Failed to remove the directory.");
    }
  }

  public static void removeFile(String fileName, Path currentDirectory) {
    Path filePath = currentDirectory.resolve(fileName).normalize();
    try {
      Files.delete(filePath);
      System.out.println("File removed: " + filePath);
    } catch (NoSuchFileException e) {
      System.out.println("The file does not exist.");
    } catch (IOException e) {
      System.out.println("Failed to remove the file.");
    }
  }

  public static void main(String[] args) {
    // CMD cmd = new CMD();
    Path currentDirectory = Paths.get(System.getProperty("user.dir"));
    boolean run = true;
    Scanner input = new Scanner(System.in);
    // overWrite(content, "file.txt", currentDirectory);
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
          if (commandArgs.size() != 1) {
            System.out.println(" number of arguments should be one .");
          } else {
            makeDirectory(commandArgs.get(0), currentDirectory);
          }
        }
        case "rmdir" -> {
          if (commandArgs.size() != 1) {
            System.out.println(" number of arguments should be one .");
          } else {
            removeDirectory(commandArgs.get(0), currentDirectory);
          }
        }
        case "rm" -> {
          if (commandArgs.size() != 1) {
            System.out.println(" number of arguments should be one .");
          } else {
            removeFile(commandArgs.get(0), currentDirectory);
          }
        }
        // mahmoud
        case "touch" -> {
          touch(commandArgs, currentDirectory.toString());
        }
        case "mv" -> {
          mv(commandArgs, currentDirectory.toString());
        }

        // Tolba
        case "cat" -> {
          if (commandArgs.isEmpty()) {
            System.out.println("Get-Content at command(Enter X To Get Out)");
            // Scanner catInput = new Scanner(System.in);
            boolean catRun = true;
            while (catRun) {
              String output = input.nextLine();
              if (output.toLowerCase().equals("x")) {
                break;
              }
              System.out.println("$" + output);
            }
          } else if (
            commandArgs.size() == 2 &&
            (commandArgs.get(0).equals(">") || commandArgs.get(0).equals(">>")) // will take input and put it in file
          ) {
            System.out.println("Get-Content at command(Enter X To Get Out)");
            ArrayList<String> outputToFile = new ArrayList<>();
            boolean catRun = true;
            while (catRun) {
              String output = input.nextLine();
              if (output.toLowerCase().equals("x")) {
                break;
              }
              outputToFile.add(output);
            }
            if (commandArgs.get(0).equals(">")) overWrite(
              outputToFile,
              commandArgs.get(1),
              currentDirectory
            ); else appendWrite(
              outputToFile,
              commandArgs.get(1),
              currentDirectory
            );
          } else {
            ArrayList<String> outputStrings = new ArrayList<>(); // save content of all files
            boolean overWriteOperator = false; // if i have overwrite operator then i will not print content
            for (int i = 0; i < commandArgs.size(); i++) {
              if (
                commandArgs.get(i).equals(">") ||
                commandArgs.get(i).equals(">>")
              ) { // Handle OverWrite Operator
                if (i == commandArgs.size() - 1) { // if there is no argument for file
                  System.out.println("MissingFileSpecification");
                } else if (i != commandArgs.size() - 2) { // if there more than one path
                  System.out.println("Cannot find path");
                } else { // right case
                  if (commandArgs.get(i).equals(">")) {
                    overWrite(
                      outputStrings,
                      commandArgs.get(i + 1),
                      currentDirectory
                    );
                  } else {
                    appendWrite(
                      outputStrings,
                      commandArgs.get(i + 1),
                      currentDirectory
                    );
                  }
                  overWriteOperator = true;
                  break;
                }
              }
              String path = commandArgs.get(i);
              Path newPath = currentDirectory;
              try {
                newPath = currentDirectory.resolve(path).normalize(); // if wrong path will not give error
                if (Files.exists(newPath) && !Files.isDirectory(newPath)) { // it is not directory or wrong path
                  outputStrings = readFile(newPath, outputStrings);
                } else {
                  throw new FileNotFoundException(); // This path is Directory
                }
              } catch (Exception e) {
                System.out.println("InvalidInput"); // error in file name
              }
            }
            if (!overWriteOperator) outputStrings.forEach(out ->
              System.out.println(out)
            );
          }
        }
        case "pwd" -> {
          System.out.println(currentDirectory);
        }
        case "cd" -> {
          if (commandArgs.isEmpty()) { // can take no argument and then  it will be like pwd
            System.out.println(currentDirectory);
          } else if (commandArgs.size() != 1) { // only one argument
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
