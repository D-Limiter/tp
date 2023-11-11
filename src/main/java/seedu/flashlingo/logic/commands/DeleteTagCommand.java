package seedu.flashlingo.logic.commands;

import seedu.flashlingo.commons.core.index.Index;
import seedu.flashlingo.commons.util.ToStringBuilder;
import seedu.flashlingo.logic.Messages;
import seedu.flashlingo.logic.commands.exceptions.CommandException;
import seedu.flashlingo.model.Model;
import seedu.flashlingo.model.flashcard.FlashCard;
import seedu.flashlingo.model.tag.Tag;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.flashlingo.logic.Messages.MESSAGE_EMPTY_TAGS;
import static seedu.flashlingo.logic.Messages.MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX;
import static seedu.flashlingo.logic.parser.CliSyntax.*;
import static seedu.flashlingo.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;

public class DeleteTagCommand extends Command {
    /** Command word to delete a tag **/
    public static final String COMMAND_WORD = "delTag";

    // For help function
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the specified tag of the "
            + "specified flash card.\n"
            + "Parameters: "
            + PREFIX_TAG + "TAG \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "Mastered ";
    public static final String MESSAGE_DELETE_TAGS_SUCCESS = "Tag deleted for: %1$s";
    private final Index index;
    private Tag toBeDeletedTag;
    /**
     * Constructs a new DeleteTagCommand
     * @param index Index of the flash card with Tag to be deleted
     * @param toBeDeletedTag Specified Tag to be deleted
     */
    public DeleteTagCommand(Index index, Tag toBeDeletedTag) {
        requireNonNull(index);
        requireNonNull(toBeDeletedTag);
        this.index = index;
        this.toBeDeletedTag = toBeDeletedTag;
    }
    /**
     * Executes and returns the result of this command
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult after executing this
     * @throws CommandException If passed tag to be deleted is empty, this is exception is thrown to notify the user
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<FlashCard> lastShownList = model.getFilteredFlashCardList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX);
        }

        if (this.toBeDeletedTag == null) {
            throw new CommandException(MESSAGE_EMPTY_TAGS);
        }

        FlashCard flashCardToEditTag = lastShownList.get(index.getZeroBased());
        flashCardToEditTag.deleteTag(this.toBeDeletedTag);
        model.updateFilteredFlashCardList(PREDICATE_SHOW_ALL_FLASHCARDS);

        return new CommandResult(String.format(MESSAGE_DELETE_TAGS_SUCCESS, Messages.format(flashCardToEditTag)));
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
        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherEditTagCommand = (DeleteTagCommand) other;
        return index.equals(otherEditTagCommand.index)
                && toBeDeletedTag.equals(otherEditTagCommand.toBeDeletedTag);
    }
    /**
     * Evaluates String representation of this Command
     * @return String representation of this Command
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("Deleted Tag", toBeDeletedTag)
                .toString();
    }
}
