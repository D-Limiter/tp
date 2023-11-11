//@@author itsNatTan
package seedu.flashlingo.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.Comparator;

import seedu.flashlingo.logic.commands.exceptions.CommandException;
import seedu.flashlingo.logic.parser.exceptions.ParseException;
import seedu.flashlingo.model.flashcard.FlashCard;
import seedu.flashlingo.session.SessionManager;

/**
 * An UI component that displays information of a {@code FlashCard}.
 * @author Nathanael M. Tan
 * @version 1.2
 * @since 1.2
 */
public class FlashcardBoxNoButton extends UiPart<Region> {

    private static final String FXML = "FlashcardBoxNoButton.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final FlashCard flashCard;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label original;
    @FXML
    private Label translation;
    @FXML
    private Label level;
    @FXML
    private Button reveal;

    @FXML
    private Label lang;

    private MainWindow mw;
    private int index;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code FlashCard code} with the given {@code FlashCard} and index to display.
     */
    public FlashcardBoxNoButton(FlashCard fc, int displayedIndex, MainWindow mw) {
        super(FXML);
        this.flashCard = fc;
        this.mw = mw;
        this.index = displayedIndex;
        original.setWrapText(true);
        translation.setWrapText(true);
        assert(!SessionManager.getInstance().isReviewSession());
        id.setText(displayedIndex + ") ");
        original.setText(fc.getOriginalWord().getWord() + ": ");
        if (fc.getIsRevealed()) {
            translation.setText(flashCard.getTranslatedWord().getWord());
            reveal.setText(" Hide ");
        } else {
            translation.setText("");
            reveal.setText("Reveal");
        }
        level.setText("Current Level: " + fc.getProficiencyLevel().toString());
        fc.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Reveals translation and changes text in box to Hide if it is not displayed
     * Hides translation and changes text to reveal if it is displayed
     */
    @FXML
    public void toggleReveal() throws CommandException, ParseException {
        if (flashCard.getIsRevealed()) {
            translation.setText("");
            this.mw.executeCommand("reveal " + index);
            reveal.setText("Reveal");
        } else {
            translation.setText(flashCard.getTranslatedWord().toString());
            this.mw.executeCommand("reveal " + index);
            reveal.setText(" Hide ");
        }
    }
}
