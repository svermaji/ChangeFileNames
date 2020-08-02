package com.sv.changefilenames;

public enum ChoiceInfo {

    REMOVE_NUMBERS_FROM_FILE_NAMES("Remove numbers from file names", "RemoveNumbersFromFileNames"),
    REMOVE_NUMBERS_FROM_START("Remove numbers from start", "RemoveNumbersFromStart"),
    REMOVE_NUMBERS_FROM_END("Remove numbers from end", "RemoveNumbersFromEnd"),
    APPEND_STRING_IN_START("Append string in start", "AppendStringInStart"),
    APPEND_STRING_IN_END("Append string in end", "AppendStringInEnd"),
    REMOVE_CHARS_FROM_START("Remove chars from start", "RemoveCharsFromStart"),
    REMOVE_CHARS_FROM_END("Remove chars from end", "RemoveCharsFromEnd"),
    REMOVE_SPACES_FROM_START("Remove spaces from start", "RemoveSpacesFromStart"),
    REMOVE_SPACES_FROM_END("Remove spaces from end", "RemoveSpacesFromEnd"),
    REMOVE_SPACES_FROM_BOTH_SIDES("Remove spaces from both sides", "RemoveSpacesFromBothSides"),
    CONVERT_TO_TITLE_CASE("Convert to title case", "ConvertToTitleCase"),
    CONVERT_TO_SENTENCE_CASE("Convert to title case", "ConvertToTitleCase"),
    REMOVE_MATCH_FROM_START("Remove match from start", "RemoveMatchFromStart"),
    REMOVE_MATCH_FROM_END("Remove match from end", "RemoveMatchFromEnd"),
    REMOVE_MATCH("Remove match", "RemoveMatch"),
    REPLACE_MATCH_FROM_START("Replace match from start", "ReplaceMatchFromStart"),
    REPLACE_MATCH_FROM_END("Replace match from end", "ReplaceMatchFromEnd"),
    REPLACE_MATCH("Replace match", "ReplaceMatch");

    private final String label, value;

    ChoiceInfo(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return getLabel();
    }
}

class ChoicesComparator implements java.util.Comparator<ChoiceInfo> {
    public int compare(ChoiceInfo left, ChoiceInfo right) {
        return left.getLabel().compareTo(right.getLabel());
    }
}
