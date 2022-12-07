import java.util.*;

public class Player {
    private String name;
    private boolean hardMode;
    private int roundsPlayed;
    private int wins;
    private int streak;
    private int maxStreak;
    private List<Integer> guessDist;

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

    public String getName() {
        return name;
    }

    public boolean isHardMode() {
        return hardMode;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getStreak() {
        return streak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public List<Integer> getGuessDist() {
        return guessDist;
    }

    public void toggleHardMode() {
        hardMode = !hardMode;
    }

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
