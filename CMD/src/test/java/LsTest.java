import org.example.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class LsTest {
  @TempDir
  Path testDirectory;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Test
  public void testBasicLs() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>());
    setUpTestFiles();

    cmd.ls(testDirectory);
    String output = outContent.toString().trim();

    Assertions.assertTrue(output.contains("file1.txt"));
    Assertions.assertTrue(output.contains("file2.txt"));
    Assertions.assertFalse(output.contains(".hiddenFile"));

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithA() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList("-a")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    String output = outContent.toString().trim();

    Assertions.assertTrue(output.contains("file1.txt"));
    Assertions.assertTrue(output.contains("file2.txt"));
    Assertions.assertTrue(output.contains(".hiddenFile"));

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithR() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList("-r")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    String output = outContent.toString().trim();
    String[] lines = output.split("\n");

    Assertions.assertTrue(lines[0].compareTo(lines[1]) > 0); // Check reverse order

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithInvalidArgument() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList(">>-a m.txt")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    String output = outContent.toString();

    Assertions.assertTrue(output.contains("Unexpected argument:"));

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithRedirection() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList(">", "redirect.txt")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    File file = new File(testDirectory.toFile(), "redirect.txt");

    Assertions.assertTrue(file.exists());

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithAppend() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList(">>", "append.txt")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    File file = new File(testDirectory.toFile(), "append.txt");

    Assertions.assertTrue(file.exists());

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithConflictingRedirectionArgs() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList(">", "file1.txt", ">>", "file2.txt")));
    setUpTestFiles();

    cmd.ls(testDirectory);

    Assertions.assertTrue(outContent.toString().contains("Conflicting arguments"));

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithPipeline() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList("|", "sort")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    String output = outContent.toString().trim();
    String[] lines = output.split("\n");

    Assertions.assertTrue(lines.length >= 2); // Ensure there are at least two lines (files) in the output
    Assertions.assertTrue(lines[0].compareTo(lines[1]) <= 0); // Check if the output is sorted

    System.setOut(originalOut);
  }
  @Test
  public void testLsWithPipeline2() throws Exception {
    System.setOut(new PrintStream(outContent));
    Command cmd = new Command("ls", new ArrayList<>(Arrays.asList("-a","|", "grep", "file")));
    setUpTestFiles();

    cmd.ls(testDirectory);
    String output = outContent.toString().trim();

    // Check that the output contains the expected files filtered by "grep file"
    Assertions.assertTrue(output.contains("file1.txt"));
    Assertions.assertTrue(output.contains("file2.txt"));
    Assertions.assertFalse(output.contains(".hiddenFile")); // This should not be included

    System.setOut(originalOut);
  }

  private void setUpTestFiles() throws Exception {
    new File(testDirectory.toFile(), "file1.txt").createNewFile();
    new File(testDirectory.toFile(), "file2.txt").createNewFile();
    new File(testDirectory.toFile(), ".hiddenFile").createNewFile();
  }
}
