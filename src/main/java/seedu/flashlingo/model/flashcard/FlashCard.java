//@@author itsNatTan

package seedu.flashlingo.model.flashcard;

import static seedu.flashlingo.commons.util.AppUtil.checkArgument;
import static seedu.flashlingo.logic.Messages.MESSAGE_SAME_WORD;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import seedu.flashlingo.model.flashcard.words.OriginalWord;
import seedu.flashlingo.model.flashcard.words.TranslatedWord;
import seedu.flashlingo.model.tag.Tag;

/**
 * Represents each flashcard
 *
 * @author Nathanael M. Tan
 * @version 1.0
 * @since 1.0
 */
public class FlashCard {
    private final OriginalWord originalWord;
    private final TranslatedWord translatedWord;
    private Date whenToReview; // Date the flashcard was needs to be reviewed
    private ProficiencyLevel currentLevel; // How many times successfully remembered
    private boolean isRemembered; //if successfully remembers word
    private boolean isRevealed = false;

    private final Set<Tag> tags = new HashSet<>();
    /**
     * Constructor for Flashcard
     *
     * @param originalWord   The word in the original language
     * @param translatedWord The word in the language you are learning
     * @param whenToReview   The date of when you need to review this word
     * @param level          The level of familiarity with the word
     */
    public FlashCard(OriginalWord originalWord, TranslatedWord translatedWord, Date whenToReview,
                     ProficiencyLevel level) {
        this.currentLevel = level;
        this.whenToReview = whenToReview;
        this.translatedWord = translatedWord;
        this.originalWord = originalWord;
        checkArgument(isValidWord(originalWord, translatedWord), MESSAGE_SAME_WORD);
    }

    /**
     * Constructor for Flashcard
     *
     * @param originalWord   The word in the original language
     * @param translatedWord The word in the language you are learning
     * @param whenToReview   The date of when you need to review this word
     * @param level          The level of familiarity with the word
     * @param isRemembered   Whether the word was remembered
     */
    public FlashCard(OriginalWord originalWord, TranslatedWord translatedWord, Date whenToReview,
                     ProficiencyLevel level, boolean isRemembered) {
        this.currentLevel = level;
        this.whenToReview = whenToReview;
        this.translatedWord = translatedWord;
        this.originalWord = originalWord;
        this.isRemembered = isRemembered;
    }

    /**
     * Constructor for Flashcard
     *
     * @param originalWord   The word in the original language
     * @param translatedWord The word in the language you are learning
     * @param whenToReview   The date of when you need to review this word
     * @param level          The level of familiarity with the word
     * @param isRemembered   Whether the word was remembered
     * @param tags Tags for this flash card
     */
    public FlashCard(OriginalWord originalWord, TranslatedWord translatedWord, Date whenToReview,
                     ProficiencyLevel level, boolean isRemembered, Set<Tag> tags) {
        this.currentLevel = level;
        this.whenToReview = whenToReview;
        this.translatedWord = translatedWord;
        this.originalWord = originalWord;
        this.isRemembered = isRemembered;
        this.tags.addAll(tags);
    }
    public OriginalWord getOriginalWord() {
        return originalWord;
    }

    public TranslatedWord getTranslatedWord() {
        return translatedWord;
    }

    public Date getWhenToReview() {
        return whenToReview;
    }

    public ProficiencyLevel getProficiencyLevel() {
        return currentLevel;
    }

    public boolean isDeletedFromReview() {
        return this.currentLevel.isDeletedFromReview();
    }

    public void setIsRevealed(Boolean isRevealed) {
        this.isRevealed = isRevealed;
    }
    public boolean getIsRevealed() {
        return this.isRevealed;
    }

    private boolean isValidWord(OriginalWord word, TranslatedWord translate) {
        return !word.getWord().equalsIgnoreCase(translate.getWord());
    }

    /**
     * Returns the tags set for this FlashCard
     * @return tags set for this flashCard
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Sets the tags for this object by adding all elements from the provided set.
     * If there are existing tags, the new tags will be added to the current set.
     * This method allows for dynamically updating the tag set associated with this object.
     * @param tags A Set of Tag objects to be added. It should not be null.
     *             If the set is empty, no tags will be added.
     *             If there are duplicate tags between the existing set and the provided set,
     *             each tag will only be stored once due to the Set nature.
     *
     */
    public void setTags(Set<Tag> tags) {
        this.tags.addAll(tags);
    }

    /**
     * Replaces the passed original tag with the replaced Tag
     * @param originalTag Tag to be replaced
     * @param replacedTag Tag t be replaced with
     */
    public void replaceTags(Tag originalTag, Tag replacedTag) {
        this.tags.remove(originalTag);
        this.tags.add(replacedTag);
    }
    /**
     * Edits the flashCard
     * @param changes The new word to replace the old word
     * @return The new flashcard
     */
    public FlashCard editFlashCard(String[] changes) {
        OriginalWord originalWord = this.originalWord.editWord(changes[0], changes[1]);
        TranslatedWord translatedWord = this.translatedWord.editWord(changes[2], changes[3]);
        return new FlashCard(originalWord, translatedWord, whenToReview, currentLevel);
    }
    public void deleteTag(Tag toBeDeletedTag) {
        this.tags.remove(toBeDeletedTag);
    }
    /**
     * Returns true if both flashcards have the same originalWord and translatedWord.
     * This defines a weaker notion of equality between two flashcards.
     */
    public boolean isSameFlashCard(FlashCard otherFlashCard) {
        if (otherFlashCard == this) {
            return true;
        }

        return otherFlashCard != null
                && otherFlashCard.getOriginalWord().equals(getOriginalWord())
                && otherFlashCard.getTranslatedWord().equals(getTranslatedWord());
    }

    //@@author D-Limiter
    /**
     * Returns true if the original word or the translation contains the keyword.
     * @param inputWord The keyword to check for
     * @return True or False depending on whether the keyword is found
     */
    public boolean hasKeyword(String inputWord) {
        return this.originalWord.hasSubpart(inputWord) || this.translatedWord.hasSubpart(inputWord);
    }

    /**
     * Sets this FlashCard as remembered
     */
   //@@D-Limiter
    public void recallFlashCard() {
        this.isRemembered = true;
    }
    /**
     * Evaluates and returns if this FlashCard is remembered
     */
    public boolean isRecalled() {
        return this.isRemembered;
    }

    /**
     * Sets this FlashCard as forgotten
     */
    public void forgetFlashCard() {
        this.isRemembered = false;
    }

    /**
     * Returns true if the original word or the translation is of the language.
     * @param language The language to check for
     * @return True or False depending on whether the language is found
     */
    public boolean isSameLanguage(String language) {
        return this.originalWord.isSameLanguage(language) || this.translatedWord.isSameLanguage(language);
    }

    //@@author itsNatTan
    /**
     * Returns true if the review date is before the current date.
     * @return True or False depending on whether the review date is before the current date
     */
    public boolean isOverdue() {
        return this.whenToReview.before(new Date());
    }

    /**
     * Update the flash card to next level
     */
    public void updateLevel(boolean isSuccess) {
        if (isSuccess) {
            getProficiencyLevel().upgradeLevel();
            updateReviewDate(getProficiencyLevel().calculateNextReviewInterval());
        } else {
            getProficiencyLevel().downgradeLevel();
            updateReviewDate(getProficiencyLevel().calculateNextReviewInterval());
        }
    }

    /**
     * Formats Flashcard for writing to textFile
     *
     * @return Tab separated String formatted for writing
     */
    @Override
    public String toString() {
        String sb = originalWord + " | " + originalWord.getLanguage() + " | " + translatedWord + " | "
                + translatedWord.getLanguage() + " | " + whenToReview.toString() + " | " + currentLevel + "\n";
        return sb;
    }

    public void updateReviewDate(long timeInMs) {
        this.whenToReview = new Date(new Date().getTime() + timeInMs);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FlashCard)) {
            return false;
        }

        FlashCard otherFlashCard = (FlashCard) other;
        return originalWord.equals(otherFlashCard.originalWord)
                && translatedWord.equals(otherFlashCard.translatedWord);
    }
}
