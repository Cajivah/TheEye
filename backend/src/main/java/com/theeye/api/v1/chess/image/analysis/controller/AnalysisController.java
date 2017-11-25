package com.theeye.api.v1.chess.image.analysis.controller;

import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import com.theeye.api.v1.chess.image.analysis.mapper.ColorMapper;
import com.theeye.api.v1.chess.image.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.image.analysis.mapper.ImageMapper;
import com.theeye.api.v1.chess.image.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.image.analysis.model.dto.ChessboardPositionFeaturesDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.PreprocessedChessboardImageDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.ReferenceColorsDTO;
import com.theeye.api.v1.chess.image.analysis.service.AnalysisService;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.theeye.api.v1.common.util.SaveToFile.save;


@RestController
@RequestMapping("/api/v1/chess/analysis")
public class AnalysisController {

     private final AnalysisService analysisService;

     private final CoordsMapper coordsMapper;

     private final ImageMapper imageMapper;

     private final ColorMapper colorMapper;

     @Autowired
     public AnalysisController(AnalysisService analysisService,
                               CoordsMapper coordsMapper,
                               ImageMapper imageMapper,
                               ColorMapper colorMapper) {
          this.analysisService = analysisService;
          this.coordsMapper = coordsMapper;
          this.imageMapper = imageMapper;
          this.colorMapper = colorMapper;
     }

     @PostMapping("/coords")
     public ChessboardPositionFeaturesDTO findChessboardCorners(
             @RequestBody @Validated ChessboardImageDTO emptyChessboard)
             throws IOException {
          Mat mat = imageMapper.toMat(emptyChessboard.getBase64Image());
          Mat lines = analysisService.detectLines(mat);
          Point[] roiCorners = analysisService.findCorners(mat, lines);
          Mat trimmed = analysisService.trimToCorners(mat, roiCorners);
          save(trimmed, "debug");
          Point[][] points = analysisService.detectAllTilesCornerPoints(mat);
          return coordsMapper.toChessboardFeaturesDTO(roiCorners, points);
     }

     @PostMapping("/colors")
     public ReferenceColorsDTO findReferenceColors(
             @RequestBody @Validated PreprocessedChessboardImageDTO preprocessedChessboardImage) throws IOException {

          Mat image = imageMapper.toMat(preprocessedChessboardImage.getImage().getBase64Image());
          Point[] chessboardCorners = coordsMapper.toPoints(preprocessedChessboardImage.getChessboardCorners());

          Mat preparedImage = analysisService.doPreprocessing(image, chessboardCorners);

          TileCorners[][] tilesCorners =
                  coordsMapper.toTilesCoords(preprocessedChessboardImage.getTilesCornerPoints());
          ReferenceColors referenceColors = analysisService.getReferenceColors(preparedImage, tilesCorners);
          return colorMapper.toReferenceColorsDTO(referenceColors);
     }
}