package com.theeye.api.v1.chess.engine.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "stockfish")
public class StockfishProperties {

     private String path;
     private int depth;
     private int time;
}
