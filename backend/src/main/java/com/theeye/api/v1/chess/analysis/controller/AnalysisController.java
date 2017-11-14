package com.theeye.api.v1.chess.analysis.controller;

import com.theeye.api.v1.chess.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.analysis.model.dto.ChessBoardFeaturesDTO;
import com.theeye.api.v1.chess.analysis.service.AnalysisService;
import com.theeye.api.v1.chess.analysis.mapper.ImageMapper;
import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/chess/analysis")
public class AnalysisController {

     private final AnalysisService analysisService;

     private final CoordsMapper coordsMapper;

     private final ImageMapper imageMapper;

     @Autowired
     public AnalysisController(AnalysisService analysisService,
                               CoordsMapper coordsMapper,
                               ImageMapper imageMapper) {
          this.analysisService = analysisService;
          this.coordsMapper = coordsMapper;
          this.imageMapper = imageMapper;
     }

     @PostMapping("/features")
     public ChessBoardFeaturesDTO findChessboardCorners(@RequestBody ChessboardImageDTO emptyChessboard) throws IOException {
          Mat mat = imageMapper.toMat(emptyChessboard.getBase64Image());
          Mat lines = analysisService.detectLines(mat);
          Point[] roiCorners = analysisService.findCorners(mat, lines);
          Mat trimmed = analysisService.trimToCorners(mat, roiCorners);
          Point[][] points = analysisService.detectAllTilesCornerPoints(trimmed);
          return coordsMapper.toChessboardFeaturesDTO(roiCorners, points);
     }
}
