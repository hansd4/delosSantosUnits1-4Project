import java.io.Console;
import java.io.File;
import java.util.*;

/* List of 5-Letter Words: https://www-cs-faculty.stanford.edu/~knuth/sgb.html
   Retrieving the current directory: https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
   Reading a text file: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
   Adding values to a list: https://www.digitalocean.com/community/tutorials/java-list-add-addall-methods
   Go back to the beginning of the line (\r): https://stackoverflow.com/questions/7522022/how-to-delete-stuff-printed-to-console-by-system-out-println
 */

public class Wordle {
    private List<String> words = new ArrayList<>();

    public Wordle() throws Exception {
        File file = new File(System.getProperty("user.dir") + File.separator + "words.txt");
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) {
            words.add(scan.nextLine());
        }

        scan.close();
    }

    private String newWord() {
        return words.get((int) (Math.random() * words.size()));
    }

    public void start() {
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "How to Play" + ConsoleColors.RESET);
        System.out.println();
        System.out.println("Guess the Wordle in 6 tries\n-Each guess must be a valid 5-letter word\n-The color of the tiles will change to show how close your guess was to the word.");
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD + "Examples" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.GREEN_BACKGROUND + "W" + ConsoleColors.RESET + "EARY");
        System.out.println(ConsoleColors.WHITE_BOLD + "W" + ConsoleColors.RESET + " is in the word and in the correct spot.");
        System.out.println();
        System.out.println("P" + ConsoleColors.YELLOW_BACKGROUND + "I" + ConsoleColors.RESET + "LLS");
        System.out.println(ConsoleColors.WHITE_BOLD + "I" + ConsoleColors.RESET + " is in the word but in the wrong spot.");
        System.out.println();
        System.out.println("VAG" + ConsoleColors.BLACK_BACKGROUND_BRIGHT + "U" + ConsoleColors.RESET + "E");
        System.out.println(ConsoleColors.WHITE_BOLD + "U" + ConsoleColors.RESET + " is not in the word in any spot.");
    }
}
