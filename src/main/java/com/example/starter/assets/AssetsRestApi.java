package com.example.starter.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {

//  public static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);
  public static final String PATH_ASSETS = "/assets";

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "NFLX", "TSLA", "NEUR");

  public static void attach(Router parent) {

    parent.get(PATH_ASSETS).handler(new GetAssetsHandler());
  }
}
