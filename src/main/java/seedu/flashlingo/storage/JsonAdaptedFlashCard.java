package seedu.flashlingo.storage;

import static seedu.flashlingo.logic.Messages.MESSAGE_CONSTRAINTS;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.flashlingo.commons.exceptions.IllegalValueException;
import seedu.flashlingo.model.flashcard.FlashCard;
import seedu.flashlingo.model.flashcard.ProficiencyLevel;
import seedu.flashlingo.model.flashcard.words.OriginalWord;
import seedu.flashlingo.model.flashcard.words.TranslatedWord;
import seedu.flashlingo.model.tag.Tag;

/**
 * Jackson-friendly version of {@link FlashCard}.
 */
public class JsonAdaptedFlashCard {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Flash card's %s field is missing!";
    public static final String INVALID_DATE_FORMAT_MESSAGE = "Invalid date format.";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX";
    private final String originalWord;
    private final String originalWordLanguage;
    private final String translatedWord;
    private final String translatedWordLanguage;
    private final String whenToReview;
    private final int level;
    private final boolean isRemembered;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    /**
     * Constructs a {@code JsonAdaptedFlashCard} with the given flash card details.
     */
    @JsonCreator
    public JsonAdaptedFlashCard(@JsonProperty("originalWord") String originalWord,
                                @JsonProperty("originalWordLanguage") String originalWordLanguage,
                                @JsonProperty("translatedWord") String translatedWord,
                                @JsonProperty("translatedWordLanguage") String translatedWordLanguage,
                                @JsonProperty("whenToReview") String whenToReview,
                                @JsonProperty("level") int level,
                                @JsonProperty("isRemembered") boolean isRemembered,
                                @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.originalWord = originalWord;
        this.originalWordLanguage = originalWordLanguage;
        this.translatedWord = translatedWord;
        this.translatedWordLanguage = translatedWordLanguage;
        this.whenToReview = whenToReview;
        this.isRemembered = isRemembered;
        this.level = level;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code FlashCard} into this class for Jackson use.
     */
    public JsonAdaptedFlashCard(FlashCard source) {
        originalWord = source.getOriginalWord().getWord();
        originalWordLanguage = source.getOriginalWord().getLanguage();
        translatedWord = source.getTranslatedWord().getWord();
        translatedWordLanguage = source.getTranslatedWord().getLanguage();
        level = source.getProficiencyLevel().getLevel();
        isRemembered = source.isRecalled();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        Date whenToReview = source.getWhenToReview();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(whenToReview.toInstant(), ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        String iso8601Date = formatter.format(zonedDateTime);
        this.whenToReview = iso8601Date;
    }

    /**
     * Converts this Jackson-friendly adapted flash card object into the model's {@code FlashCard} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted flash card.
     */
    public FlashCard toModelType() throws IllegalValueException {
        final List<Tag> flashCardTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            flashCardTags.add(tag.toModelType());
        }
        if (originalWord == null || originalWordLanguage == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
              OriginalWord.class.getSimpleName()));
        }
        if (translatedWord == null || translatedWordLanguage == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
              TranslatedWord.class.getSimpleName()));
        }
        if (!OriginalWord.isValidLanguage(originalWordLanguage)) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }
        if (!TranslatedWord.isValidLanguage(translatedWordLanguage)) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }

        final String modelOriginalWord = originalWord;
        final String modelOriginalWordLanguage = originalWordLanguage;
        final String modelTranslatedWord = translatedWord;
        final String modelTranslatedWordLanguage = translatedWordLanguage;

        if (whenToReview == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
              Date.class.getSimpleName()));
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            ZonedDateTime.parse(whenToReview, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(INVALID_DATE_FORMAT_MESSAGE);
        }
        final Date modelWhenToReview = Date.from(ZonedDateTime.parse(whenToReview).toInstant());

        if (!ProficiencyLevel.isValidProficiencyLevel(level)) {
            throw new IllegalValueException(ProficiencyLevel.MESSAGE_CONSTRAINTS);
        }
        final int modelLevel = level;
        final Set<Tag> modelTags = new HashSet<>(flashCardTags);
        return new FlashCard(new OriginalWord(modelOriginalWord, modelOriginalWordLanguage),
            new TranslatedWord(modelTranslatedWord, modelTranslatedWordLanguage),
          modelWhenToReview, new ProficiencyLevel(modelLevel), isRemembered, modelTags);
    }
}
