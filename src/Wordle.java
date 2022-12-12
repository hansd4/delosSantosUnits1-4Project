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
   https://stackoverflow.com/questions/4404084/check-if-a-value-exists-in-arraylist
   https://www.educative.io/answers/how-to-create-a-dictionary-in-java
   https://www.geeksforgeeks.org/list-clear-method-in-java-with-examples/
 */

public class Wordle {
    private final String ALPHABET = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private final List<String> WORDS;
    private Player currentPlayer;
    private List<Player> playerList;
    private boolean on;
    private boolean loggedIn;
    private DecimalFormat percentFormat;
    private Scanner scan;
    private String word;
    private String guess;
    private Hashtable<String, String> charStats;
    private boolean validGuess;
    private int guessNum;
    private boolean win;

    public Wordle() throws Exception {
        WORDS = new ArrayList<>();
        File file = new File(System.getProperty("user.dir") + File.separator + "words.txt");
        Scanner scanFile = new Scanner(file);

        while (scanFile.hasNextLine()) {
            WORDS.add(scanFile.nextLine().toUpperCase());
        }

        scanFile.close();

        playerList = new ArrayList<>();
        on = true;
        loggedIn = true;
        percentFormat = new DecimalFormat("##.##");
        scan = new Scanner(System.in);

        word = "";
        guess = "";
        charStats = new Hashtable<>();
        validGuess = false;
        guessNum = 0;
        win = false;
    }

    public void start() {
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
            if (on) {
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
                    if (playerName.equals("")) {
                        playerList.add(new Player());
                    } else {
                        playerList.add(new Player(playerName));
                    }
                    currentPlayer = playerList.get(playerList.size() - 1);
                    System.out.println("Welcome, " + currentPlayer.getName());
                }

                loggedIn = true;
                while (loggedIn) {
                    int choice = 0;
                    System.out.println();
                    System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Main Menu" + ConsoleColors.RESET);
                    while (choice < 1 || choice > 5) {
                        System.out.println();
                        System.out.println("1. Play");
                        System.out.println("2. Help");
                        System.out.println("3. Stats");
                        System.out.println("4. Settings");
                        System.out.println("5. Log Out");
                        choice = scan.nextInt();
                        scan.nextLine();
                        if (choice < 1 || choice > 5) {
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
    }

    private void play() {
        newWord();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Wordle" + ConsoleColors.RESET);
        // debug
        System.out.println("Word: " + word);
        while (guessNum <= 6 && !win) {
            while (!validGuess) {
                System.out.println(keyboard());
                guess = scan.nextLine().toUpperCase();
                validGuess = checkGuess();
            }
            validGuess = false;
            System.out.println("\r" + colorWord());
            guessNum++;
            if (word.equals(guess)) {
                win = true;
                System.out.println("Spectacular");
                currentPlayer.endRound(guessNum);
            } else if (guessNum > 6) {
                System.out.println(word);
                currentPlayer.endRound();
            }
        }
        System.out.println();
        stats();
        charStats.clear();
        validGuess = false;
        guessNum = 0;
        win = false;
    }

    private void tutorial() {
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
    }

    private void stats() {
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Statistics" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + currentPlayer.getRoundsPlayed() + ConsoleColors.RESET + " Played");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + percentFormat.format(((double) currentPlayer.getWins() / currentPlayer.getRoundsPlayed()) * 100) + ConsoleColors.RESET + " Win %");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + currentPlayer.getStreak() + ConsoleColors.RESET + " Current Streak");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + currentPlayer.getMaxStreak() + ConsoleColors.RESET + " Max Streak");
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "GUESS DISTRIBUTION" + ConsoleColors.RESET);
        List<Integer> guessDist = currentPlayer.getGuessDist();
        for (int i = 1; i <= 6; i++) {
            System.out.print(ConsoleColors.WHITE_BOLD_BRIGHT + i + ConsoleColors.RESET + " - " + guessDist.get(i) + " ");
            for (int j = 0; j < guessDist.get(i); j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    private void settings() {
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Settings" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Hard Mode" + ConsoleColors.RESET);
        System.out.println("Any revealed hints must be used in subsequent guesses");
        System.out.println(currentPlayer.isHardMode() ? "Enabled." : "Disabled.");

        String choice = "";
        while (!choice.equals("y") && !choice.equals("n")) {
            System.out.print((currentPlayer.isHardMode() ? "Disable?" : "Enable?") + " (y/n): ");
            choice = scan.nextLine();
            if (!choice.equals("y") && !choice.equals("n")) {
                System.out.println("Invalid choice, try again: ");
            }
        }

        if (choice.equals("y")) {
            currentPlayer.toggleHardMode();
            System.out.println("Hard mode is now " + (currentPlayer.isHardMode() ? "enabled." : "disabled."));
        } else {
            System.out.println("Quitting to Main Menu...");
        }
    }

    private void logOut() {
        System.out.println("Logged out.");
        loggedIn = false;
        currentPlayer = null;
    }

    private void newWord() {
        word = WORDS.get((int) (Math.random() * WORDS.size()));
    }

    private String colorWord() {
        String currentChar;
        String coloredWord = "";
        String w = word;
        for (int i = 0; i < guess.length(); i++) {
            currentChar = guess.substring(i, i+1);
            if (currentChar.equals(w.substring(i, i+1))) {
                coloredWord += ConsoleColors.GREEN_BACKGROUND + currentChar + ConsoleColors.RESET;
                w = removeChar(w, i);
                charStats.put(currentChar, ConsoleColors.GREEN_BACKGROUND + i);
            } else if (w.contains(currentChar)) {
                coloredWord += ConsoleColors.YELLOW_BACKGROUND + currentChar + ConsoleColors.RESET;
                w = removeChar(w, w.indexOf(currentChar));
                charStats.put(currentChar, ConsoleColors.YELLOW_BACKGROUND);
            } else {
                coloredWord += ConsoleColors.BLACK_BACKGROUND_BRIGHT + currentChar + ConsoleColors.RESET;
                charStats.put(currentChar, ConsoleColors.BLACK_BACKGROUND_BRIGHT);
            }
        }
        return coloredWord;
    }

    private String removeChar(String w, int c) {
        return w.substring(0, c) + " " + w.substring(c + 1);
    }

    private boolean checkGuess() {
        if (WORDS.contains(guess)) {
            if (currentPlayer.isHardMode()) {
                for (int i = 0; i < ALPHABET.length(); i++) {
                    String currentLetter = ALPHABET.substring(i, i + 1);
                    String state = charStats.get(currentLetter);
                    if (state != null) {
                        if (state.substring(0, 5).equals(ConsoleColors.GREEN_BACKGROUND)) {
                            int correctPos = Integer.parseInt(state.substring(5));
                            if (!guess.substring(correctPos, correctPos + 1).equals(currentLetter)) {
                                System.out.println(toOrdinal(correctPos + 1) + " letter must be " + currentLetter);
                                return false;
                            }
                        } else if (state.equals(ConsoleColors.YELLOW_BACKGROUND)) {
                            if (guess.indexOf(currentLetter) == -1) {
                                System.out.println("Guess must contain " + currentLetter);
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        System.out.println("Not in word list");
        return false;
    }

    private String toOrdinal(int i) {
        String ordinalSuffix = "th";
        if (i / 10 % 10 != 1) {
            ordinalSuffix = switch (i % 10) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }
        return i + ordinalSuffix;
    }

    private String keyboard() {
        String keyboard = "";
        List<String> lines = new ArrayList<>();
        lines.add(ALPHABET.substring(0, 10));
        lines.add(ALPHABET.substring(10, 19));
        lines.add(ALPHABET.substring(19));
        for (int i = 0; i < lines.size(); i++) {
            String letters = lines.get(i);
            keyboard += "\n";
            for (int j = 0; j < letters.length(); j++) {
                String letter = letters.substring(j, j + 1);
                keyboard += removeIndexNumber(charStats.get(letter)) + letter + ConsoleColors.RESET + " ";
            }
        }
        return keyboard;
    }

    private String removeIndexNumber(String s) {
        if (s == null) {
            return "";
        } else if (!s.substring(s.length() - 1).equals("m")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
}
