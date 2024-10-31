package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Path currentDirectory = Paths.get(System.getProperty("user.dir"));
    boolean isAlive = true;
    UserList userList = new UserList();
    Scanner input = new Scanner(System.in);
    while (isAlive) {
      userList.show(currentDirectory);
      String line = input.nextLine();
      Split split = new Split(line);
      split.splitCommand();
      String command = split.getCommand();
      ArrayList<String> commandArgs = split.getCommandArgs();
      Command cmd = new Command(command, commandArgs);

      switch (command) {
        case "mkdir" -> {
          if (commandArgs.isEmpty()) {
            System.out.println("at least you should write one argument.");
          } else {
            cmd.makeDirectory(currentDirectory);
          }
        }
        case "rmdir" -> {
          if (commandArgs.isEmpty()) {
            System.out.println(
                    "at least you should write one argument to remove."
            );
          } else {
            cmd.removeDirectory(currentDirectory);
          }
        }
        case "rm" -> {
          if (commandArgs.isEmpty()) {
            System.out.println("at least you should write one file remove.");
          } else {
            cmd.removeFile(currentDirectory);
          }
        }
        case "touch" -> {
          try {
            cmd.touch(currentDirectory.toString());
          } catch(IOException e) {
            System.out.println("Cannot create file '" + e.getMessage() + "' no such path or directory");
          } catch(Exception e) {
            System.out.println(e.getMessage());
          }

        }
        case "mv" -> {
          try {
            cmd.mv(currentDirectory.toString());
          } catch(IOException e) {
            System.out.println("Cannot perform '" + e.getMessage() + "' Second path is wrong");
          } catch(Exception e) {
            System.out.println(e.getMessage());
          }

        }
        case "cat" -> {
          if (commandArgs.isEmpty()) {
            cmd.emptyCat();
          } else if (commandArgs.size() == 2 && (commandArgs.getFirst().equals(">") || commandArgs.getFirst().equals(">>"))) {
            // will take input and put it in file
            ArrayList<String> contentFromUser = cmd.takeCatInputFromUser();
            if (commandArgs.get(0).equals(">")) cmd.overWrite(contentFromUser, commandArgs.get(1), currentDirectory);
            else cmd.appendWrite(contentFromUser, commandArgs.get(1), currentDirectory);
          } else {
            ArrayList<String> contentFromFile = new ArrayList<>();
            int i = 0;
            for (; i < commandArgs.size(); i++) {
              if (commandArgs.get(i).equals(">") || commandArgs.get(i).equals(">>") || commandArgs.get(i).equals("|")) {
                break;
              }
              contentFromFile = cmd.takeCatInputFromFile(currentDirectory, commandArgs.get(i), contentFromFile); // Take Content Of Files
            }
            try {
              cmd.determineBehaviourOfCat(i, contentFromFile, currentDirectory);
            } catch (IllegalArgumentException e) {
              System.out.println(e.getMessage());
            }
          }
        }
        case "cd" -> {
          try {
            currentDirectory = cmd.callChangeDirectory(currentDirectory);
          } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
          }
        }
        case "pwd" -> {
          System.out.println(currentDirectory);
        }
        case "ls" -> {
          cmd.ls(currentDirectory);
        }
        case "help" -> {
          cmd.displayHelp();
        }
        case "exit" -> {
          isAlive = false;
        }
        default -> {
          System.out.printf("\'%s\'is not recognized as an internal command\n", command);
        }
      }
    }
  }
}
