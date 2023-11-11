package seedu.flashlingo.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.flashlingo.logic.Messages.*;
import static seedu.flashlingo.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.flashlingo.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;

import seedu.flashlingo.commons.core.index.Index;
import seedu.flashlingo.commons.util.ToStringBuilder;
import seedu.flashlingo.logic.Messages;
import seedu.flashlingo.logic.commands.exceptions.CommandException;
import seedu.flashlingo.model.Model;
import seedu.flashlingo.model.flashcard.FlashCard;
import seedu.flashlingo.model.tag.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adds a tag to the specified flash card
 */
public class AddTagCommand extends Command {
    /** Command word to add a tag **/
    public static final String COMMAND_WORD = "addt";

    // For help function
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to the specified flash card.\n"
            + "Parameters: "
            + PREFIX_TAG + "TAG \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "Mastered ";
    public static final String MESSAGE_ADD_TAGS_SUCCESS = "Tags added to: %1$s";
    /** Index of the flash card to be edited **/
    private final Index index;
    /** Set of tags to be added to the spcecified flash card **/
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a new AddTagCommand
     * @param index Index of the flash card to add the tags to
     * @param tags Set of tags to be added to the specified flash card
     */
    public AddTagCommand(Index index, Set<Tag> tags) {
        requireNonNull(tags);
        requireNonNull(index);
        this.index = index;
        this.tags.addAll(tags);
    }

    /**
     * Executes and returns the result of this command
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult after executing this
     * @throws CommandException If passed tags are empty, this is exception is thrown to notify the user
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<FlashCard> lastShownList = model.getFilteredFlashCardList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX);
        }

        if (tags.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_TAGS);
        }

        FlashCard flashCardToTag = lastShownList.get(index.getZeroBased());
        flashCardToTag.setTags(tags);
        model.updateFilteredFlashCardList(PREDICATE_SHOW_ALL_FLASHCARDS);

        return new CommandResult(String.format(MESSAGE_ADD_TAGS_SUCCESS, Messages.format(flashCardToTag)));
    }

    /**
     * Evaluates whether this command is equal to the passed command
     * @param other Object to check for equality against
     * @return Evaluates and returns a boolean depending on whether the passed object is equal to this
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand otherAddTagCommand = (AddTagCommand) other;
        return index.equals(otherAddTagCommand.index)
                && tags.equals(otherAddTagCommand.tags);
    }

    /**
     * Evaluates String representation of this Command
     * @return String representation of this Command
     */
    @Override
    public String toString() {
        Tag[] tagArray = tags.toArray(new Tag[0]);
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tags", Arrays.toString(tagArray))
                .toString();
    }
}
