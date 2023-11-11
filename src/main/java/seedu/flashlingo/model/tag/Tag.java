package seedu.flashlingo.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.flashlingo.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the Flashlingo.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags names should be either of Essentials, Uncommon, Rare," +
            "Slang";
    public static final String VALIDATION_REGEX = "Essentials|Uncommon|Rare|Slang";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) throws IllegalArgumentException {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = getCommonForm(tagName);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches("(?i)" + VALIDATION_REGEX);
    }

    /**
     * Evaluates the tagName in its common camel form
     * @param inputTagName TagName to be converted to its subsequent common form
     * @return Common form(first character uppercase followed by the resi in lower case
     * @throws IllegalArgumentException If a null tagName is passed, to communicate to the user that the tagName should
     * not be null
     */
    public static String getCommonForm(String inputTagName) throws IllegalArgumentException{
        if (inputTagName == null) {
            throw new IllegalArgumentException("Tag name cannot be null");
        }

        String lowerCaseTag = inputTagName.toLowerCase();

        switch (lowerCaseTag) {
            case "essentials":
                return "Essentials";
            case "uncommon":
                return "Uncommon";
            case "rare":
                return "Rare";
            case "slang":
                return "Slang";
            default:
                throw new IllegalArgumentException("Tag name cannot be null");
        }
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
