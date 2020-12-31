package com.sv.changefilenames.processors;

import com.sv.changefilenames.Arguments;
import com.sv.changefilenames.BaseProcessor;
import com.sv.core.logger.MyLogger;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class ReplaceMatchFromEnd extends BaseProcessor {

    public ReplaceMatchFromEnd(MyLogger logger) {
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
        String strRev = new StringBuilder(str).reverse().toString();
        String param1Rev = new StringBuilder(args.getParam1()).reverse().toString();
        String param2Rev = new StringBuilder(args.getParam2()).reverse().toString();

        if (strRev.startsWith(param1Rev)) {
            strRev = param2Rev + strRev.substring(args.getParam1().length());
        }

        return new StringBuilder(strRev).reverse().toString();
    }

}
