package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;

import static com.sv.core.Constants.EMPTY;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveNumbersFromFileNames extends BaseProcessor {

    public RemoveNumbersFromFileNames(MyLogger logger) {
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
        StringBuilder sb = new StringBuilder(EMPTY);
        for (int c = 0; c < args.getFile().getName().length(); c++) {
            char ch = args.getFile().getName().charAt(c);
            if (!Utils.isNumeric(ch))
                sb.append(ch);
        }
        return sb.toString();
    }

}
