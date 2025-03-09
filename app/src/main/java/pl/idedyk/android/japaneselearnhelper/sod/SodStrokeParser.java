package pl.idedyk.android.japaneselearnhelper.sod;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import android.graphics.Matrix;

public class SodStrokeParser {
	
	private static List<StrokePath> parseWsReplyStrokes(StrokePathInfo strokePathsInfo) {

		List<StrokePath> result = new ArrayList<StrokePath>();

		List<List<String>> strokePaths = strokePathsInfo.getStrokePaths();

		float moveX = 0.0f;

		for (int charStrokePathsIdx = 0; charStrokePathsIdx < strokePaths.size(); ++charStrokePathsIdx) {

			List<String> currentCharStrokePaths = strokePaths.get(charStrokePathsIdx);

			float currentCharMaxX = 0.0f;

			List<StrokePath> charSmallResult = new ArrayList<StrokePath>();

			for (int i = 0; i < currentCharStrokePaths.size(); i++) {
				String line = currentCharStrokePaths.get(i);

				if (line != null && !"".equals(line)) {
					StrokePath strokePath = StrokePath.parsePath(line.trim());

					currentCharMaxX = Math.max(currentCharMaxX, strokePath.getMaxX());                    

					charSmallResult.add(strokePath);
				}
			}
			
			for (StrokePath currentCharSmallResult : charSmallResult) {
				// move
				Matrix matrix = new Matrix();
				matrix.setTranslate(moveX, 0);
				currentCharSmallResult.transformMoveTo(matrix);
				
				currentCharSmallResult.transformNonRelativeCurves(moveX);
			}
			
			result.addAll(charSmallResult);

			moveX += currentCharMaxX;
		}

		return result;
	}

	public static StrokedCharacter parseWsReply(StrokePathInfo strokePathsInfo) {
		List<StrokePath> strokes = parseWsReplyStrokes(strokePathsInfo);

		float maxX = 0.0f;
		float maxY = 0.0f;

		for (StrokePath strokePath : strokes) {
			maxX = Math.max(strokePath.getMaxX(), maxX);
			maxY = Math.max(strokePath.getMaxY(), maxY);
		}

		StrokedCharacter result = new StrokedCharacter(strokes, maxX, maxY);

		return result;
	}
}
