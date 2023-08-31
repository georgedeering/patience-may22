/**
 * Displays the card images on a table (the Javafx stage)
 * @author George Deering, Faisal Rezwan, Chris Loftus and Lynda Thomas
 * @version 4.0
 */
package uk.ac.aber.dcs.cs12320.cards.gui.javafx;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.*;

public class CardTable {
    public final Stage stage;
    private boolean done;
    private ArrayList<String> cards = new ArrayList<>();

    /**
     * cardTable constructor
     * @param stage
     */
    public CardTable(Stage stage) {
        this.stage = stage;
        stage.setTitle("The Cards");
    }

    /**
     * getDealtCards gets the cards that have already been dealt
     * @return cards that have been dealt as an ArrayList<String>
     */
    public ArrayList<String> getDealtCards() {
        return cards;
    }

    /**
     * addCard adds a card to the dealtCards when it has been dealt
     * @param card
     */
    public void addCard(String card) {
        cards.add(card);
        cardDisplay();
    }

    /**
     * removeCard removes a card from the dealt cards
     * @param pos
     */
    public void removeCard(int pos) {
        cards.remove(pos);
        cardDisplay();
    }

    /**
     * Called when the user quits the game. It results
     * in the face-down pack of cards not being displayed.
     */
    public void allDone() {
        done = true;
    }

    /**
     * Displays all the face-up cards (just the top showing cards)
     * and if the game is not over then also displays the face-down deck.
     */
    public void cardDisplay() {
        // We need to do this within the GUI thread. We assume
        // that the method is called by a non-GUI thread
        /*
        if(!dealtCards.isEmpty()) {
            for(int i = dealtCards.size(); i != 0; i--) {
                cards.add(dealtCards.get(i));
            }
        }
        */
        Platform.runLater(() -> {
            ScrollPane sp = new ScrollPane();
            Scene scene = new Scene(sp, 800, 155);
            scene.setFill(Color.BLACK);
            HBox box = new HBox();
            Image image = null;
            if (!cards.isEmpty() && !done) {
                for (String card : cards) {
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/" + card + ".gif")));
                    drawCards(box, image);
                }
                // Draws the face-down top card of our pack of cards
                image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/b.gif")));
                drawCards(box, image);
            }

            sp.setContent(box);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            stage.setScene(scene);
            stage.show();
            stage.setAlwaysOnTop(true);


        });

    }

    /**
     * drawCards creates and displays the cards within the scene
     * @param box
     * @param image
     */
    private void drawCards(HBox box, Image image) {
        ImageView iv;
        iv = new ImageView();
        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        box.getChildren().add(iv);
    }


}
