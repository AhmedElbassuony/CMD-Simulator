import org.example.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CommandTest {
    @TempDir
    Path tempDir;

    @Test
    public void testRemoveDirectorySucsses() {
        Path testDir = tempDir.resolve("testDir").normalize();
        try {
            Files.createDirectory(testDir);
        } catch (IOException e) {
            Assertions.fail("Failed to set up for the test");
        }
        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("testDir");
        Command cmd = new Command("rmdir",commandArgs);
        cmd.removeDirectory(tempDir);
        Assertions.assertFalse(Files.exists(testDir));
    }
    @Test
    public void testRemoveDirectoryFails() {
        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("testDir");
        Command cmd = new Command("rmdir",commandArgs);
        Assertions.assertThrows(Exception.class,()->cmd.removeDirectory(tempDir));
    }

    @Test
    public void testMakeDirectory() {
        Path testDir = tempDir.resolve("testDir");

        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("testDir");
        Command cmd = new Command("mkdir",commandArgs);
        cmd.makeDirectory(tempDir);
        Assertions.assertTrue(Files.exists(testDir));
    }

    @Test
    public void testMakeDirectoryFails() {
        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("testDir.../......./lsjdnfksjdf");
        Command cmd = new Command("mkdir",commandArgs);
        Assertions.assertThrows(Exception.class,()->cmd.makeDirectory(tempDir));
    }

    @Test
    public void testRemoveFile() {
        Path testFile = tempDir.resolve("testFile.txt");
        try {
            Files.createFile(testFile);
        } catch (IOException e) {
            Assertions.fail("Failed to create the file");
        }
        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("testFile.txt");
        Command cmd = new Command("rm",commandArgs);
        cmd.removeFile(tempDir);
        Assertions.assertFalse(Files.exists(testFile));
    }

    @Test
    public void testRemoveFileFails() {
        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("testFile.txt");
        Command cmd = new Command("rm",commandArgs);
        Assertions.assertThrows(Exception.class,()->cmd.removeFile(tempDir));
    }


}
