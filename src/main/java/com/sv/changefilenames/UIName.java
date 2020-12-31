package com.sv.changefilenames;

public enum UIName {

    LBL_FOLDER("Folder", 'F', "Folder"),
    LBL_ACTION("Action", 'A', "Select action"),
    LBL_LOADING("Loading", 'L', "Loading, please wait"),
    LBL_P1("Param1", '1', "Parameter - 1st"),
    LBL_P2("Param2", '2', "Parameter - 2nd"),
    LBL_EXTN("Extension", 'E', "Extension of files to be processed"),
    BTN_PRVW("Preview", 'P', "Preview changes that will be applied"),
    BTN_CHNG("Change", 'C', "Apply changes"),
    BTN_BROWSE("Browse", 'B', "Browse folder"),
    BTN_CLEAR("Clear Status", 'S', "Clear status area"),
    BTN_USG("Usage", 'U', "See usage of application");

    String name, tip;
    char mnemonic;

    UIName(String name, char mnemonic) {
        this(name, mnemonic, null);
    }

    UIName(String name, char mnemonic, String tip) {
        this.name = name;
        this.tip = tip;
        this.mnemonic = mnemonic;
    }

}
