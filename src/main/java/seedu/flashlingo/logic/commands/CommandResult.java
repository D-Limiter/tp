package seedu.flashlingo.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.flashlingo.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    /** Feedback message to be shown to the user. */
    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The application should switch theme. */
    private final boolean switchTheme;
    private boolean isStats;
    private double successRate;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean switchTheme) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.switchTheme = switchTheme;
        this.isStats = false;
        successRate = 0;
    }
    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean switchTheme,
                         double successRate) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.switchTheme = switchTheme;
        this.isStats = true;
        this.successRate = successRate;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     * @return feedback to user
     */
    public String getFeedbackToUser() {
        return feedbackToUser;
    }
    public double getSuccessRate() {
        return this.successRate;
    }

    /**
     * shows whether it is help command
     * @return whether to show help
     */
    public boolean isShowHelp() {
        return showHelp;
    }

    /**
     * shows whether it is exit command
     * @return whether to exit
     */
    public boolean isExit() {
        return exit;
    }

    /**
     * shows whether it is switch theme command
     * @return whether to switch theme
     */
    public boolean isSwitchTheme() {
        return switchTheme;
    }

    public boolean isStatsResult() {
        return this.isStats;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .toString();
    }

}
