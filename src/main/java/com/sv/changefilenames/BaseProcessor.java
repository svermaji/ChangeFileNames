package com.sv.changefilenames;

import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;

import static com.sv.core.Constants.SP_DASH_SP;

/**
 * Created by 44085037 on 16-Oct-17
 */
public abstract class BaseProcessor {

    private final MyLogger logger;

    public BaseProcessor(MyLogger logger) {
        this.logger = logger;
    }

    private void printParameters(Arguments args) {
        printParameters(args, true);
    }

    private void printParameters(Arguments args, boolean isInitMsg) {
        logger.info(this.getClass().getName() + SP_DASH_SP
            + (isInitMsg ? "Initialising" : "Finishing") + " process with parameters: fileName ["
            + ((args.getFile() != null) ? args.getFile().getName() : "") + "] and [" + args.toString() + "]");
    }

    public String execute(Arguments args) {
        printParameters(args);
        String result = process(args);
        printParameters(args, false);
        return result;
    }

    public void log(String msg) {
        logger.info(this.getClass().getName() + SP_DASH_SP + msg);
    }

    public void checkParamForEmpty (String param, String errMsg) {
        if (!Utils.hasValue(param)) {
            log(errMsg);
            throw new ChangeFileNamesException(errMsg);
        }
    }

    protected abstract String process(Arguments args);

}
