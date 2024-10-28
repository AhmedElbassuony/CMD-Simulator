import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
   public static void main(String[] args) {
      Path currentDirectory = Paths.get(System.getProperty("user.dir"));
      boolean isAlive = true;
      UserList userList = new UserList();
      Scanner input = new Scanner(System.in);
      while(isAlive) {
         userList.show(currentDirectory);
         String line = input.nextLine();
         Split split = new Split(line);
         split.splitCommand();
         String command = split.getCommand();
         ArrayList<String> commandArgs = split.getCommandArgs();
         Command cmd = new Command(command, commandArgs);
<<<<<<< HEAD
         switch(command) {
            case "mkdir" -> {
               if (commandArgs.isEmpty()) {
               System.out.println("at least you should write one argument.");
            } else {
               cmd.makeDirectory(currentDirectory);

            }
            }
            case "rmdir" -> {
          if (commandArgs.isEmpty()) {
            System.out.println("at least you should write one argument to remove.");
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
          cmd.touch(currentDirectory.toString());
         }
         case "mv" -> {
          cmd.mv(currentDirectory.toString());
         }
         case "pwd" -> {
          System.out.println(currentDirectory);
         }

      }
   }
}
=======
      }
      input.close();
   }
>>>>>>> 5421bbe9c67c403ca7f1863b47e8e96006b2ebfc
}