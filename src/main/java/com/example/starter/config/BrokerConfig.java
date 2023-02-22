package com.example.starter.config;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.Objects;

@Builder
@Value
@ToString
public class BrokerConfig {

  int serverPort;
  String version;
  DbConfig dbConfig;

  public static BrokerConfig from(final JsonObject config) {
    final Integer _serverPort = config.getInteger(ConfigLoader.SERVER_PORT);
    if (Objects.isNull(_serverPort)) {
      throw new RuntimeException(ConfigLoader.SERVER_PORT + " not configured!");
    }

    final String _version = config.getString(ConfigLoader.VERSION);
    if (Objects.isNull(_version)) {
      throw new RuntimeException(ConfigLoader.VERSION + " not configured!");
    }

    return BrokerConfig.builder()
      .serverPort(_serverPort)
      .version(_version)
      .dbConfig(new DbConfig())
      .build();
  }

}
