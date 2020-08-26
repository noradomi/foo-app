package vn.zalopay.phucvt.fooapp.util;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigUtils {
    private static final Logger LOGGER = LogManager.getLogger(ConfigUtils.class);

    public static ConfigRetriever load(Vertx vertx) {
        ConfigStoreOptions fileStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", ""));

        String extenalConfigFile = System.getProperty("vmr-config-file", "conf.yml");
        ConfigStoreOptions externalStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", extenalConfigFile));

        ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions().addStore(fileStoreOptions)
                .addStore(externalStoreOptions);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, retrieverOptions);

        return configRetriever;
    }

}
