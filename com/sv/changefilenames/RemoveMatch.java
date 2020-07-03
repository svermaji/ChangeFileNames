package com.sv.changefilenames;

/**
 *
 */
public class RemoveMatch extends BaseProcessor {

    public RemoveMatch(MyLogger logger) {
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
                "Parameter explaining which string to remove is null.");

        String str = args.getFileNameNoExtn();
        int idx = str.indexOf(args.getParam1());
        int len = args.getParam1().length();
        while (idx > 0) {
            str = str.substring(0, idx) + str.substring(idx+len);
            idx = str.indexOf(args.getParam1());
        }
        return str;
    }

}
