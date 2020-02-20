/**
 * @author Jacob Ortiz
 * @title Hangman.java
 * @date started 02/12/20 , finished & tweaked 02/13/20
 * @desciption This program plays the game Hangman with hint feature
 *
 */

import java.util.*;
import java.util.regex.Pattern;

public class Hangman {

    private static String correctWord;
    private static ArrayList<Character> wordPrompt = new ArrayList<>();
    private static ArrayList<Character> alreadyGuessed = new ArrayList<>();
    private static int guessesLeft = 4;

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        String option;
        String inputGuess;

        boolean endGame = false;

        System.out.println("--------- Welcome to Hangman ---------");
        System.out.print("\nEnter a word:");
        correctWord = keyboard.nextLine().toUpperCase();

        // function so user can only input letters, otherwise
        // game would be unplayable if user entered special characters
        while(correctWord.matches(".*\\d.*")) {
            System.out.println("Please input a word with no numbers");
            correctWord = keyboard.nextLine().toUpperCase();
        }

        createPrompt(correctWord);

        while(guessesLeft > 0) {
            endGame = checkEndGame();

            if(endGame) {
                break;
            }

            System.out.print("\nSo far, the word is: ");
            wordPrompt.forEach(character -> System.out.print(character + " "));
            System.out.printf("\nYou have %d incorrect guesses left", guessesLeft);
            System.out.print("\nEnter either 1 for guessing or 2 for hint: ");

            option = keyboard.nextLine();

            while(true) {

                if(option.equals("1")) {
                    System.out.print("Enter your guess: ");
                    inputGuess = keyboard.nextLine().toUpperCase();

                    // makes user input correct input
                    while(!Character.isLetter(inputGuess.charAt(0)) || inputGuess.length() > 1) {
                        System.out.println("Incorrect input.");
                        System.out.print("Enter your guess: ");
                        inputGuess = keyboard.nextLine().toUpperCase();
                    }

                    if(checkIfAlreadyGuessed(inputGuess.charAt(0))) {
                        break;
                    }

                    alreadyGuessed.add(inputGuess.charAt(0));
                    updatePrompt(inputGuess.charAt(0), false);

                    break;

                } else if(option.equals("2")) {
                    Character hint = getHint();
                    updatePrompt(hint, true);

                    System.out.println("OK! The hint is " + hint);
                    System.out.printf("But since you used the hint, you can guess %d more times.\n", guessesLeft);
                    break;
                } else {
                    System.out.println("Incorrect input.");
                    System.out.print("Enter either 1 for guessing or 2 for hint: ");
                    option = keyboard.nextLine();
                }
            }
        }

        if(endGame) {
            System.out.print("\nCongratulations! The word was ");
            wordPrompt.forEach(character -> System.out.printf("%c ", character));

        } else {
            System.out.print("\nYou failed. The word was ");

            for(int i = 0; i < correctWord.length(); i++) {
                System.out.printf("%c ", correctWord.charAt(i));
            }
        }
        keyboard.close();
    }

    // function to create prompt of _ and #
    public static void createPrompt(String word) {
        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) == ' ') {
                wordPrompt.add('#');
            } else {
                wordPrompt.add('_');
            }
        }
    }

    // updates the prompt
    public static void updatePrompt(Character letter, boolean hint) {
        if(hint) {
            for (int i = 0; i < correctWord.length(); i++) {
                if (correctWord.charAt(i) == letter) {
                    wordPrompt.set(i, letter);
                }
            }
        } else {
            boolean isInWord = false;
            for (int i = 0; i < correctWord.length(); i++) {
                if (correctWord.charAt(i) == letter) {
                    wordPrompt.set(i, letter);
                    isInWord = true;
                }
            }

            if (isInWord) {
                System.out.printf("That's right! %c is in the word.\n", letter);
            } else {
                System.out.printf("Sorry, %c is not in the word.\n", letter);
                guessesLeft--;
            }
        }
    }

    // check to see if word guessed correctly
    public static boolean checkEndGame() {
        if(wordPrompt.contains('_')) {
            return false;
        }
        return true;
    }

    // function to get already guessed letters
    public static boolean checkIfAlreadyGuessed(Character letter) {
        if(alreadyGuessed.contains(letter)) {
            System.out.printf("Not valid input, you already guessed %s.\n", letter);
            guessesLeft--;
            return true;
        }
        return false;
    }

    // function to get hint of first letter of correct word that hasn't been guessed
    public static Character getHint() {
        guessesLeft --;
        for(int i = 0; i < wordPrompt.size(); i++) {
            if(wordPrompt.get(i).equals('_')) {
                updatePrompt(correctWord.charAt(i), true);
                return correctWord.charAt(i);
            }
        }
        // i had to put this here otherwise error?
        return ' ';
    }
}