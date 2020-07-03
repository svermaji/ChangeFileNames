package com.sv.changefilenames;

public enum CheckboxInfo {

    SUB_FOLDER("Process sub-folders", false, "If selected process files from root and all sub-folders else only files from the root folder"),
    PROCESS_FOLDER("Process folder names also", false, "Will process folder names also and convert them to title case if selected (only for CONVERT_TO_TITLE_CASE Action)"),
    //OVERWRITE_MP3_TAG ("Overwrite MP3 tag info", true, "Process sub-folders if selected else only files of the folder"),
    APPEND_FOLDER("Append folder name", false, "If selected process files from root and all sub-folders else only files from the root folder");
    //ID3V2TTAG ("Update ID3v2Tag also", true, "Process sub-folders if selected else only files of the folder"),
    //MP3_TITLE_TAG ("Modify Title Tag", false, "Process sub-folders if selected else only files of the folder");

    String label, toolTip;
    boolean selected;

    CheckboxInfo(String label, boolean selected, String toolTip) {
        this.label = label;
        this.selected = selected;
        this.toolTip = toolTip;
    }

    public String getLabel() {
        return label;
    }

    public String getToolTip() {
        return toolTip;
    }

    public boolean isSelected() {
        return selected;
    }
}
