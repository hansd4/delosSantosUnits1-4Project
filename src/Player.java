import java.util.*;

/**
 * This class represents a Player of the Wordle game
 *
 * @author Hans de los Santos with contributions from David Merdjan and Mohammed Nihal
 */
public class Player {
    /** The name of the player */
    private String name;

    /** Whether or not the player is on hardMode */
    private boolean hardMode;

    /** The number of rounds of Wordle the player has played */
    private int roundsPlayed;

    /** The number of rounds of Wordle the player has won */
    private int wins;

    /** The number of rounds of Wordle the player has won since last losing */
    private int streak;

    /** The most ever number of rounds of Wordle the player has went without losing */
    private int maxStreak;

    /** The numbers of times a player has won at a certain guess number for each possible number of guesses, 1 to 6 */
    private List<Integer> guessDist;

    /**
     * Instantiates a Player object.
     *
     * @param name The name of the player
     */
    public Player(String name) {
        this.name = name;
        roundsPlayed = 0;
        wins = 0;
        streak = 0;
        maxStreak = 0;
        guessDist = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            guessDist.add(0);
        }
    }

    /**
     * Returns the name of the player
     *
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether or not the player has hard mode enabled
     *
     * @return Whether or not the player enabled hard mode
     */
    public boolean isHardMode() {
        return hardMode;
    }

    /**
     * Returns the number of rounds the player has played
     *
     * @return The number of rounds the player has played
     */
    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    /**
     * Returns how many rounds the player has won
     *
     * @return The number of wins the player has
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the player's current streak; how many rounds the player's gone without losing
     *
     * @return The player's win streak
     */
    public int getStreak() {
        return streak;
    }

    /**
     * Returns the player's all-time greatest streak; the most rounds the player has ever gone without losing
     *
     * @return The player's record win streak
     */
    public int getMaxStreak() {
        return maxStreak;
    }

    /**
     * Returns the number of times a player has won at a certain stage of the game. Each index corresponds to a number of guesses the player may have taken to win, and at each index is their wins at that specific guess.
     *
     * @return A list of integers representing the number of times a player has won at a given guess number
     */
    public List<Integer> getGuessDist() {
        return guessDist;
    }

    /**
     * Enables/disables hard mode for the player. An on/off switch.
     */
    public void toggleHardMode() {
        hardMode = !hardMode;
    }

    /**
     * Handles statistics at the end of a round of Wordle. Saves to the above instance variables the new number of rounds played, the new number of wins the player has, the player's new win streak, the player's new maximum win streak, and the player's new guess distribution.
     *
     * @param win Whether or not the player won this round.
     * @param guess The number of guesses it took to end the round.
     */
    public void endRound(boolean win, int guess) {
        roundsPlayed++;
        if (win) {
            wins++;
            streak++;
            if (streak > maxStreak) {
                maxStreak = streak;
            }
            guessDist.set(guess, guessDist.get(guess) + 1);
        } else {
            streak = 0;
        }
    }
}
