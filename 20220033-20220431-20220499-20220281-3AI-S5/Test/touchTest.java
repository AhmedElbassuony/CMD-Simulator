import org.example.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class touchTest {
    @TempDir
    Path dir;

    @Test
    public void test_empty() {
        ArrayList<String> commandArgs = new ArrayList<>();
        Command cmd = new Command("touch", commandArgs);
        Exception exp = Assertions.assertThrows(Exception.class, () -> {
            cmd.touch(dir.toString());
        });
        String msg = exp.getMessage();
        Assertions.assertEquals("touch: missing file operand\nTry 'touch --help' for more information.", msg);
    }

    @Test
    public void test_tooLongException() {
        ArrayList<String> commandArgs = new ArrayList<>();
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 300; i++) {
            s.append('a');
        }
        commandArgs.add(s.toString());
        Command cmd = new Command("touch", commandArgs);
        Exception exp = Assertions.assertThrows(Exception.class, () -> {
            cmd.touch(dir.toString());
        });
        String msg = exp.getMessage();
        Assertions.assertEquals("touch: cannot touch '" + s.toString() + " ': File name too long", msg);
    }

    @Test
    public void test_fileHandling() {
        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add("x");
        commandArgs.add("r/x");
        Command cmd = new Command("touch", commandArgs);
        String msg = "";
        try {
            cmd.touch(dir.toString());
        } catch (Exception e) {
            msg = e.getMessage();
        }
        Path path1 = dir.resolve(commandArgs.get(0));
        Path path2 = dir.resolve(commandArgs.get(1));
        Assertions.assertTrue(Files.exists(path1));
        Assertions.assertFalse(Files.exists(path2));
    }
}
