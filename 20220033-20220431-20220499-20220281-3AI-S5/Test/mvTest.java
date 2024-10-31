import org.example.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
public class mvTest {
    @TempDir
    Path dir;

    @Test
    public void test_empty_size() {
        //empty
        ArrayList<String> commandArgs = new ArrayList<>();
        Command cmd = new Command("mv", commandArgs);
        Exception exp = Assertions.assertThrows(Exception.class, () -> {
            cmd.mv(dir.toString());
        });
        String msg = exp.getMessage();
        Assertions.assertEquals("mv: missing file operand\nTry 'mv --help' for more information.", msg);

        //size 1
        commandArgs.add("x");
        try {
            cmd.touch(dir.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        exp = Assertions.assertThrows(Exception.class, () -> {
            cmd.mv(dir.toString());
        });
        msg = exp.getMessage();
        Assertions.assertEquals("mv: missing destination file operand after '" + commandArgs.get(0) + "'", msg);

        //size > 2
        commandArgs.add("b");
        commandArgs.add("c");
        exp = Assertions.assertThrows(Exception.class, () -> {
            cmd.mv(dir.toString());
        });
        msg = exp.getMessage();
        Assertions.assertEquals("mv: target '" + commandArgs.getLast() + "' is not a directory", msg);

    }

    @Test
    public void test_filePath() {
        ArrayList<String> commandArgs = new ArrayList<>();
        Command cmd = new Command("mv", commandArgs);
        commandArgs.add("x");
        try {
            cmd.touch(dir.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            cmd.mv(dir.toString());
        }  catch(Exception e) {
            System.out.println(e.getMessage());
        }
        commandArgs.add("y");
        Path path1 = dir.resolve("x");
        Path path2 = dir.resolve("y");
        Assertions.assertTrue(Files.exists(path1));
        Assertions.assertFalse(Files.exists((path2)));
    }
}
