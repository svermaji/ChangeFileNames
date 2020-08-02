package com.sv.changefilenames;

/**
 * Created by 44085037 on 16-Oct-17
 */
public class ConvertToSentenceCase extends BaseProcessor {

    public ConvertToSentenceCase(MyLogger logger) {
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
        String[] parts = fileName.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append((""+part.charAt(0)).toUpperCase());
            if  (part.length() > 1) {
                sb.append(part.substring(1));
            }
            sb.append(" ");
        }

        return sb.toString();
    }

}
