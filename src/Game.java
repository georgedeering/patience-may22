/**
 * The Game of patience main class
 * @author George Deering, Faisal Rezwan, Chris Loftus and Lynda Thomas
 * @version 4.0
 */

import java.util.*;
import java.io.*;
import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

public class Game extends Application {
    private CardTable cardTable = new CardTable(new Stage());
    private Scanner scan;
    //private Random rand = new Random();
    private ArrayList<String> cards = new ArrayList<>();
    private ArrayList<Leaderboard> leaderboard = new ArrayList<>();
    private boolean amalgamateDone;

    /**
     * shufflePack shuffles the pack of cards before they are dealt
     */
    private void shufflePack() {
        Collections.shuffle(cards);
    }

    /**
     * printPack prints the pack of cards to the command line
     */
    private void printPack() {
        System.out.println(cards);
    }

    /**
     * dealCard deals a car
     */
    private void dealCard() {
        if (!cards.isEmpty()) {
            cardTable.addCard(cards.get(0));
            cards.remove(0);
        } else {
            cardTable.allDone();
        }
    }

    /**
     * loadLeaderboard loads a leaderboard from a file into a Leaderboard object
     * @throws IOException
     */
    private void loadLeaderboard() throws IOException {
        FileReader filereader = new FileReader("leaderboard.txt");
        try (Scanner fileScanner = new Scanner(filereader).useDelimiter("\r?\n|\r")) {
            for (int i = 0; i < 9; i++) {
                if (fileScanner.hasNextLine()) {
                    String player = fileScanner.nextLine();
                    String playerName = player.split(":")[0];
                    int playerScore = Integer.parseInt(player.split(":")[1]);
                    Leaderboard newPlayer = new Leaderboard(playerName, playerScore);
                    leaderboard.add(newPlayer);
                }
            }
        }
        filereader.close();
    }

    /**
     * saveLeaderboard saves the leaderboard to a file
     * @throws IOException
     */
    private void saveLeaderboard() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String playerName;
        int playerScore;
        for (int i = 0; i < 10; i++) {
            playerName = leaderboard.get(i).getName();
            playerScore = leaderboard.get(i).getScore();
            stringBuilder.append(playerName).append(":").append(playerScore).append("\n");
        }

        try(FileWriter fileWriter = new FileWriter("leaderboard.txt")) {
            fileWriter.write(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * printLeaderboard prints the leaderboard to the command line
     */
    private void printLeaderboard() {
        for (int i = 0; i < 10; i++) {
            if (leaderboard.get(i) != null) {
                String playerName = leaderboard.get(i).getName();
                int playerScore = leaderboard.get(i).getScore();
                System.out.println((i + 1) + ". Player name: " + playerName + " with score: " + playerScore);
            }
        }
    }

    /**
     * getCards loads the cards from directory
     */
    private void getCards() {
        File dir = new File("C:\\Users\\georg\\OneDrive\\Documents\\Aberystwyth University\\Year 1 Semester 2\\CS12320 Programming Using an Object-Oriented Language\\Main assignment\\implementationV1.0\\src\\cards");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File child : files) {
                String cardName = child.getName().split(".gif")[0];
                cards.add(cardName);
            }
        }
        removeBlankCard();
    }

    /**
     * removeBlankCard removes the blank card from the deck so that it cannot be dealt
     */
    private void removeBlankCard() {
        cards.remove(36);
    }

    /**
     * makeMove makes a move by combining two piles a certain number of spaces if they match
     * @param spacesToMove
     */
    private void makeMove(int spacesToMove) {
        if(spacesToMove <= 0) {
            System.out.println("Error, spaces-to-move cannot be zero or less!");
        } else if(cardTable.getDealtCards().size() > spacesToMove) {
            ArrayList<String> tempDealtCards = cardTable.getDealtCards();
            int originalSize = tempDealtCards.size() - 1;
            String cardToReplace = tempDealtCards.get(originalSize);
            String cardToBeReplaced = tempDealtCards.get(originalSize - spacesToMove);
            if(cardToReplace.charAt(0) == cardToBeReplaced.charAt(0) || cardToReplace.charAt(1) == cardToBeReplaced.charAt(1)) {
                tempDealtCards.set(originalSize - spacesToMove, cardToReplace);
                cardTable.removeCard(originalSize);
            } else {
                System.out.println("Error, cards do not match!");
            }
        } else {
            System.out.println("Error, spaces-to-move is more than spaces available");
        }
    }

    /**
     * amalgamate amalgamates two piles together if they match
     * @param intCardToReplace
     * @param intCardToBeReplaced
     */
    private void amalgamate(int intCardToReplace, int intCardToBeReplaced) {
        String cardToReplace = cardTable.getDealtCards().get(intCardToReplace);
        String cardToBeReplaced = cardTable.getDealtCards().get(intCardToBeReplaced);
        if(cardToReplace.charAt(0) == cardToBeReplaced.charAt(0) || cardToReplace.charAt(1) == cardToBeReplaced.charAt(1)) {
            cardTable.getDealtCards().set(intCardToBeReplaced, cardToReplace);
            cardTable.removeCard(intCardToReplace);
            amalgamateDone = true;
        }
    }

    /**
     * runMenu prints the menu to the command line
     */
    private void runMenu() {
        System.out.println("1) Show the pack");
        System.out.println("2) Shuffle the cards (before starting the game)");
        System.out.println("3) Deal a card");
        System.out.println("4) Move last card onto previous pile");
        System.out.println("5) Move last card onto the pile, skipping two piles");
        System.out.println("6) Amalgamate piles in the middle (by giving their numbers)");
        System.out.println("7) Show all displayed cards on command line");
        System.out.println("8) Play for me");
        System.out.println("9) Show the top ten results of all games so far (best score is 1)");
        System.out.println("Q) Quit");
    }

    /**
     * runGame is the main game method that runs the menu and then takes the user input by Scanner
     * @throws IOException
     */
    private void runGame() throws IOException {
        getCards();
        loadLeaderboard();
        cardTable.cardDisplay();
        String response;
        do {

            runMenu();
            System.out.println("What would you like to do? >> ");
            scan = new Scanner(System.in);
            response = scan.nextLine().toUpperCase();
            switch (response) {
                //1) Show the pack
                case "1" -> {
                    printPack();
                    break;
                }

                //2) Shuffle the cards (before starting the game)
                case "2" -> {
                    shufflePack();
                    break;
                }

                //3) Deal a card
                case "3" -> {
                    dealCard();
                    break;
                }

                //4) Move last card onto previous pile
                case "4" -> {
                    makeMove(1);
                    break;
                }

                //5) Move last card onto the pile, skipping two piles
                case "5" -> {
                    makeMove(2);
                    break;
                }

                //6) Amalgamate piles in the middle (by giving their numbers)
                case "6" -> {
                    try {
                        System.out.println("Enter position of card to be moved >> ");
                        int cardToReplace = scan.nextInt();
                        System.out.println("Enter position of card to be replaced >> ");
                        int cardToBeReplaced = scan.nextInt();
                        amalgamate(cardToReplace, cardToBeReplaced);
                    } catch (Exception e) {
                        System.out.println("Error, invalid input. Please try again");
                    }
                    break;
                }

                //7) Show all displayed cards on command line
                case "7" -> {
                    System.out.println(cardTable.getDealtCards());
                    break;
                }

                //8) Play for me
                case "8" -> {
                    amalgamateDone = false;
                    for (int i = 0; i < (cardTable.getDealtCards().size()); i++) {
                        for (int j = 0; j < (cardTable.getDealtCards().size()); j++) {
                            while (!amalgamateDone) {
                                amalgamate(i, j);
                            }
                        }
                    }
                    amalgamateDone = false;
                    break;
                }

                //9) Show the top ten results of all games so far (best score is 1)
                case "9" -> {
                    printLeaderboard();
                    break;
                }

                //Q) Quit
                case "Q" -> {
                    System.out.println("Enter your name >> ");
                    String playerName = scan.nextLine();
                    int playerScore = 52 - cards.size();
                    System.out.println(playerName + " you scored: " + playerScore);
                    Leaderboard player = new Leaderboard(playerName, playerScore);
                    leaderboard.add(player);
                    saveLeaderboard();
                    cardTable.allDone();
                    break;
                }

                //Invalid input
                default -> {
                    System.out.println("Invalid input, please try again");
                    break;
                }
            }
        } while (!(response.equals("Q")));
    }
    // //////////////////////////////////////////////////////////////

    /**
     * start is where the game starts and the scene is created
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        cardTable = new CardTable(stage);

        Game game = new Game();
        // The interaction with this game is from a command line
        // menu. We need to create a separate non-GUI thread
        // to run this in. DO NOT REMOVE THIS.
        Runnable commandLineTask = () -> {
            try {
                game.runGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();
    }

    /**
     * main launches the Application
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}