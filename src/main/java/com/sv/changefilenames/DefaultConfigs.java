package com.sv.changefilenames;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DefaultConfigs {

    enum Config {
        ACTION ("default-action"),
        LOCATION ("default-location"),
        PARAM1 ("default-param1"),
        PARAM2 ("default-param2"),
        EXTENSION ("default-extension");

        String val;
        Config (String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    String propFileName = "C:\\sv\\JavaPrgs\\Utilities\\src\\com\\sv\\changefilenames\\cfn.config";
    private final Properties configs = new Properties();
    private MyLogger logger;

    public DefaultConfigs(MyLogger logger) {
        this.logger = logger;
        initialize();
    }

    public void initialize() {
        readConfig();
    }

    public String getConfig(Config config) {
        if (configs.containsKey(config.getVal()))
            return configs.getProperty(config.getVal());
        return Utils.EMPTY;
    }

    private void readConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            configs.load(is);
        } catch (Exception e) {
            logger.log ("Error in loading properties via class loader, trying file path.");
            try (InputStream is = Files.newInputStream(Paths.get(propFileName))) {
                configs.load(is);
            } catch (IOException ioException) {
                logger.log ("Error in loading properties via file path.");
            }
        }
    }

    public void saveConfig(ChangeFileNames cfn) {
        logger.log ("Saving properties.");
        configs.clear();
        configs.put(Config.ACTION.getVal(), cfn.getAction());
        configs.put(Config.LOCATION.getVal(), cfn.getLocationVal());
        configs.put(Config.PARAM1.getVal(), cfn.getTxtParam1());
        configs.put(Config.PARAM2.getVal(), cfn.getTxtParam2());
        configs.put(Config.EXTENSION.getVal(), cfn.getTxtExt());
        logger.log ("Config is " + configs);
        try {
            configs.store(new FileOutputStream(propFileName), null);
        } catch (IOException e) {
            logger.log ("Error in saving properties.");
        }
    }

}
