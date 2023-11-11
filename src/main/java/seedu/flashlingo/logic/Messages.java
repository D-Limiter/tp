package seedu.flashlingo.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.flashlingo.logic.parser.Prefix;
import seedu.flashlingo.model.flashcard.FlashCard;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_CONSTRAINTS =
            "Languages should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_EMPTY_VALUE = "Word/Translation cannot be empty";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX = "The flash card index provided is invalid";
    public static final String MESSAGE_FLASHCARDS_LISTED_OVERVIEW = "%1$d flash card(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_DUPLICATE_FLASHCARD = "This flash card already exists in Flashlingo!";

    public static final String MESSAGE_IN_REVIEW_SESSION = "Sorry, currently you are in a review session. Your command "
        + "is not supported.\nPlease end the review session first.";
    public static final String MESSAGE_NOT_IN_REVIEW_SESSION = "You are not in a review session.";
    public static final String MESSAGE_OPEN_FILE_FAIL = "File not found or accessible.";
    public static final String MESSAGE_READ_FILE_FAIL = "File cannot be read due to invalid content or format.";
    public static final String MESSAGE_SAME_WORD = "Word and translation should be different.";
    public static final String MESSAGE_EMPTY_TAGS = "Input tags should not be empty";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code flashcard} for display to the user.
     */
    public static String format(FlashCard flashCard) {
        final StringBuilder builder = new StringBuilder();
        builder.append(flashCard.getOriginalWord().getWord())
                .append("; Translated Word: ")
                .append(flashCard.getTranslatedWord().getWord());
        return builder.toString();
    }
}
