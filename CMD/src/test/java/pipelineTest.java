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
public class pipelineTest {
    @TempDir
    Path dir;

    @Test
    void test_exceptions() {
        ArrayList<String> content = new ArrayList<>();
        ArrayList<String> commands = new ArrayList<>();


        content.add("a");
        content.add("cc");
        content.add("ffgsfsdf");
        content.add("Mahmoud");
        content.add("else");
        content.add("OS");
        content.add("Mahmoud");
        content.add("d");
        content.add("d");
        content.add("d");
        content.add("d");

        // two pipes in row
        commands.add("|");
        commands.add("|");
        commands.add("more");


        Exception exp = Assertions.assertThrows(Exception.class, () -> {
            Command.pipeLine(content, commands);
        });
        String msg = exp.getMessage();
        Assertions.assertEquals("| was unexpected at this time.", msg);

        // grep with no part
        ArrayList<String> commands1 = new ArrayList<>();
        commands1.add("|");
        commands1.add("grep");
        commands1.add("|");
        exp = Assertions.assertThrows(Exception.class, () -> {
            Command.pipeLine(content, commands1);
        });
        msg = exp.getMessage();
        Assertions.assertEquals("The syntax of the command is incorrect.", msg);

        // commands are incorrect
        ArrayList<String> commands2 = new ArrayList<>();
        commands2.add("|");
        commands2.add("x");
        commands2.add("|");
        commands2.add("more");
        exp = Assertions.assertThrows(Exception.class, () -> {
            Command.pipeLine(content, commands2);
        });
        msg = exp.getMessage();
        Assertions.assertEquals("The syntax of the command is incorrect.", msg);

        // last pipeline
        ArrayList<String> commands3 = new ArrayList<>();
        commands3.add("|");
        commands3.add("x");
        commands3.add("|");
        commands3.add("more");
        exp = Assertions.assertThrows(Exception.class, () -> {
            Command.pipeLine(content, commands3);
        });
        msg = exp.getMessage();
        Assertions.assertEquals("The syntax of the command is incorrect.", msg);
    }

    @Test
    void Test_sort() {
        ArrayList<String> content = new ArrayList<>();

        content.add("a");
        content.add("cc");
        content.add("ffgsfsdf");
        content.add("Mahmoud");
        content.add("else");
        content.add("OS");
        content.add("Mahmoud");
        content.add("d");
        content.add("d");
        content.add("d");
        content.add("d");

        content = Command.sort(content);
        for(int i = 1; i < content.size(); i++) {
            Assertions.assertTrue(content.get(i).compareTo(content.get(i - 1)) >= 0);
        }

    }

    @Test
    void Test_unique() {
        ArrayList<String> content = new ArrayList<>();

        content.add("a");
        content.add("cc");
        content.add("ffgsfsdf");
        content.add("Mahmoud");
        content.add("else");
        content.add("OS");
        content.add("Mahmoud");
        content.add("d");
        content.add("d");
        content.add("d");
        content.add("d");

        content = Command.unique(content);
        for(int i = 0; i < content.size() - 1; i++) {
            for(int j = i + 1; j < content.size(); j++) {
                Assertions.assertNotEquals(content.get(i), content.get(j));
            }
        }
    }

    @Test
    void Test_grep() {
        ArrayList<String> content = new ArrayList<>();
        content.add("a");
        content.add("cc");
        content.add("ffgsfsdf");
        content.add("Mahmoud");
        content.add("else");
        content.add("OS");
        content.add("Mahmoud");
        content.add("d");
        content.add("d");
        content.add("d");
        content.add("d");
        content = Command.grep(content, "s");
        for(int i = 0; i < content.size(); i++) {
            Assertions.assertTrue(content.get(i).contains("s"));
        }
    }

}
