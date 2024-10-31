import org.example.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CommandTest {
 @Test
public void testRemoveDirectory() {

       Path testDir = Paths.get(System.getProperty("user.dir")).resolve("testDir");


       try {
           Files.deleteIfExists(testDir);
           Files.createDirectory(testDir);
       } catch (IOException e) {
           fail("Failed to set up for the test");
       }


      Command.removeDirectory(new ArrayList<>(List.of("testDir")), Paths.get(System.getProperty("user.dir")));


       assertTrue(Files.exists(testDir), "The directory should get removed");
   }
}
 @Test
public void testMakeDirectory() {

       Path testDir = Paths.get(System.getProperty("user.dir")).resolve("testDir");


       try {
           Files.deleteIfExists(testDir);
       } catch (IOException e) {
           fail("Failed to clean up before the test");
       }


       Command.makeDirectory(new ArrayList<>(List.of("testDir")), Paths.get(System.getProperty("user.dir")));


       assertTrue(Files.exists(testDir), "The directory should be created");


}
 @Test
public void testRemoveFile() {

       Path testFile = Paths.get(System.getProperty("user.dir")).resolve("testFile.txt");


       try {
           Files.deleteIfExists(testFile);
       } catch (IOException e) {
           fail("Failed to clean  before doing the test");
       }


       try {
           Files.createFile(testFile);
       } catch (IOException e) {
           fail("Failed to create the file");
       }


      Command.removeFile(new ArrayList<>(List.of("testFile.txt")), Paths.get(System.getProperty("user.dir")));


       assertFalse(Files.exists(testFile), "The file should be removed");
   }
