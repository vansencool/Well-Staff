package dev.vansen.libs.fastapi.text;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class replaces text in a string with customizable features.
 */
@SuppressWarnings("unused")
public final class Replacer {
    private final String target;
    private final String replacement;
    private final boolean ignoreCase;
    private final boolean replaceAll;
    private final boolean trim;
    private final boolean removeEmptyLines;
    private final int limit;
    private final boolean prefixOnly;
    private final boolean suffixOnly;
    private final String surroundWith;
    private final boolean wholeWord;

    private Replacer(@NotNull String target, @NotNull String replacement, boolean ignoreCase, boolean replaceAll, boolean trim, boolean removeEmptyLines, int limit, boolean escapeSpecialChars, boolean prefixOnly, boolean suffixOnly, @NotNull String surroundWith, boolean wholeWord) {
        this.target = escapeSpecialChars ? Pattern.quote(target) : target;
        this.replacement = replacement;
        this.ignoreCase = ignoreCase;
        this.replaceAll = replaceAll;
        this.trim = trim;
        this.removeEmptyLines = removeEmptyLines;
        this.limit = limit;
        this.prefixOnly = prefixOnly;
        this.suffixOnly = suffixOnly;
        this.surroundWith = surroundWith;
        this.wholeWord = wholeWord;
    }

    /*
     * Creates a new Replacer instance with simplified configuration.
     *
     * @return a new ReplacerBuilder instance
     */
    public static ReplacerBuilder builder() {
        return new ReplacerBuilder();
    }

    /*
     * Replaces text in a string with the specified features.
     *
     * @param input the string to process
     * @return the modified string
     */
    public String replace(@NotNull String input) {
        String result = input;

        String regex = target;
        if (wholeWord) {
            regex = "\\b" + regex + "\\b";
        } else {
            if (prefixOnly) {
                regex = "\\b" + regex;
            }
            if (suffixOnly) {
                regex = regex + "\\b";
            }
        }

        if (ignoreCase) {
            regex = "(?i)" + regex;
        }

        result = replaceWithLimit(result, regex, surroundWith != null ? surroundWith + replacement + surroundWith : replacement);

        if (trim) {
            result = result.trim();
        }
        if (removeEmptyLines) {
            result = result.replaceAll("\\n\\s*\\n", "\n");
        }
        return result;
    }

    private String replaceWithLimit(@NotNull String input, @NotNull String regex, @NotNull String replacement) {
        if (replaceAll || limit <= 0) {
            return input.replaceAll(regex, replacement);
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();
        int count = 0;
        while (matcher.find()) {
            if (count >= limit) {
                break;
            }
            matcher.appendReplacement(result, replacement);
            count++;
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /*
     * Builds a Replacer with configurable options.
     */
    public static class ReplacerBuilder {
        private String target = "";
        private String replacement = "";
        private boolean ignoreCase;
        private boolean replaceAll;
        private boolean trim;
        private boolean removeEmptyLines;
        private int limit = 0; // default unlimited
        private boolean escapeSpecialChars;
        private boolean prefixOnly;
        private boolean suffixOnly;
        private String surroundWith;
        private boolean wholeWord;

        /*
         * Sets the target string to find.
         *
         * @param target the string to find
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder target(@NotNull String target) {
            this.target = target;
            return this;
        }

        /*
         * Sets the string to replace with.
         *
         * @param replacement the string to replace with
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder replacement(@NotNull String replacement) {
            this.replacement = replacement;
            return this;
        }

        /*
         * Configures the replacement to be case-insensitive.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder ignore() {
            this.ignoreCase = true;
            return this;
        }

        /*
         * Configures to replace all occurrences.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder all() {
            this.replaceAll = true;
            return this;
        }

        /*
         * Configures to trim leading and trailing whitespace.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder trim() {
            this.trim = true;
            return this;
        }

        /*
         * Configures to remove lines with only whitespace.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder remove() {
            this.removeEmptyLines = true;
            return this;
        }

        /*
         * Configures the maximum number of replacements to make.
         *
         * @param limit the maximum number of replacements; 0 means unlimited
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /*
         * Configures to escape special characters in the target string.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder escape() {
            this.escapeSpecialChars = true;
            return this;
        }

        /*
         * Configures to replace only if the target string is at the beginning of a word.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder prefixOnly() {
            this.prefixOnly = true;
            return this;
        }

        /*
         * Configures to replace only if the target string is at the end of a word.
         *
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder suffixOnly() {
            this.suffixOnly = true;
            return this;
        }

        /*
         * Configures to surround each occurrence with a specified string.
         *
         * @param surroundWith the string to surround each occurrence with
         * @return the builder instance
         */
        @CanIgnoreReturnValue
        public ReplacerBuilder surround(@NotNull String surroundWith) {
            this.surroundWith = surroundWith;
            return this;
        }

        /*
         * Configures to replace only whole word occurrences of the target string.
         *
         * @return the builder instance
         */
        public ReplacerBuilder wholeWord() {
            this.wholeWord = true;
            return this;
        }

        /*
         * Builds the Replacer instance.
         *
         * @return the Replacer instance
         */
        public Replacer build() {
            return new Replacer(target, replacement, ignoreCase, replaceAll, trim, removeEmptyLines, limit, escapeSpecialChars, prefixOnly, suffixOnly, surroundWith, wholeWord);
        }
    }
}