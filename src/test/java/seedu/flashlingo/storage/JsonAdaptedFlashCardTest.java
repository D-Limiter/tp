package seedu.flashlingo.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.flashlingo.logic.Messages.MESSAGE_CONSTRAINTS;
import static seedu.flashlingo.storage.JsonAdaptedFlashCard.DATE_PATTERN;
import static seedu.flashlingo.storage.JsonAdaptedFlashCard.INVALID_DATE_FORMAT_MESSAGE;
import static seedu.flashlingo.storage.JsonAdaptedFlashCard.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.flashlingo.testutil.Assert.assertThrows;
import static seedu.flashlingo.testutil.TypicalFlashCards.WORD;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.flashlingo.commons.exceptions.IllegalValueException;
import seedu.flashlingo.model.flashcard.ProficiencyLevel;
import seedu.flashlingo.model.flashcard.words.OriginalWord;
import seedu.flashlingo.model.flashcard.words.TranslatedWord;

public class JsonAdaptedFlashCardTest {
    private static final String INVALID_ORIGINAL_WORD_LANGUAGE = "English! ";
    private static final String INVALID_TRANSLATED_WORD_LANGUAGE = "French";
    private static final int INVALID_LEVEL = -1;
    private static final String INVALID_WHEN_TO_REVIEW = "2023-2-12T30:59:59Z";

    private static final String VALID_ORIGINAL_WORD = WORD.getOriginalWord().getWord();
    private static final String VALID_ORIGINAL_WORD_LANGUAGE = WORD.getOriginalWord().getLanguage();
    private static final String VALID_TRANSLATED_WORD = WORD.getTranslatedWord().getWord();
    private static final String VALID_TRANSLATED_WORD_LANGUAGE = WORD.getTranslatedWord().getLanguage();
    private static final String VALID_WHEN_TO_REVIEW = DateTimeFormatter.ofPattern(DATE_PATTERN)
            .format(ZonedDateTime.ofInstant(WORD.getWhenToReview().toInstant(), ZoneOffset.UTC));
    private static final int VALID_LEVEL = WORD.getProficiencyLevel().getLevel();
    private static final boolean VALID_REMEMBRANCE = WORD.isRecalled();
    private static final List<JsonAdaptedTag> VALID_TAGS = WORD.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validFlashCardDetails_returnsFlashCard() throws Exception {
        JsonAdaptedFlashCard flashCard = new JsonAdaptedFlashCard(WORD);
        assertEquals(WORD, flashCard.toModelType());
    }

    @Test
    public void toModelType_invalidOriginalWordLanguage_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard =
                new JsonAdaptedFlashCard(VALID_ORIGINAL_WORD, INVALID_ORIGINAL_WORD_LANGUAGE, VALID_TRANSLATED_WORD,
                        VALID_TRANSLATED_WORD_LANGUAGE, VALID_WHEN_TO_REVIEW, VALID_LEVEL, VALID_REMEMBRANCE,
                        VALID_TAGS);
        String expectedMessage = MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }

    @Test
    public void toModelType_nullOriginalWord_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard = new JsonAdaptedFlashCard(null, VALID_ORIGINAL_WORD_LANGUAGE,
                VALID_TRANSLATED_WORD, VALID_TRANSLATED_WORD_LANGUAGE, VALID_WHEN_TO_REVIEW, VALID_LEVEL,
                VALID_REMEMBRANCE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, OriginalWord.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }

    @Test
    public void toModelType_invalidTranslatedWordLanguage_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard =
                new JsonAdaptedFlashCard(VALID_ORIGINAL_WORD, VALID_ORIGINAL_WORD_LANGUAGE, VALID_TRANSLATED_WORD,
                        INVALID_TRANSLATED_WORD_LANGUAGE, VALID_WHEN_TO_REVIEW, VALID_LEVEL, VALID_REMEMBRANCE,
                        VALID_TAGS);
        String expectedMessage = MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }

    @Test
    public void toModelType_nullTranslatedWordLanguage_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard = new JsonAdaptedFlashCard(VALID_ORIGINAL_WORD, VALID_ORIGINAL_WORD_LANGUAGE,
                VALID_TRANSLATED_WORD, null, VALID_WHEN_TO_REVIEW, VALID_LEVEL, VALID_REMEMBRANCE,
                VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, TranslatedWord.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }

    @Test
    public void toModelType_invalidLevel_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard =
                new JsonAdaptedFlashCard(VALID_ORIGINAL_WORD, VALID_ORIGINAL_WORD_LANGUAGE, VALID_TRANSLATED_WORD,
                        VALID_TRANSLATED_WORD_LANGUAGE, VALID_WHEN_TO_REVIEW, INVALID_LEVEL, VALID_REMEMBRANCE,
                        VALID_TAGS);
        String expectedMessage = ProficiencyLevel.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }

    @Test
    public void toModelType_nullWhenToReview_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard = new JsonAdaptedFlashCard(VALID_ORIGINAL_WORD, VALID_ORIGINAL_WORD_LANGUAGE,
                VALID_TRANSLATED_WORD, VALID_TRANSLATED_WORD_LANGUAGE, null, VALID_LEVEL, VALID_REMEMBRANCE,
                VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }

    @Test
    public void toModelType_invalidWhenToReview_throwsIllegalValueException() {
        JsonAdaptedFlashCard flashCard =
                new JsonAdaptedFlashCard(VALID_ORIGINAL_WORD, VALID_ORIGINAL_WORD_LANGUAGE, VALID_TRANSLATED_WORD,
                        VALID_TRANSLATED_WORD_LANGUAGE, INVALID_WHEN_TO_REVIEW, VALID_LEVEL, VALID_REMEMBRANCE,
                        VALID_TAGS);
        String expectedMessage = INVALID_DATE_FORMAT_MESSAGE;
        assertThrows(IllegalValueException.class, expectedMessage, flashCard::toModelType);
    }
}
