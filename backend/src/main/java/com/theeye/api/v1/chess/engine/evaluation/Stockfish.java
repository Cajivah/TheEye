package com.theeye.api.v1.chess.engine.evaluation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish {

     private BufferedReader processReader;
     private OutputStreamWriter processWriter;

     private static final String SET_FEN_CMD = "position fen ";
     private static final String GO_MOVETIME_CMD = "go movetime ";
     private static final String ISREADY_CMD = "isready";
     private static final String READY_PATTERN = "readyok";
     private static final String QUIT_CMD = "quit";
     private static final String DRAW_BOARD_CMD = "d";
     private static final int SAFE_TIME_MARGIN = 100;
     private static final String BEST_MOVE_PATTERN = "bestmove ";
     private static final String SCORE_CP_PATTERN = "score cp ";
     private static final String INFO_DEPTH_PATTERN = "info depth ";
     private static final String NODES_PATTERN = " nodes";
     private static final String UPPERBOUND_NODES_PATTERN = " upperbound nodes";
     private static final String DELIMITER = " ";
     private static final String ENDLINE = "\n";

     public Stockfish startEngine(String path) throws IOException {
          Process engineProcess = Runtime.getRuntime().exec(path);
          processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
          processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
          return this;
     }

     public String getBestMove(String fen, int waitTime) throws IOException, InterruptedException {
          sendCommand(SET_FEN_CMD + fen);
          sendCommand(GO_MOVETIME_CMD + waitTime);
          return getOutput(waitTime + SAFE_TIME_MARGIN)
                  .split(BEST_MOVE_PATTERN)[1]
                  .split(DELIMITER)[0];
     }

     private void sendCommand(String command) throws IOException {
          processWriter.write(command + ENDLINE);
          processWriter.flush();
     }

     private String getOutput(int waitTime) throws InterruptedException, IOException {
          StringBuilder buffer = new StringBuilder();
          Thread.sleep(waitTime);
          sendCommand(ISREADY_CMD);
          while (true) {
               String text = processReader.readLine();
               if (text.equals(READY_PATTERN)) {
                    break;
               } else {
                    buffer.append(text)
                          .append(ENDLINE);
               }
          }
          return buffer.toString();
     }

     public void stopEngine() throws IOException {
          sendCommand(QUIT_CMD);
          processReader.close();
          processWriter.close();
     }

     public void drawBoard(String fen) throws IOException, InterruptedException {
          sendCommand(SET_FEN_CMD + fen);
          sendCommand(DRAW_BOARD_CMD);

          String[] rows = getOutput(0).split(ENDLINE);

          for (int i = 1; i < 18; i++) {
               System.out.println(rows[i]);
          }
     }

     public float getEvalScore(String fen, int waitTime) throws IOException, InterruptedException {
          sendCommand(SET_FEN_CMD + fen);
          sendCommand(GO_MOVETIME_CMD + waitTime);

          float evalScore = 0.0f;
          String[] dump = getOutput(waitTime + SAFE_TIME_MARGIN).split(ENDLINE);
          for (int i = dump.length - 1; i >= 0; i--) {
               if (dump[i].startsWith(INFO_DEPTH_PATTERN)) {
                    try {
                         evalScore = Float.parseFloat(dump[i].split(SCORE_CP_PATTERN)[1]
                                 .split(NODES_PATTERN)[0]);
                    } catch(Exception e) {
                         evalScore = Float.parseFloat(dump[i].split(SCORE_CP_PATTERN)[1]
                                 .split(UPPERBOUND_NODES_PATTERN)[0]);
                    }
               }
          }
          return evalScore/100;
     }

     public float getEvalScoreAndClose(String fenDescription, int time) throws IOException, InterruptedException {
          float evalScore = this.getEvalScore(fenDescription, time);
          this.stopEngine();
          return evalScore;
     }

     public String getBestMoveAndClose(String fen, int time) throws IOException, InterruptedException {
          String bestMove = this.getBestMove(fen, time);
          this.stopEngine();
          return bestMove;
     }
}

