import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Command {
   private String command;
   private ArrayList<String> commandArgs;
   Command(String command, ArrayList<String> commandArgs) {
      this.command = command;
      this.commandArgs = commandArgs;
   }
   public void mv(String currentDir) {
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
   public void touch(String currentDir) {
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

<<<<<<< HEAD
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

   public Path changeDirectory(String path, Path currentDirectory) {
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

   public ArrayList<String> readFile(
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
   public void overWrite(
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
   public void appendWrite(
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

   public void makeDirectory(Path currentDirectory) {
   for (String dirName : commandArgs) {
        Path newDir = currentDirectory.resolve(dirName).normalize();
        try {
            Files.createDirectory(newDir);
            System.out.println("Directory created: " + newDir);
        } catch (FileAlreadyExistsException e) {
            System.out.println("The directory '" + dirName + "' already exists.");
        } catch (IOException e) {
            System.out.println("Failed to create the directory: " + dirName);
        }
    }
   }

   public void removeDirectory(Path currentDirectory) {
    for (String dirName : commandArgs) {
        Path dirPath = currentDirectory.resolve(dirName).normalize();
        try {
            Files.delete(dirPath);
            System.out.println("Directory removed: " + dirPath);
        } catch (NoSuchFileException e) {
            System.out.println("The directory '" + dirName + "' does not exist.");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("The directory '" + dirName + "' is not empty.");
        } catch (IOException e) {
            System.out.println("Failed to remove the directory: " + dirName);
        }
    }
   }

   public void removeFile(Path currentDirectory) {
    for (String fileName : commandArgs) {
        Path filePath = currentDirectory.resolve(fileName).normalize();
        try {
            Files.delete(filePath);
            System.out.println("File removed: " + filePath);
        } catch (NoSuchFileException e) {
            System.out.println("The file '" + fileName + "' does not exist.");
        } catch (IOException e) {
            System.out.println("Failed to remove the file: " + fileName);
        }
    }
   }



   public static void pipeLine(ArrayList<String> content, ArrayList<String> commands) {
      boolean more = false;
      boolean less = false;
      int cnt = 0;
      for(int i = 0; i < commands.size(); i++) {
         if(commands.get(i).equals("|")) {
            cnt++;
            continue;
         } else {
            cnt = 0;
         }
         if(cnt == 2) {
            System.out.println("| was unexpected at this time.");
            return;
=======
   public static void pipeLine(ArrayList<String> content, ArrayList<String> commands) {
      boolean more = false;
      boolean less = false;
      for(int i = 0; i < commands.size(); i++) {
         if(commands.get(i).equals("|")) {
            ++i;
>>>>>>> 5421bbe9c67c403ca7f1863b47e8e96006b2ebfc
         }
         if(i ==commands.size()) {
            System.out.println("The syntax of the command is incorrect.");
            return;
         }
         switch(commands.get(i)) {
            case "unique" -> {
               content = unique(content);
            }
            case "sort" -> {
               content = sort(content);
            }
            case "more" -> {
               more = true;
            }
            case "less" -> {
               less = true;
            }
            case "grep" -> {
               ++i;
               if(commands.get(i).equals("|")) {
                  System.out.println("The syntax of the command is incorrect.");
                  return;
               }
               content = grep(content, commands.get(i));
            }
            default -> {
               System.out.println("The syntax of the command is incorrect.");
               return;
            }

         }
      }
      if(less && more) {
         System.out.println("The syntax of the command is incorrect.");
      } else if (more) {
         for(int i = 0; i < Math.min(5, content.size()); i++) {
            System.out.println(content.get(i));
         }
         int i = 5;
<<<<<<< HEAD
         Scanner input1 = new Scanner(System.in);
         while(i < content.size()) {
            System.out.println("--Press m for more--");
            char c;
            c = input1.next().charAt(0);
=======
         Scanner input = new Scanner(System.in);
         while(i < content.size()) {
            System.out.println("--Press m for more--");
            char c;
            c = input.next().charAt(0);
>>>>>>> 5421bbe9c67c403ca7f1863b47e8e96006b2ebfc
            if(c == 'm') {
               ++i;
               System.out.println(content.get(i));
            } else {
               System.out.println("--You must press m--");
            }
         }
      } else if(less) {
         //---> i do not know yet what I should do
      } else {
         for(int i = 0; i < content.size(); i++) {
            System.out.println(content.get(i));
         }
      }
   }
   public static ArrayList<String> unique(ArrayList<String> content) {
      ArrayList<String> tmp = new ArrayList<>();
      Collections.sort(content);
      if(!content.isEmpty()) {
         tmp.add(content.get(0));
         for(int i = 1; i < content.size(); i++) {
            if(i > 0 && content.get(i) != content.get(i - 1)) {
               tmp.add(content.get(i));
            }
         }
      }
      return tmp;
   }
   public static ArrayList<String> sort(ArrayList<String> content) {
      Collections.sort(content);
      return content;
   }
   public static ArrayList<String> grep(ArrayList<String> content, String part) {
      ArrayList<String> tmp = new ArrayList<>();
      for(int i = 0; i < content.size(); i++) {
         if(content.get(i).contains(part)) {
            tmp.add(content.get(i));
         }
      }
      return tmp;
   }




}