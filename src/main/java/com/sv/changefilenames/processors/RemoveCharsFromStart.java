package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.changefilenames.ChangeFileNamesException;
import com.sv.core.logger.MyLogger;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveCharsFromStart extends BaseProcessor {

    public RemoveCharsFromStart(MyLogger logger) {
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
        checkParamForEmpty(args.getParam1(),
                "Parameter explaining how many characters to remove is null.");

        int numChars;
        String fileName = args.getFileNameNoExtn();
        try {
            numChars = Integer.parseInt(args.getParam1());
            if (numChars >= fileName.length()) {
                log("***Number of characters exceeding file name length.");
                numChars = 0;
            }
        } catch (NumberFormatException e) {
            String msg = "Invalid parameter, cannot convert [" + args.getParam1() + "] to number";
            log(msg);
            throw new ChangeFileNamesException(msg, e);
        }

        return fileName.substring(numChars);
    }

}
