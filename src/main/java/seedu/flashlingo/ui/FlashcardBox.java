//@@author itsNatTan
package seedu.flashlingo.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.flashlingo.logic.commands.exceptions.CommandException;
import seedu.flashlingo.logic.parser.exceptions.ParseException;
import seedu.flashlingo.model.flashcard.FlashCard;
import seedu.flashlingo.session.SessionManager;

import java.util.Comparator;

/**
 * An UI component that displays information of a {@code FlashCard}.
 * @author Nathanael M. Tan
 * @version 1.2
 * @since 1.2
 */
public class FlashcardBox extends UiPart<Region> {

    private static final String FXML = "FlashcardBox.fxml";

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
    private Label lang;

    @FXML
    private Button reveal;

    private MainWindow mw;
    private int index;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code FlashCard code} with the given {@code FlashCard} and index to display.
     */
    public FlashcardBox(FlashCard fc, int displayedIndex, MainWindow mw) {
        super(FXML);
        // Ensure that FlashCard with buttons is only created when in review session
        //@@author itsNatTan
        assert(SessionManager.getInstance().isReviewSession());
        //@@author itsNatTan
        this.flashCard = fc;
        this.mw = mw;
        this.index = displayedIndex;
        original.setWrapText(true);
        translation.setWrapText(true);
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
     * Handles success when user presses "Yes" button
     */
    @FXML
    public void success() throws CommandException, ParseException {
        this.mw.executeCommand("yes");
        level.setText("Proficiency Level: " + flashCard.getProficiencyLevel().getLevel());
    }

    /**
     * Handles failure when user presses "No" button
     */
    @FXML
    public void failure() throws CommandException, ParseException {
        this.mw.executeCommand("no");
        level.setText("Proficiency Level: " + flashCard.getProficiencyLevel().getLevel());
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
