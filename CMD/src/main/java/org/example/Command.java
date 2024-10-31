package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class Command {

  private String command;
  private ArrayList<String> commandArgs;

  public Command(String command, ArrayList<String> commandArgs) {
    this.command = command;
    this.commandArgs = commandArgs;
  }

  public void mv(String currentDir) throws Exception {
      if(commandArgs.isEmpty()) {
        throw new Exception("mv: missing file operand\nTry 'mv --help' for more information.");
      }
      if (commandArgs.size() == 1) {
        throw new Exception("mv: missing destination file operand after '" + commandArgs.get(0) + "'");
      }
      if (commandArgs.size() > 2) {
        throw new Exception("mv: target '" + commandArgs.getLast() + "' is not a directory");
      }
      String src = commandArgs.get(0);
      String dist = commandArgs.get(1);
      if (!Paths.get(src).isAbsolute()) {
        src = currentDir + "\\" + src;
      }
      if (!Paths.get(dist).isAbsolute()) {
        dist = currentDir + "\\" + dist;
      }
      if (!Files.exists(Paths.get(src))) {
        throw new Exception("mv: cannot stat '" + commandArgs.get(0) + "': No such file or directory");
      }
      Path srcP = Paths.get(src);
      Path distP = Paths.get(dist);
      Files.move(srcP, distP, StandardCopyOption.REPLACE_EXISTING);
  }

  public void touch(String currentDir) throws Exception {
    if(commandArgs.isEmpty()) {
      throw new Exception("touch: missing file operand\nTry 'touch --help' for more information.");
    }
    for (String f : commandArgs) {
        String file = f;
        if (!Paths.get(f).isAbsolute()) {
          file = currentDir + "\\" + f;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < file.length(); i++) {
          s.append(file.charAt(i));
          if (file.charAt(i) == '\\') s = new StringBuilder();
        }
        if (s.toString().length() > 255) {
          throw new Exception("touch: cannot touch '" + s.toString() + " ': File name too long");
        }
        Path p = Paths.get(file);
        if (Files.exists(p)) Files.delete(p);
        Files.createFile(p);
    }
  }

  public void displayHelp() {
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

  public void emptyCat() {
    System.out.println("Get-Content at command(Enter X To Get Out)");
    // Scanner catInput = new Scanner(System.in);
    boolean catRun = true;
    Scanner input = new Scanner(System.in);
    while (catRun) {
      String output = input.nextLine();
      if (output.toLowerCase().equals("x")) {
        break;
      }
      System.out.println("$" + output);
    }
  }

  public ArrayList<String> takeCatInputFromUser() {
    System.out.println("Get-Content at command(Enter X To Get Out)");
    ArrayList<String> outputToFile = new ArrayList<>();
    Scanner input = new Scanner(System.in);
    boolean catRun = true;
    while (catRun) {
      String output = input.nextLine();
      if (output.toLowerCase().equals("x")) {
        break;
      }
      outputToFile.add(output);
    }
    return outputToFile;
  }

  public ArrayList<String> takeCatInputFromFile(Path currentDirectory, String fileName, ArrayList<String> content) {
    try {
      Path newPath = currentDirectory.resolve(fileName).normalize();
      if (Files.exists(newPath) && !Files.isDirectory(newPath)) { // it is not directory or wrong path
        return readFile(newPath, content);
      } else {
        throw new IllegalArgumentException("InvalidInput"); // This path is Directory
      }
    }catch (Exception e){
      throw e;
    }
  }

  public void determineBehaviourOfCat(int index, ArrayList<String> content, Path currentDirectory) {
    if (index >= commandArgs.size()) {
      content.forEach(System.out::println);
      return;
    } else if (commandArgs.get(index).equals(">") || commandArgs.get(index).equals(">>")) {
      if (commandArgs.size() == index + 1 || commandArgs.size() > index + 2) {
        throw new IllegalArgumentException("MissingFileSpecification");
      }
      if (commandArgs.get(index).equals(">")) {
        overWrite(content, commandArgs.get(index + 1), currentDirectory);
      } else {
        appendWrite(content, commandArgs.get(index + 1), currentDirectory);
      }
    } else {
      ArrayList<String> subCommandArgs = new ArrayList<>(commandArgs.subList(index, commandArgs.size()));
      try {
        pipeLine(content, subCommandArgs);
      } catch(Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public Path callChangeDirectory(Path currentDirectory) {
    if (commandArgs.isEmpty()) { // can take no argument and then  it will be like pwd
      return currentDirectory;
    } else if (commandArgs.size() != 1) { // only one argument
      throw new IllegalArgumentException("The system cannot find the path specified.");
    } else {
      try {
        currentDirectory = changeDirectory(commandArgs.getFirst(), currentDirectory);
      }catch (Exception e) {
        throw new IllegalArgumentException(e);
      }
    }
    return currentDirectory;
  }

  public Path changeDirectory(String path, Path currentDirectory) {
    try {
      Path newPath = currentDirectory.resolve(path).normalize();
      if (Files.exists(newPath) && Files.isDirectory(newPath)) {
        return newPath;
      }
      throw new IllegalArgumentException("The system cannot find the path specified");
    } catch (Exception e) {
      throw new IllegalArgumentException("The system cannot find the path specified");
    }
  }

  public ArrayList<String> readFile(Path newPath, ArrayList<String> array) {
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

  public void overWrite(ArrayList<String> contents, String fileName, Path currentDirectory) {
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

  public void appendWrite(ArrayList<String> contents, String fileName, Path currentDirectory) {
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

  public void makeDirectory(Path currentDirectory) {
    for (String dirName : commandArgs) {
      try {
        Path newDir = currentDirectory.resolve(dirName).normalize();
        Files.createDirectory(newDir);
      } catch (IOException e) {
        System.out.println("Failed to create the directory: " + dirName);
      }
    }
  }

  public void removeDirectory(Path currentDirectory) {
    for (String dirName : commandArgs) {
      try {
        Path dirPath = currentDirectory.resolve(dirName).normalize();
        Files.delete(dirPath);
      } catch (IOException e) {
        System.out.println("Failed to remove the directory: " + dirName);
      }
    }
  }

  public void removeFile(Path currentDirectory) {
    for (String fileName : commandArgs) {
      try {
        Path filePath = currentDirectory.resolve(fileName).normalize();
        if (Files.isDirectory(filePath) || !Files.exists(filePath)) {
          throw new Exception("This Is Not A File");
        }
        Files.delete(filePath);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public static void pipeLine(ArrayList<String> content, ArrayList<String> commands) throws Exception {

      boolean more = false;
      int cnt = 0;
      for (int i = 0; i < commands.size(); i++) {

        if (commands.get(i).equals("|")) {
          cnt++;
        } else {
          cnt = 0;
        }
        if (cnt == 2) {
          throw new Exception("| was unexpected at this time.");
        }
        if(cnt == 1) {
          continue;
        }
        switch (commands.get(i)) {
          case "unique" -> {
            content = unique(content);
          }
          case "sort" -> {
            content = sort(content);
          }
          case "more" -> {
            more = true;
          }
          case "grep" -> {
            ++i;
            if (i == commands.size()) {
              throw new Exception("The syntax of the command is incorrect.");
            }
            if (commands.get(i).equals("|")) {
              throw new Exception("The syntax of the command is incorrect.");
            }
            content = grep(content, commands.get(i));
          }
          default -> {
            throw new Exception("The syntax of the command is incorrect.");
          }

        }
      }
      if(cnt == 1) {
        throw new Exception("The syntax of the command is incorrect");
      }
      if (more) {
        for (int i = 0; i < Math.min(5, content.size()); i++) {
          System.out.println(content.get(i));
        }
        int i = 5;
        Scanner input = new Scanner(System.in);
        while (i < content.size()) {
          System.out.println("--Press m for more--");
          char c;
          c = input.next().charAt(0);
          if (c == 'm') {
            ++i;
            System.out.println(content.get(i));
          } else {
            System.out.println("--You must press m--");
          }
        }
      } else {
        for (int i = 0; i < content.size(); i++) {
          System.out.println(content.get(i));
        }
      }
  }

  public static ArrayList<String> unique(ArrayList<String> content) {
    LinkedHashSet<String> tmp = new LinkedHashSet<>(content);
    return new ArrayList<>(tmp);

  }

  public static ArrayList<String> sort(ArrayList<String> content) {
    Collections.sort(content);
    return content;
  }

  public static ArrayList<String> grep(ArrayList<String> content, String part) {
    ArrayList<String> tmp = new ArrayList<>();
    for (String s:content) {
      if (s.contains(part)) {
        tmp.add(s);
      }
    }
    return tmp;
  }

  public void ls(Path currentDirectory) {
    boolean showAll = false;
    boolean reverseOrder = false;
    boolean redirect = false;
    boolean append = false;
    boolean pipe = false;
    String pathArg = null;
    String filePath = null;  //for redirection
    ArrayList<String> pipeArgs = new ArrayList<>();

    // Parse command arguments
    for (int i = 0; i < commandArgs.size(); i++) {
      String arg = commandArgs.get(i);

      switch (arg) {
        case "-a":
          showAll = true;
          break;
        case "-r":
          reverseOrder = true;
          break;
        case ">":
          redirect = true;
          if (i + 1 < commandArgs.size()) {
            filePath = commandArgs.get(i + 1);
            i++;
          } else {
            System.out.println("ls: Error - Missing file name after '>' for redirection.");
            return;
          }
          break;
        case ">>":
          append = true;
          if (i + 1 < commandArgs.size()) {
            filePath = commandArgs.get(i + 1);
            i++;
          } else {
            System.out.println("ls: Error - Missing file name after '>>' for append redirection.");
            return;
          }
          break;
        case "|":
          pipe = true;
          if (i + 1 < commandArgs.size()) {
            pipeArgs = new ArrayList<>(commandArgs.subList(i + 1, commandArgs.size()));
            i = commandArgs.size();
          } else {
            System.out.println("ls: Error - Missing command after '|'");
            return;
          }
          break;
        default:
          if (pathArg == null && !arg.startsWith("-")) {
            pathArg = arg; // Capture the first non-flag argument as pathArg } else {
            System.out.println("ls: Error - Unexpected argument: " + arg);
            return;
          }
          break;
      }
    }

    // Handle conflicting redirection arguments
    if (redirect && append) {
      System.out.println("ls: Error - Conflicting arguments '>' and '>>'. Use one or the other.");
      return;
    }

    Path targetDirectory = currentDirectory;

    // Resolve the target directory based on pathArg
    if (pathArg != null) {
      if (pathArg.equals("..")) {
        targetDirectory = currentDirectory.getParent() != null ? currentDirectory.getParent() : currentDirectory;
      } else if (pathArg.equals(".")) {
        targetDirectory = currentDirectory;
      } else {
        targetDirectory = currentDirectory.resolve(pathArg).normalize();
      }
    }

    File directory = targetDirectory.toFile();
    if (!directory.exists() || !directory.isDirectory()) {
      System.out.println("ls: Error - No such directory: '" + targetDirectory + "'");
      return;
    }

    String[] fileList = directory.list();
    if (fileList == null) {
      System.out.println("ls: Error - Unable to read contents of '" + targetDirectory + "'");
      return;
    }

    // Sort the file list
    Arrays.sort(fileList);
    if (reverseOrder) {
      Collections.reverse(Arrays.asList(fileList));
    }

    ArrayList<String> output = new ArrayList<>();
    for (String fileName : fileList) {
      if (!showAll && fileName.startsWith(".")) continue; // Skip hidden files if -a is not set
      output.add(fileName);
    }

    // Handle output options: redirection, appending, or piping
    if (redirect) {
      overWrite(output, filePath, currentDirectory);
    } else if (append) {
      appendWrite(output, filePath, currentDirectory);
    } else if (pipe) {
      if (pipeArgs.isEmpty()) {
        System.out.println("ls: Error - Missing command for pipe '|'");
        return;
      }
      try {
        pipeLine(output, pipeArgs);
      } catch (Exception e) {
        System.out.println("Error executing pipeline: " + e.getMessage());
      }
    } else {
      // Default output to console
      output.forEach(System.out::println);
    }
  }

}
