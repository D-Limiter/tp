package seedu.flashlingo.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_ORIGINAL_WORD = new Prefix("w/");
    public static final Prefix PREFIX_ORIGINAL_WORD_LANGUAGE = new Prefix("wl/");
    public static final Prefix PREFIX_TRANSLATED_WORD = new Prefix("t/");
    public static final Prefix PREFIX_TRANSLATED_WORD_LANGUAGE = new Prefix("tl/");
    public static final Prefix PREFIX_TAG = new Prefix("tg/");
    public static final Prefix PREFIX_ORIGINAL_TAG = new Prefix("ot/");
    public static final Prefix PREFIX_REPLACED_TAG = new Prefix("rt/");

}
