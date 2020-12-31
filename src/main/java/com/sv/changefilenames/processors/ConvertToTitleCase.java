package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.core.logger.MyLogger;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class ConvertToTitleCase extends BaseProcessor {

    public ConvertToTitleCase(MyLogger logger) {
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
        String fileName = args.getFile().getName();
        fileName = fileName.toLowerCase();
        if (fileName.length() > 1) {
            fileName = (fileName.charAt(0)+"").toUpperCase() + fileName.substring(1);
        } else {
            fileName = (fileName.charAt(0)+"").toUpperCase();
        }

        return fileName;
    }

}
