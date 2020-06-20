import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DefaultConfigs {

    enum Config {
        ACTION ("default-action"),
        LOCATION ("default-location"),
        PARAM1 ("default-param1"),
        EXTENSION ("default-extension");

        String val;
        Config (String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }
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
        String propFileName = "cfn.config";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            configs.load(is);
        } catch (IOException e) {
            logger.log ("Error in loading properties.");
        }
    }

}
