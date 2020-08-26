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
        Future<JsonObject> future = Future.future();

//        Get UserInfo from request
        final User user = Json.decodeValue(requestJson, User.class);

        Future<User> getUserAuth = Future.future();
        getUserAuth.complete(new User("1","phucvt","123","noradomi"));

        getUserAuth.compose(userAuth -> {
            if(userAuth != null){
//                **: Chua ma hoa password bang BCrypt
                if(user.getPassword().equals(userAuth.getPassword())){
                    String jwt = jwtManager.generateToken(userAuth.getUserId());

//                    Create JsonObject response
                    JsonObject obj = new JsonObject();
                    obj.put("jwt",jwt);
                    obj.put("userId",userAuth.getUserId());

//                    Config LogUtils
                    LOGGER.info("Sign in granted with username: "+userAuth.getUserName());

                }
                else{
                    LOGGER.info("Invalid Username or Password");
                }

            }
            LOGGER.info("UserAuth empty");
        }, Future.future().setHandler(handler -> {
            LOGGER.info("Sign in failed");
            future.fail(handler.cause());
        }));
        return future;
    }
}
