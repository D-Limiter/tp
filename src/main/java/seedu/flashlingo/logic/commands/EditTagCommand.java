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
import static seedu.flashlingo.logic.Messages.*;
import static seedu.flashlingo.logic.parser.CliSyntax.*;
import static seedu.flashlingo.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;

public class EditTagCommand extends Command {
    /** Command word to edit a tag **/
    public static final String COMMAND_WORD = "editTag";

    // For help function
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a tag of the specified flash card.\n"
            + "Parameters: "
            + PREFIX_ORIGINAL_TAG + "TAG "
            + PREFIX_REPLACED_TAG + "REPLACED_TAG \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ORIGINAL_TAG + "Essentials"
            + PREFIX_REPLACED_TAG + "Uncommon";
    public static final String MESSAGE_EDIT_TAGS_SUCCESS = "Tags edited for: %1$s";
    private final Index index;
    private Tag originalTag;
    private Tag replacedTag;
    /**
     * Constructs a new EditTagCommand
     * @param index Index of the flash card to add the tags to
     * @param originalTag Specified Tag to be edited
     * @param replacedTag Specified Tag to be replaced with
     */
    public EditTagCommand(Index index, Tag originalTag, Tag replacedTag) {
        requireNonNull(index);
        requireNonNull(originalTag);
        requireNonNull(replacedTag);
        this.index = index;
        this.originalTag = originalTag;
        this.replacedTag = replacedTag;
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

        if (this.originalTag == null || this.replacedTag == null) {
            throw new CommandException(MESSAGE_EMPTY_TAGS);
        }

        FlashCard flashCardToEditTag = lastShownList.get(index.getZeroBased());
        flashCardToEditTag.replaceTags(originalTag,replacedTag);
        model.updateFilteredFlashCardList(PREDICATE_SHOW_ALL_FLASHCARDS);

        return new CommandResult(String.format(MESSAGE_EDIT_TAGS_SUCCESS, Messages.format(flashCardToEditTag)));
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
        if (!(other instanceof EditTagCommand)) {
            return false;
        }

        EditTagCommand otherEditTagCommand = (EditTagCommand) other;
        return index.equals(otherEditTagCommand.index)
                && originalTag.equals(otherEditTagCommand.originalTag)
                && replacedTag.equals(otherEditTagCommand.replacedTag);
    }
    /**
     * Evaluates String representation of this Command
     * @return String representation of this Command
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("Original Tag", originalTag)
                .add("Replaced Tag", replacedTag)
                .toString();
    }
}
