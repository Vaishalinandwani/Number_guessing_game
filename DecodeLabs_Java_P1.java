import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/*
  DecodeLabs  - Project 1: Number Guessing Game
 
  Goal: Generate a random number (1-100) and  user guess it,
  giving "too high" / "too low" feedback until the correct number is found.
 
  Features included:
   - Limited number of attempts per round
   - Multiple rounds (play again loop using do-while)
   - Final score tracking across rounds
   - Defensive input validation (try-catch for InputMismatchException)
 */
public class DecodeLabs_Java_P1 {

    // Constants controlling game behaviour
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 7; // ~log2(100) is about 7, so this is fair

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int score = 0;
        int roundsPlayed = 0;
        boolean playAgain = true;

        System.out.println("=================================================");
        System.out.println("  DecodeLabs Java Project 1: Number Guessing Game");
        System.out.println("=================================================");

        //  do-while loop so the game runs at least once
        // and lets the player choose to play multiple rounds.
        do {
            roundsPlayed++;
            boolean wonRound = playRound(scanner, random);

            if (wonRound) {
                score++;
            }

            System.out.println("\nCurrent Score: " + score + " / " + roundsPlayed);
            playAgain = askPlayAgain(scanner);

        } while (playAgain);

        // Final summary
        System.out.println("\n================ GAME OVER ================");
        System.out.println("Rounds played : " + roundsPlayed);
        System.out.println("Rounds won    : " + score);
        System.out.println("Final Score   : " + score + " / " + roundsPlayed);
        System.out.println("Thanks for playing the DecodeLabs Number Game!");

        scanner.close();
    }

    /*
      Plays a single round of the guessing game.
      Returns true if the player guessed correctly within the attempt limit.
     */
    private static boolean playRound(Scanner scanner, Random random) {
        // Stochastic Generation: random.nextInt(100) gives 0-99, so +1 shifts
        // it to the human-friendly 1-100 range (the "Zero-Index Shift").
        int target = random.nextInt(MAX_RANGE) + MIN_RANGE;

        int attemptsUsed = 0;
        boolean win = false;

        System.out.println("\nI'm thinking of a number between " + MIN_RANGE
                + " and " + MAX_RANGE + ". You have " + MAX_ATTEMPTS + " attempts.");

        // Feedback Loop: keep looping until the player wins or runs out of attempts
        while (attemptsUsed < MAX_ATTEMPTS && !win) {
            int guess = getValidGuess(scanner, attemptsUsed, MAX_ATTEMPTS);
            attemptsUsed++;

            if (guess == target) {
                win = true;
                System.out.println("Correct! You guessed it in " + attemptsUsed + " attempt(s).");
            } else if (guess > target) {
                System.out.println("Too High! Attempts remaining: " + (MAX_ATTEMPTS - attemptsUsed));
            } else {
                System.out.println("Too Low! Attempts remaining: " + (MAX_ATTEMPTS - attemptsUsed));
            }
        }

        if (!win) {
            System.out.println("Out of attempts! The number was: " + target);
        }

        return win;
    }

    /*
      Stream Capture with defensive input validation.
      Keeps asking until the user provides a valid integer guess,
      handling InputMismatchException so the program never crashes.
     */
    private static int getValidGuess(Scanner scanner, int attemptsUsed, int maxAttempts) {
        while (true) {
            System.out.print("Attempt " + (attemptsUsed + 1) + "/" + maxAttempts + " - Enter your guess: ");
            try {
                int guess = scanner.nextInt();
                scanner.nextLine(); // flush the newline left behind by nextInt() (the "Scanner Trap")
                return guess;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a whole number.");
                scanner.nextLine(); // clear the bad token so we don't loop forever
            }
        }
    }

    /*
      Asks the player if they want to play another round.
      Validates the input so only 'y' or 'n' (any case) is accepted.
     */
    private static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            System.out.print("Play again? (Y/N): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y") || response.equals("yes")) {
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter Y or N.");
            }
        }
    }
}
