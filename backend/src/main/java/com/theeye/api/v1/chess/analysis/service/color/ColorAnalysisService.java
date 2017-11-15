package com.theeye.api.v1.chess.analysis.service.color;

import com.theeye.api.v1.chess.analysis.collector.RGBCollector;
import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.analysis.model.domain.TileReferenceColors;
import com.theeye.api.v1.chess.analysis.model.enumeration.TileColor;
import com.theeye.api.v1.chess.analysis.util.CoordUtil;
import com.theeye.api.v1.chess.analysis.util.TileScaler;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.analysis.model.consts.InitialColorReferenceIndexes.COLUMNS_OCCUPIED_BY_BLACK;
import static com.theeye.api.v1.chess.analysis.model.consts.InitialColorReferenceIndexes.COLUMNS_OCCUPIED_BY_WHITE;
import static com.theeye.api.v1.chess.analysis.model.consts.InitialColorReferenceIndexes.COLUMNS_UNOCCUPIED;

@Service
public class ColorAnalysisService {

     public TileReferenceColors getBlackTilesAverages(Mat preparedImage, TileCorners[][] tilesCorners) {
          Scalar occupiedByWhite = getBlackOccupiedByWhiteAverage(preparedImage, tilesCorners);
          Scalar occupiedByBlack = getBlackOccupiedByBlackAverage(preparedImage, tilesCorners);
          Scalar unoccupied = getUnoccupiedBlackAverage(preparedImage, tilesCorners);
          return TileReferenceColors.builder()
                                    .occupiedByWhite(occupiedByWhite)
                                    .occupiedByBlack(occupiedByBlack)
                                    .unoccupied(unoccupied)
                                    .build();
     }

     private Scalar getBlackOccupiedByWhiteAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromColumns(tilesCorners, TileColor.Black, COLUMNS_OCCUPIED_BY_WHITE);
          return null;
     }

     private Scalar getBlackOccupiedByBlackAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromColumns(tilesCorners, TileColor.Black, COLUMNS_OCCUPIED_BY_BLACK);
          return null;
     }

     private Scalar getUnoccupiedBlackAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromColumns(tilesCorners, TileColor.Black, COLUMNS_UNOCCUPIED);
          return null;
     }

     public TileReferenceColors getWhiteTilesAverages(Mat preparedImage, TileCorners[][] tilesCorners) {
          Scalar occupiedByWhite = getWhiteOccupiedByWhiteAverage(preparedImage, tilesCorners);
          Scalar occupiedByBlack = getWhiteOccupiedByBlackAverage(preparedImage, tilesCorners);
          Scalar unoccupied = getWhiteUnoccupiedAverage(preparedImage, tilesCorners);
          return TileReferenceColors.builder()
                                    .occupiedByWhite(occupiedByWhite)
                                    .occupiedByBlack(occupiedByBlack)
                                    .unoccupied(unoccupied)
                                    .build();
     }

     private Scalar getWhiteOccupiedByWhiteAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromColumns(tilesCorners, TileColor.White, COLUMNS_OCCUPIED_BY_WHITE);

          return null;
     }

     private Scalar getWhiteOccupiedByBlackAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromColumns(tilesCorners, TileColor.White, COLUMNS_OCCUPIED_BY_BLACK);

          return null;
     }

     private Scalar getWhiteUnoccupiedAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromColumns(tilesCorners, TileColor.White, COLUMNS_UNOCCUPIED);
          computeAverageColor(preparedImage, tiles);
          return null;
     }

     private Scalar computeAverageColor(Mat image, List<TileCorners> tiles) {
          return tiles.stream()
                      .map(tile -> getRoiSubmat(image, tile))
                      .map(this::getAverage)
                      .collect(RGBCollector.getRgbAverageCollector());
     }

     private Scalar getAverage(Mat roi) {
          return null;
     }

     public Mat getRoiSubmat(Mat src, TileCorners tile) {
          TileCorners roiCorners = limitRoi(tile, 0.25);
          Rect rect = CoordUtil.innerRect(roiCorners);
          return src.submat(rect);
     }

     public TileCorners limitRoi(TileCorners tile, double areaScale) {
          double sideScale = Math.pow(areaScale, 0.5);
          return TileScaler.computeScaledTile(tile, sideScale);
     }

     private List<TileCorners> getTilesFromColumns(TileCorners[][] tiles, TileColor tileColor, int...cols) {
          return Arrays.stream(cols)
                       .boxed()
                       .flatMap(col -> getTilesFromColumn(tiles, tileColor, col).stream())
                       .collect(Collectors.toList());
     }

     private List<TileCorners> getTilesFromColumn(TileCorners[][] tiles, TileColor tileColor, Integer col) {
          TileCorners[] column = tiles[col];
          List<TileCorners> tilesList = new LinkedList<>();
          int firstIndex = (tileColor.getOffset() + col % 2) % 2;
          for (int i = firstIndex; i < column.length; i+=2) {
               tilesList.add(column[i]);
          }
          return tilesList;
     }
}
