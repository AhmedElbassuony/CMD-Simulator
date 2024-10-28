import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Command {
   private String command;
   private ArrayList<String> commandArgs;
   Command(String command, ArrayList<String> commandArgs) {
      this.command = command;
      this.commandArgs = commandArgs;
   }
   public void mv(String currentDir) {
      if(commandArgs.size() == 1) {
          System.out.println("mv: missing destination file operand after '" + commandArgs.get(0) + "'");
          return;
      }
      if(commandArgs.size() > 2) {
          System.out.println("mv: target '" + commandArgs.getLast() + "' is not a directory");
          return;
      }
      String src = commandArgs.get(0);
      String dist = commandArgs.get(1);
      if (!Paths.get(src).isAbsolute()) {
          src = currentDir + "\\" + src;
      }
      if (!Paths.get(dist).isAbsolute()) {
          dist = currentDir + "\\" + dist;
      }
      if(!Files.exists(Paths.get(src))) {
          System.out.println("mv: cannot stat '" + commandArgs.get(0) + "': No such file or directory");
          return;
      }

      try {
          Path srcP = Paths.get(src);
          Path distP = Paths.get(dist);
          Files.move(srcP, distP, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
          System.out.println("mv: cannot move '" + commandArgs.get(0) + "'to '" + commandArgs.get(1) + "': No such file or directory");
      }
   }
   public void touch(String currentDir) {
      for(String f:commandArgs) {
            String file = f;
            if (!Paths.get(f).isAbsolute()) {
                file = currentDir + "\\" + f;
            }
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < file.length(); i++) {
                s.append(file.charAt(i));
                if(file.charAt(i) == '\\') s = new StringBuilder();
            }
            if(s.toString().length() > 255) {
                System.out.println("touch: cannot touch '" + s.toString() + " ': File name too long");
                continue;
            }
            Path p = Paths.get(file);
          try {
              if(Files.exists(p)) Files.delete(p);
              Files.createFile(p);
          } catch(IOException e) {
              System.out.println("touch: cannot touch '" + p + "': No such file or directory");
          }  
      }
   }

   public static void pipeLine(ArrayList<String> content, ArrayList<String> commands) {
      boolean more = false;
      boolean less = false;
      for(int i = 0; i < commands.size(); i++) {
         if(commands.get(i).equals("|")) {
            ++i;
         }
         if(i ==commands.size()) {
            System.out.println("The syntax of the command is incorrect.");
            return;
         }
         switch(commands.get(i)) {
            case "unique" -> {
               content = unique(content);
            }
            case "sort" -> {
               content = sort(content);
            }
            case "more" -> {
               more = true;
            }
            case "less" -> {
               less = true;
            }
            case "grep" -> {
               ++i;
               if(commands.get(i).equals("|")) {
                  System.out.println("The syntax of the command is incorrect.");
                  return;
               }
               content = grep(content, commands.get(i));
            }
            default -> {
               System.out.println("The syntax of the command is incorrect.");
               return;
            }

         }
      }
      if(less && more) {
         System.out.println("The syntax of the command is incorrect.");
      } else if (more) {
         for(int i = 0; i < Math.min(5, content.size()); i++) {
            System.out.println(content.get(i));
         }
         int i = 5;
         Scanner input = new Scanner(System.in);
         while(i < content.size()) {
            System.out.println("--Press m for more--");
            char c;
            c = input.next().charAt(0);
            if(c == 'm') {
               ++i;
               System.out.println(content.get(i));
            } else {
               System.out.println("--You must press m--");
            }
         }
      } else if(less) {
         //---> i do not know yet what I should do
      } else {
         for(int i = 0; i < content.size(); i++) {
            System.out.println(content.get(i));
         }
      }
   }
   public static ArrayList<String> unique(ArrayList<String> content) {
      ArrayList<String> tmp = new ArrayList<>();
      Collections.sort(content);
      if(!content.isEmpty()) {
         tmp.add(content.get(0));
         for(int i = 1; i < content.size(); i++) {
            if(i > 0 && content.get(i) != content.get(i - 1)) {
               tmp.add(content.get(i));
            }
         }
      }
      return tmp;
   }
   public static ArrayList<String> sort(ArrayList<String> content) {
      Collections.sort(content);
      return content;
   }
   public static ArrayList<String> grep(ArrayList<String> content, String part) {
      ArrayList<String> tmp = new ArrayList<>();
      for(int i = 0; i < content.size(); i++) {
         if(content.get(i).contains(part)) {
            tmp.add(content.get(i));
         }
      }
      return tmp;
   }




}