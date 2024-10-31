import org.example.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeDirectiryTest {
  @TempDir
  Path currentDirectory;


  @Test
  @DisplayName("Without Arrguments")
  public void testWithoutArrguments() {
    ArrayList<String> commandArgs = new ArrayList<>();
    Command cmd = new Command("cd", commandArgs);
    assertEquals(currentDirectory, cmd.callChangeDirectory(currentDirectory));
  }


  @Test
  @DisplayName("More Than One Argument")
  public void testMoreThanOneArgument() {
    ArrayList<String> commandArgs = new ArrayList<>();
    commandArgs.add("..");
    commandArgs.add(".");
    Command cmd = new Command("cd", commandArgs);
    Assertions.assertThrows(Exception.class, () -> cmd.callChangeDirectory(currentDirectory));
  }


  @Test
  @DisplayName("Try To Change Dir to File")
  public void testTryToChangeDirToFile() {
    ArrayList<String> commandArgs = new ArrayList<>();
    commandArgs.add("file.txt");
    Command cmd = new Command("cd", commandArgs);
    Assertions.assertThrows(Exception.class, () -> cmd.callChangeDirectory(currentDirectory));
  }

  @Test
  @DisplayName("Good Input")
  public void testGoodInput() throws IOException {
    ArrayList<String> commandArgs = new ArrayList<>();
    commandArgs.add("f");
    Files.createDirectory(currentDirectory.resolve("f"));
//    System.out.println(currentDirectory);
    Command cmd = new Command("cd", commandArgs);
    Assertions.assertDoesNotThrow(() -> cmd.callChangeDirectory(currentDirectory));
    Assertions.assertEquals(currentDirectory.resolve("f"), cmd.callChangeDirectory(currentDirectory));
  }
}
