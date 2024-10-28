import java.nio.file.Path;
import java.nio.file.Paths;

public class UserList {
   public static void show(Path currentDirectory) {
      System.out.println(currentDirectory);
      System.out.print(" >");
   }
}