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
      }
      input.close();
   }
}