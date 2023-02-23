package com.example.starter.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ConfigLoader {

  public static final String VERSION = "version";
  private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
  public static final String SERVER_PORT = "SERVER_PORT";
  public static final String CONFIG_FILE = "application.yml";

  public static final String DB_HOST = "DB_HOST";
  public static final String DB_DATABASE = "DB_DATABASE";
  public static final String DB_PORT = "DB_PORT";
  public static final String DB_USER = "DB_USER";
  public static final String DB_PASSWORD = "DB_PASSWORD";
  public static final List<String> EXPOSED_ENVIRONMENT_VARIABLES = Arrays
    .asList(SERVER_PORT, DB_HOST, DB_USER, DB_DATABASE, DB_PASSWORD, DB_PORT);


  public static Future<BrokerConfig> load(Vertx vertx) {

    final var exposedKeys = new JsonArray();
    EXPOSED_ENVIRONMENT_VARIABLES.forEach(exposedKeys::add);
    LOG.debug("Fetch configuration for {}", exposedKeys.encode());

    var envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("keys", exposedKeys));

    var propertyStore = new ConfigStoreOptions()
      .setType("sys")
      .setConfig(new JsonObject().put("cache", false));

    var yamlStore = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject().put("path", CONFIG_FILE));

    var retriever = ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions()
        // Order defines overload rules
        .addStore(yamlStore) // yamlStore on top
        .addStore(propertyStore)
        .addStore(envStore) // SERVER_PORT=8888

    );

    return retriever.getConfig().map(BrokerConfig::from);
  }
}
