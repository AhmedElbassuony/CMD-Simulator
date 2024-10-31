package org.example;

import java.util.ArrayList;

public class Split {

  private String line;
  private String command;
  ArrayList<String> commandArgs;

  Split(String line) {
    this.line = line;
    commandArgs = new ArrayList<>();
  }

  public void splitCommand() {
    String[] commandParts = line.split("\\s+");
    command = commandParts[0].toLowerCase();
    StringBuilder tmp = new StringBuilder();
    boolean f = true;
    for (int i = 0; i < command.length(); i++) {
      if (f && command.charAt(i) == ' ') continue; else f = false;
      tmp.append(command.charAt(i));
    }
    command = tmp.toString();
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
  }

  public String getCommand() {
    return command;
  }

  public ArrayList<String> getCommandArgs() {
    return commandArgs;
  }
}
