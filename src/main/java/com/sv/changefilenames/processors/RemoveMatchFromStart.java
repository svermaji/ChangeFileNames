package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.core.logger.MyLogger;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveMatchFromStart extends BaseProcessor {

    public RemoveMatchFromStart(MyLogger logger) {
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

        String str = args.getFileNameNoExtn();

        if (str.startsWith(args.getParam1())) {
            str = str.substring(args.getParam1().length());
        }

        return str;
    }

}
