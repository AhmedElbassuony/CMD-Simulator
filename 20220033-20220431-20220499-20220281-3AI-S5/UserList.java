package org.example;

import java.nio.file.Path;

public class UserList {

  public static void show(Path currentDirectory) {
    System.out.println(currentDirectory);
    System.out.print(" >");
  }
}
