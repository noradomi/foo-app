package vn.zalopay.phucvt.fooapp.util;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ConfigLoader {
    public static ConfigRetriever load(Vertx vertx){
        ConfigStoreOptions fileStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path",""));

        String extenalConfigFile = System.getProperty("vmr-config-file","conf.yml");
        ConfigStoreOptions externalStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path",extenalConfigFile));

        ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions().addStore(fileStoreOptions)
                .addStore(externalStoreOptions);

        return ConfigRetriever.create(vertx,retrieverOptions);
    }
}
