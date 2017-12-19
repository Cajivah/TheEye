package com.theeye.api.v1.chess.image.analysis.service.color;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.image.analysis.collector.RGBCollector;
import com.theeye.api.v1.chess.image.analysis.model.consts.InitialColorReferenceIndexes;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.TileColor;
import com.theeye.api.v1.chess.image.analysis.util.CoordUtil;
import com.theeye.api.v1.chess.image.analysis.util.TileScaler;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
                  getTilesFromRow(tilesCorners, TileColor.BLACK, InitialColorReferenceIndexes.ROWS_OCCUPIED_BY_WHITE);
          return computeAverageColor(preparedImage, tiles);
     }

     private Scalar getBlackOccupiedByBlackAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromRow(tilesCorners, TileColor.BLACK, InitialColorReferenceIndexes.ROWS_OCCUPIED_BY_BLACK);
          return computeAverageColor(preparedImage, tiles);
     }

     private Scalar getUnoccupiedBlackAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromRow(tilesCorners, TileColor.BLACK, InitialColorReferenceIndexes.ROWS_UNOCCUPIED);
          return computeAverageColor(preparedImage, tiles);
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
                  getTilesFromRow(tilesCorners, TileColor.WHITE, InitialColorReferenceIndexes.ROWS_OCCUPIED_BY_WHITE);

          return computeAverageColor(preparedImage, tiles);
     }

     private Scalar getWhiteOccupiedByBlackAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromRow(tilesCorners, TileColor.WHITE, InitialColorReferenceIndexes.ROWS_OCCUPIED_BY_BLACK);

          return computeAverageColor(preparedImage, tiles);
     }

     private Scalar getWhiteUnoccupiedAverage(Mat preparedImage, TileCorners[][] tilesCorners) {
          List<TileCorners> tiles =
                  getTilesFromRow(tilesCorners, TileColor.WHITE, InitialColorReferenceIndexes.ROWS_UNOCCUPIED);
          return computeAverageColor(preparedImage, tiles);
     }

     private Scalar computeAverageColor(Mat image, List<TileCorners> tiles) {
          return tiles.stream()
                      .map(tile -> computeAverageColor(image, tile))
                      .collect(RGBCollector.getRgbAverageCollector());
     }

     private Scalar computeAverageColor(Mat image, TileCorners tile) {
          Mat roi = getRoiSubmat(image, tile);
          return getAverage(roi);
     }

     private Scalar getAverage(Mat roi) {
          return Core.mean(roi);
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

     private List<TileCorners> getTilesFromRow(TileCorners[][] tiles, TileColor tileColor, int... rows) {
          return Arrays.stream(rows)
                       .boxed()
                       .flatMap(row -> getTilesFromRow(tiles, tileColor, row).stream())
                       .collect(Collectors.toList());
     }

     private List<TileCorners> getTilesFromRow(TileCorners[][] tiles, TileColor tileColor, Integer row) {
          TileCorners[] tilesRow = tiles[row];
          List<TileCorners> tilesList = new LinkedList<>();
          int firstIndex = (tileColor.getOffset() + row % 2) % 2;
          for (int i = firstIndex; i < tilesRow.length; i += 2) {
               tilesList.add(tilesRow[i]);
          }
          return tilesList;
     }

     public Scalar[][] getTilesColorsInPlay(Mat image, TileCorners[][] tilesCorners) {
          Scalar[][] tilesColors = new Scalar[BoardConsts.ROWS][BoardConsts.COLUMNS];
          for (int i = 0; i < BoardConsts.ROWS; ++i) {
               TileCorners[] tilesRow = tilesCorners[i];
               Scalar[] colorsRow = tilesColors[i];
               for (int j = 0; j < BoardConsts.COLUMNS; ++j) {
                    TileCorners tileCorners = tilesRow[j];
                    Scalar colors = computeAverageColor(image, tileCorners);
                    colorsRow[j] = colors;
               }
          }
          return tilesColors;
     }
}
