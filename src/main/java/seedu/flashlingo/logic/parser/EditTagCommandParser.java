package seedu.flashlingo.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.flashlingo.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.flashlingo.logic.parser.CliSyntax.PREFIX_ORIGINAL_TAG;
import static seedu.flashlingo.logic.parser.CliSyntax.PREFIX_REPLACED_TAG;

import seedu.flashlingo.commons.core.index.Index;
import seedu.flashlingo.logic.commands.EditTagCommand;
import seedu.flashlingo.logic.parser.exceptions.ParseException;
import seedu.flashlingo.model.tag.Tag;

import java.util.stream.Stream;

/**
 * Parses input arguments and creates a new EditCommandParser object
 */
public class EditTagCommandParser implements Parser<EditTagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @param args input String to be parsed
     * @return EditTagCommand generated as a result of the parsing
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ORIGINAL_TAG, PREFIX_REPLACED_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE), pe);
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_ORIGINAL_TAG, PREFIX_REPLACED_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE));
        }

        Tag originalTag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_ORIGINAL_TAG).get());
        Tag replacedTag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_REPLACED_TAG).get());

        return new EditTagCommand(index, originalTag, replacedTag);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
