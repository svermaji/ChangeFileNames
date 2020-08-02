package com.sv.changefilenames;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveNumbersFromStart extends BaseProcessor {

    public RemoveNumbersFromStart(MyLogger logger) {
        super(logger);
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param args Object of type Arguments
     * @return String modified file name
     */
    @Override
    protected String process(Arguments args) {
        String filename = args.getFileNameNoExtn();
        int idx = 0;
        for (int c = 0; c < filename.length(); c++) {
            char ch = filename.charAt(c);
            if (Utils.isNumeric(ch)) {
                idx = c+1;
            } else {
                break;
            }
        }
        if (idx == filename.length()) {
            return "";
        }
        return filename.substring(idx);
    }

}
