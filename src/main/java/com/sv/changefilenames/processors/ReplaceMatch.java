package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.core.logger.MyLogger;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class ReplaceMatch extends BaseProcessor {

    public ReplaceMatch(MyLogger logger) {
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
                "Parameter explaining which string to replace is null.");
        checkParamForEmpty(args.getParam2(),
                "Parameter explaining what string to replace is null.");

        String str = args.getFileNameNoExtn();
        int idx = str.indexOf(args.getParam1());
        int len = args.getParam1().length();
        while (idx >= 0) {
            str = str.substring(0, idx) + args.getParam2() + str.substring(idx + len);
            idx = str.indexOf(args.getParam1());
        }

        return str;
    }

}
