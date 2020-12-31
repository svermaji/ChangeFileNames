package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveNumbersFromEnd extends BaseProcessor {

    public RemoveNumbersFromEnd(MyLogger logger) {
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
        int idx = filename.length();
        for (int c = filename.length() - 1; c > 0; c--) {
            char ch = filename.charAt(c);
            if (Utils.isNumeric(ch)) {
                idx = c;
            } else {
                break;
            }
        }
        if (idx == 0) {
            return "";
        }
        return filename.substring(0, idx);
    }

}
