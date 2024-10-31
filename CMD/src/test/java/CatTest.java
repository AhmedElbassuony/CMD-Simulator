import org.example.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


public class CatTest {

  @TempDir
  Path currentDirectory;

  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private final InputStream systemInBackup = System.in;


  @Test
  @DisplayName("Empty Arguments")
  public void EmptyTest() {
    // Every output will ritten in outputStreamCapture instead of terminal in test
    System.setOut(new PrintStream(outputStreamCaptor));

    String simulatedInput = "Line1\nLine2\nLine3\nx\n";
    // Take input Standard To Doesn't Need User To Input In Test
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());

    System.setIn(inputStream); // Redirect input To inputStream

    Command cmd = new Command("cat", new ArrayList<String>());
    cmd.emptyCat();

    String expectedOutput = "Get-Content at command(Enter X To Get Out)\r\n$Line1\r\n$Line2\r\n$Line3";
    Assertions.assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());

    System.setIn(systemInBackup); // Restore original System.in
    System.setOut(System.out);    // Restore original System.out
  }

  @Test
  @DisplayName("Take Input From User")
  public void TakeInputFromUserTest() {
    System.setOut(new PrintStream(outputStreamCaptor));

    String simulatedInput = "Line1\nLine2\nLine3\nx\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(inputStream);

    Command cmd = new Command("cat", new ArrayList<>());
    ArrayList<String> out = cmd.takeCatInputFromUser();

    Assertions.assertEquals("[Line1, Line2, Line3]", out.toString());

    System.setIn(systemInBackup);
    System.setOut(System.out);
  }

  @Test
  @DisplayName("Take Input From File (Empty File)")
  public void TakeInputFromEmptyFileTest() throws IOException {
    Path newpath = currentDirectory.resolve("newpath.txt").normalize();
    Files.createFile(newpath);
    Command cmd = new Command("cat", new ArrayList<>());

    ArrayList<String> content = new ArrayList<>();


    content = cmd.takeCatInputFromFile(currentDirectory, "newpath.txt", content);
    Assertions.assertEquals("[]", content.toString());
  }


  @Test
  @DisplayName("Take Input From File")
  public void TakeInputFromFileTest() throws IOException {
    Path newpath = currentDirectory.resolve("newpath.txt").normalize();
    Files.createFile(newpath);

    FileWriter myWriter = new FileWriter(newpath.toString());
    myWriter.write("Line1\nLine2\nLine3\n");
    myWriter.close();
    Command cmd = new Command("cat", new ArrayList<>());

    ArrayList<String> content = new ArrayList<>();


    content = cmd.takeCatInputFromFile(currentDirectory, "newpath.txt", content);
    Assertions.assertEquals("[Line1, Line2, Line3]", content.toString());
  }


  @Test
  @DisplayName("Take input From Invalid File")
  public void TakeInputFromInvalidFileTest() {
    Command cmd = new Command("cat", new ArrayList<>());
    Assertions.assertThrows(Exception.class, () -> {
      cmd.takeCatInputFromFile(currentDirectory, "newpath.txt", new ArrayList<>());
    });
  }


  @Test
  @DisplayName("Determine Cat Behaviour in the End of Arguments")
  public void DetermineCatBehaviourInTheEndOfArgumentsTest() {
    ArrayList<String> commandArgs = new ArrayList<>();
    commandArgs.add("Command 1");
    commandArgs.add("Command 2");
    commandArgs.add("Command 3");
    Command cmd = new Command("cat", commandArgs);
    ArrayList<String> content = new ArrayList<>();
    content.add("Line 1");
    content.add("Line 2");
    content.add("Line 3");
    System.setOut(new PrintStream(outputStreamCaptor));
    cmd.determineBehaviourOfCat(3, content, currentDirectory);
    Assertions.assertEquals("Line 1\r\nLine 2\r\nLine 3", outputStreamCaptor.toString().trim());
  }

  @Test
  @DisplayName("Determine Cat Behaviour in Case Of < Or <<")
  public void DetermineCatBehaviourInTheOverWriteTest() {
    ArrayList<String> commandArgs = new ArrayList<>();
    commandArgs.add("Command 1");
    commandArgs.add("Command 2");
    commandArgs.add(">");
    commandArgs.add("Command 4");

    Command cmd = spy(new Command("cat", commandArgs));

    ArrayList<String> content = new ArrayList<>();
    content.add("Line 1");
    content.add("Line 2");
    content.add("Line 3");

    cmd.determineBehaviourOfCat(2, content, currentDirectory);

    verify(cmd).overWrite(content, "Command 4", currentDirectory);
  }

  @Test
  @DisplayName("Determine Cat Behaviour in Case Of < Or << and Errors")
  public void DetermineCatBehaviourInTheBadOverWriteTest() {
    ArrayList<String> commandArgs = new ArrayList<>();
    commandArgs.add("Command 1");
    commandArgs.add("Command 2");
    commandArgs.add(">");

    Command cmd = new Command("cat", commandArgs);

    ArrayList<String> content = new ArrayList<>();
    content.add("Line 1");
    content.add("Line 2");
    content.add("Line 3");

    Assertions.assertThrows(Exception.class, () -> {
      cmd.determineBehaviourOfCat(2, content, currentDirectory);
    });
  }
}
