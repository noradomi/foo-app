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

    public static JsonObject getConfig(String field) {
        Vertx vertx = Vertx.vertx();
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

        JsonObject jsonObject = Future.future(configRetriever::getConfig).result();
        return jsonObject.getJsonObject(field);
    }

//    public static JsonObject getConfig(Vertx vertx, String field) {
//        final JsonObject[] res = new JsonObject[1];
//        load(vertx).getConfig((jsonResult) -> {
//            return (JsonObject) jsonResult.result();
//        });
//
////        {
////            if (jsonResult.succeeded()) {
////                LOGGER.info("Load configuration successfully");
////                res[0] = jsonResult.result();
////            } else {
////                LOGGER.info("Error when load configuration", jsonResult.cause());
////                vertx.close();
////                res[0] = null;
////            }
////        });
////        return res[0].getJsonObject(field);
////    }
//    }
}
