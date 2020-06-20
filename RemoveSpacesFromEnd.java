/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveSpacesFromEnd extends BaseProcessor {

    public RemoveSpacesFromEnd(MyLogger logger) {
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

        String fn = new StringBuilder(args.getFileNameNoExtn()).reverse().toString();
        int c;
        // Till > 0 so 1 character should be left if all are spaces
        for (c = 0; c < fn.length(); c++) {
            if (fn.charAt(c) != ' ') {
                break;
            }
        }
        return new StringBuilder(fn.substring(c)).reverse().toString();
    }

}
