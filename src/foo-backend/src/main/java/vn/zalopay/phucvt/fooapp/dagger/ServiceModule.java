package vn.zalopay.phucvt.fooapp.dagger;

import vn.zalopay.phucvt.fooapp.cache.*;
import vn.zalopay.phucvt.fooapp.config.ServiceConfig;
import vn.zalopay.phucvt.fooapp.da.*;
import vn.zalopay.phucvt.fooapp.handler.*;
import vn.zalopay.phucvt.fooapp.server.RestfulAPI;
import vn.zalopay.phucvt.fooapp.server.WebSocketServer;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.Tracker;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.da.*;

import javax.inject.Singleton;
import java.io.IOException;

@Module
@Builder
public class ServiceModule {
    ServiceConfig serviceConfig;

    @Provides
    @Singleton
    HandlerFactory provideHandler(
            EchoHandler echoHandler,
            ExampleHandler exampleHandler,
            LoginHandler loginHandler,
            SignUpHandler signUpHandler,
            SignOutHandler signOutHandler) {
        return HandlerFactory.builder()
                .echoHandler(echoHandler)
                .exampleHandler(exampleHandler)
                .loginHandler(loginHandler)
                .signUpHandler(signUpHandler)
                .signOutHandler(signOutHandler)
                .build();
    }

    @Provides
    @Singleton
    AsyncHandler ProvideAsyncHandler(Vertx vertx) {
        return AsyncHandler.builder().vertx(vertx).build();
    }

    @Provides
    @Singleton
    RedisCache provideRedisCache() {
        try {
            return RedisCache.newInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    UserCache provideUserCache(RedisCache redisCache, AsyncHandler asyncHandler) {
        return UserCacheImpl.builder().redisCache(redisCache).asyncHandler(asyncHandler).build();
    }

    @Provides
    @Singleton
    BlackListCache provideBlackListCache(RedisCache redisCache, AsyncHandler asyncHandler) {
        return BlackListCacheImpl.builder().redisCache(redisCache).asyncHandler(asyncHandler).build();
    }

    @Provides
    @Singleton
    EchoHandler provideEchoHandler() {
        return new EchoHandler();
    }

    @Provides
    @Singleton
    UserDA provideUserDA(DataSourceProvider dataSourceProvider, AsyncHandler asyncHandler) {
        return new UserDAImpl(
                dataSourceProvider.getDataSource(serviceConfig.getMySQLConfig()), asyncHandler);
    }

    @Provides
    @Singleton
    ExampleHandler provideExampleHandler(
            UserDA userDA, TransactionProvider transactionProvider, UserCache userCache) {
        return new ExampleHandler(userDA, userCache, transactionProvider);
    }

    @Provides
    @Singleton
    SignUpHandler provideSignUpHandler(
            UserDA userDA, TransactionProvider transactionProvider, UserCache userCache) {
        return new SignUpHandler(userCache,userDA, transactionProvider);
    }

    @Provides
    @Singleton
    LoginHandler provideLoginHandler(
            UserDA userDA,UserCache userCache,JWTAuth authProvider ) {
        return new LoginHandler(userDA, userCache, authProvider);
    }

    @Provides
    @Singleton
    SignOutHandler provideSignOutHanler(
            BlackListCache blackListCache) {
        return new SignOutHandler(blackListCache);
    }

    @Singleton
    @Provides
    DataSourceProvider provideDataSourceProvider(Vertx vertx) {
        return new DataSourceProviderImpl(vertx);
    }

    @Singleton
    @Provides
    TransactionProvider provideTransactionProvider(DataSourceProvider dataSourceProvider) {
        return new TransactionProviderImpl(
                dataSourceProvider.getVertxDataSource(serviceConfig.getMySQLConfig()));
    }

    @Provides
    @Singleton
    Vertx provideVertx() {
        return Vertx.vertx(
                new VertxOptions()
                        .setPreferNativeTransport(true)
                        .setMetricsOptions(
                                new MicrometerMetricsOptions()
                                        .setMicrometerRegistry(Tracker.getMeterRegistry())
                                        .setJvmMetricsEnabled(true)
                                        .setPrometheusOptions(
                                                new VertxPrometheusOptions()
                                                        .setEnabled(true)
                                                        .setStartEmbeddedServer(true)
                                                        .setPublishQuantiles(true)
                                                        .setEmbeddedServerOptions(
                                                                new HttpServerOptions()
                                                                        .setPort(serviceConfig.getVertxConfig().getPrometheusPort()))
                                                        .setEmbeddedServerEndpoint("/metrics"))
                                        .setEnabled(true)));
    }

    @Provides
    @Singleton
    JWTAuthOptions provideJWTAuthOptions() {
        // TODO: add key store
        return new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setPublicKey("dragon")
                        .setSymmetric(true));
    }

    @Provides
    @Singleton
    JWTAuth provideAuthProvider(Vertx vertx, JWTAuthOptions authConfig) {
        return JWTAuth.create(vertx, authConfig);
    }

    @Provides
    @Singleton
    RestfulAPI provideRestfulAPI(HandlerFactory handlerFactory, Vertx vertx, JWTAuth authProvider) {
        return RestfulAPI.builder()
                .vertx(vertx)
                .authProvider(authProvider)
                .handlerFactory(handlerFactory)
                .port(serviceConfig.getPort())
                .build();
    }

    @Provides
    @Singleton
    WSHandler provideWSHandler() {
        return new WSHandler();
    }

    @Provides
    @Singleton
    WebSocketServer provideWebSocketServer(WSHandler wsHandler, Vertx vertx) {
        return new WebSocketServer(wsHandler, vertx, serviceConfig.getWsPort());
    }
}
