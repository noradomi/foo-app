package vn.zalopay.phucvt.fooapp.manager;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.KeyStoreOptionsConverter;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import lombok.Data;
import vn.zalopay.phucvt.fooapp.FooApp;

@Data
public class JWTManager {
    private JWTAuth authProvider;
    private JWTOptions jwtOptions;

    public JWTManager(Vertx vertx){
        authProvider = JWTAuth.create(vertx,new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setPublicKey("dragon")
                        .setSymmetric(true))
        );

        jwtOptions = new JWTOptions()
                .setIssuer("Noradomi")
                .addAudience("User")
                .setExpiresInSeconds(172800);
    }

//    Function generate a JWT token and put 'userId' in payload
    public String generateToken(String userId){
        JsonObject userObj = new JsonObject().put("userId",userId );
        return authProvider.generateToken(userObj,jwtOptions);
    }
}
