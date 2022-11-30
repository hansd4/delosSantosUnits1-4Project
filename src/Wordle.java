import java.io.File;
import java.util.*;
import java.text.DecimalFormat;

/* List of 5-Letter Words: https://www-cs-faculty.stanford.edu/~knuth/sgb.html
   Retrieving the current directory: https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
   Reading a text file: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
   Adding values to a list: https://www.digitalocean.com/community/tutorials/java-list-add-addall-methods
   Go back to the beginning of the line (\r): https://stackoverflow.com/questions/7522022/how-to-delete-stuff-printed-to-console-by-system-out-println
   https://www.geeksforgeeks.org/how-to-replace-a-element-in-java-arraylist/
   https://www.w3schools.com/java/java_arraylist.asp
   https://www.vojtechruzicka.com/java-enhanced-switch/
   https://www2.cs.arizona.edu/classes/cs210/fall17/lectures/decimal_format.pdf
 */

public class Wordle {
    private List<String> words = new ArrayList<>();
    private Player currentPlayer = null;
    private List<Player> playerList = new ArrayList<>();
    private boolean on = true;
    private boolean loggedIn = true;
    private boolean hardMode = false;
    private DecimalFormat percentFormat = new DecimalFormat("##.##");

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
        Scanner scan = new Scanner(System.in);

        while (on) {
            int logInOrQuit = 0;
            while (logInOrQuit != 1 && logInOrQuit != 2) {
                System.out.println();
                System.out.println("1. Play Game");
                System.out.println("2. Quit Game");
                logInOrQuit = scan.nextInt();
                scan.nextLine();
                if (logInOrQuit != 1 && logInOrQuit != 2) {
                    System.out.println("Invalid choice, try again: ");
                }
            }

            on = logInOrQuit != 2;
            if (logInOrQuit == 1) {
                System.out.println();
                System.out.println("~~~~~~~~~~Wordle~~~~~~~~~~");
                System.out.print("Enter your name to continue: ");
                String playerName = scan.nextLine();
                for (int i = 0; i < playerList.size(); i++) {
                    if (playerName.equals(playerList.get(i).getName())) {
                        currentPlayer = playerList.get(i);
                        System.out.println("Welcome back, " + currentPlayer.getName());
                    }
                }
                if (currentPlayer == null) {
                    playerList.add(new Player(playerName));
                    currentPlayer = playerList.get(playerList.size() - 1);
                    System.out.println("Welcome, " + currentPlayer.getName());
                }

                while (loggedIn) {
                    int choice = 0;
                    System.out.println();
                    System.out.println("Main Menu");
                    while (choice < 1 && choice > 5) {
                        System.out.println();
                        System.out.println("1. Play");
                        System.out.println("2. Help");
                        System.out.println("3. Stats");
                        System.out.println("4. Settings");
                        System.out.println("5. Log Out");
                        choice = scan.nextInt();
                        scan.nextLine();
                        if (choice < 1 && choice > 5) {
                            System.out.print("Invalid choice, try again: ");
                        }
                    }
                    System.out.println();
                    switch (choice) {
                        case 1 -> play();
                        case 2 -> tutorial();
                        case 3 -> stats();
                        case 4 -> settings();
                        case 5 -> logOut();
                    }
                }
            }
        }
        scan.close();
    }

    private void tutorial() {
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "How to Play" + ConsoleColors.RESET);
        System.out.println();
        System.out.println("Guess the Wordle in 6 tries\n-Each guess must be a valid 5-letter word\n-The color of the tiles will change to show how close your guess was to the word.");
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Examples" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.GREEN_BACKGROUND + "W" + ConsoleColors.RESET + "EARY");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "W" + ConsoleColors.RESET + " is in the word and in the correct spot.");
        System.out.println();
        System.out.println("P" + ConsoleColors.YELLOW_BACKGROUND + "I" + ConsoleColors.RESET + "LLS");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "I" + ConsoleColors.RESET + " is in the word but in the wrong spot.");
        System.out.println();
        System.out.println("VAG" + ConsoleColors.BLACK_BACKGROUND_BRIGHT + "U" + ConsoleColors.RESET + "E");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "U" + ConsoleColors.RESET + " is not in the word in any spot.");
        System.out.println();
    }

    private void stats() {
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Statistics" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + currentPlayer.getRoundsPlayed() + ConsoleColors.RESET + " Played");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + percentFormat.format((double) currentPlayer.getWins() / currentPlayer.getRoundsPlayed()) + ConsoleColors.RESET + " Win %");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + currentPlayer.getStreak() + ConsoleColors.RESET + " Current Streak");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + currentPlayer.getMaxStreak() + ConsoleColors.RESET + " Max Streak");
        System.out.println();
        // implement guess distribution display
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "GUESS DISTRIBUTION" + ConsoleColors.RESET);
        List<Integer> guessDist = currentPlayer.getGuessDist();
        for (int i = 1; i <= 6; i++) {
            System.out.print(ConsoleColors.WHITE_BOLD_BRIGHT + i + ConsoleColors.RESET + " - " + guessDist.get(i));
            for (int j = 0; j < guessDist.get(i); j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    private void settings() {
        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Settings" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Hard Mode" + ConsoleColors.RESET);
        System.out.println("Any revealed hints must be used in subsequent guesses");
        if (hardMode) {
            System.out.println("Enable? (y/n)");
            String choice = scan.nextLine();

        } else {

        }
        scan.close();
    }

    private void logOut() {
        System.out.println();
        System.out.println("Logged out.");
        loggedIn = false;
        currentPlayer = null;
    }
}
