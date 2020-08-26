package vn.zalopay.phucvt.fooapp.service;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.model.User;

public class WebService extends BaseService{
    private static final Logger LOGGER = LogManager.getLogger(WebService.class);

    public Future<JsonObject> signIn(String requestJson){
        LOGGER.info("WebService handling ...");
        Future<JsonObject> future = Future.future();

//        Get UserInfo from request
//        final User user = Json.decodeValue(requestJson, User.class);

//        LOGGER.info("User: {}",user.toString());

        Future<User> getUserAuth = Future.future();
        getUserAuth.complete(new User("1","phucvt","123","noradomi"));

        getUserAuth.compose(userAuth -> {
//            if(userAuth != null){
            if(true){
//                **: Chua ma hoa password bang BCrypt
//                if(userAuth.getPassword().equals(userAuth.getPassword())){
                if(true){
//                    String jwt = jwtManager.generateToken(userAuth.getUserId());
                    String jwt = jwtManager.generateToken("1");

//                    Create JsonObject response
                    JsonObject obj = new JsonObject();
                    obj.put("jwt",jwt);
                    obj.put("userId",userAuth.getUserId());

//                    Config LogUtils
//                    LOGGER.info("Sign in granted with username: "+userAuth.getUserName());
                    LOGGER.info("Sign in granted with username: ");

                }
                else{
                    LOGGER.info("Invalid Username or Password");
                }

            }

        }, Future.future().setHandler(handler -> {
            LOGGER.info("Sign in failed");
            future.fail(handler.cause());
        }));
        return future;
    }
}
