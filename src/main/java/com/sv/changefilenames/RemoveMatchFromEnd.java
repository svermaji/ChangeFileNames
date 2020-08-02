package com.sv.changefilenames;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveMatchFromEnd extends BaseProcessor {

    public RemoveMatchFromEnd(MyLogger logger) {
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
        String strRev = new StringBuilder(str).reverse().toString();
        String param1Rev = new StringBuilder(args.getParam1()).reverse().toString();

        if (strRev.startsWith(param1Rev)) {
            strRev = strRev.substring(param1Rev.length());
        }

        return new StringBuilder(strRev).reverse().toString();
    }

}
