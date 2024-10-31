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
    public void testRemoveDirectory() {
        Path testDir = tempDir.resolve("testDir");

        
        try {
            Files.createDirectory(testDir);
        } catch (IOException e) {
            Assertions.fail("Failed to set up for the test");
        }

        Command cmd = new Command(commandArgs, currentDirectory);
        cmd.removeDirectory(new ArrayList<>(List.of("testDir")), tempDir);
        Assertions.assertFalse(Files.exists(testDir), "The directory should be removed");
    }

    @Test
    public void testMakeDirectory() {
        Path testDir = tempDir.resolve("testDir");

        Command cmd = new Command(commandArgs, currentDirectory);
        cmd.makeDirectory(new ArrayList<>(List.of("testDir")), tempDir);
        Assertions.assertTrue(Files.exists(testDir), "The directory should be created");
    }

    @Test
    public void testRemoveFile() {
        Path testFile = tempDir.resolve("testFile.txt");

     
        try {
            Files.createFile(testFile);
        } catch (IOException e) {
            Assertions.fail("Failed to create the file");
        }

        Command cmd = new Command(commandArgs, currentDirectory);
        cmd.removeFile(new ArrayList<>(List.of("testFile.txt")), tempDir);
        Assertions.assertFalse(Files.exists(testFile), "The file should be removed");
    }
}
