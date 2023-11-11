package seedu.flashlingo.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.flashlingo.model.Model;

/**
 * Indicates user wants to display the learning statistics
 */
public class StatsCommand extends Command {
    /** Command word to display usage statistics **/
    public static final String COMMAND_WORD = "stats";

    // For stats function
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Show user statistics\n"
            + "Example: " + COMMAND_WORD + " ";


    public static final String MESSAGE_SUCCESS = "Great work fellow learner! \nTotal number of flash cards: %d \n"
            + "Total number of flash cards remembered: %d\nOverall success rate: %f%%";
    /** Success rate of user **/
    public double successRate;
    /**
     * Executes the functioning for this command
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult after executing this command
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        int numberOfFlashCards = model.getNumberOfFlashCards();
        double numberOfRememberedWords = model.getNumberOfRememberedWords();
        successRate = 0;
        boolean isNonNegativeNumberOfWords = numberOfRememberedWords < 0;
        boolean isNonZeroNonNegativeNumberOfFlashCards = numberOfFlashCards <= 0;
        if (!(isNonNegativeNumberOfWords || isNonZeroNonNegativeNumberOfFlashCards)) {
            successRate = (numberOfRememberedWords / numberOfFlashCards) * 100;
        }
        CommandResult statsResult =  new CommandResult(String.format(MESSAGE_SUCCESS, numberOfFlashCards,
                                    (int) numberOfRememberedWords, successRate), false, false,
                false, successRate);

        return statsResult;
    }

    /**
     * Checks whether this is equal to the passed Object
     * @param other The passed object to check for equality against
     * @return True or False depending on whether this and other are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StatsCommand)) {
            return false;
        }

        return true;
    }

}
