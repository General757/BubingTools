package com.bubing.camera.models.puzzle.template.straight;

import com.bubing.camera.models.puzzle.PuzzleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: StraightLayoutHelper
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 16:47
 */
public class StraightLayoutHelper {
    private StraightLayoutHelper() {

    }

    public static List<PuzzleLayout> getAllThemeLayout(int pieceCount) {
        List<PuzzleLayout> puzzleLayouts = new ArrayList<>();
        switch (pieceCount) {
            case 1:
                for (int i = 0; i < 6; i++) {
                    puzzleLayouts.add(new OneStraightLayout(i));
                }
                break;
            case 2:
                for (int i = 0; i < 6; i++) {
                    puzzleLayouts.add(new TwoStraightLayout(i));
                }
                break;
            case 3:
                for (int i = 0; i < 6; i++) {
                    puzzleLayouts.add(new ThreeStraightLayout(i));
                }
                break;
            case 4:
                for (int i = 0; i < 8; i++) {
                    puzzleLayouts.add(new FourStraightLayout(i));
                }
                break;
            case 5:
                for (int i = 0; i < 17; i++) {
                    puzzleLayouts.add(new FiveStraightLayout(i));
                }
                break;
            case 6:
                for (int i = 0; i < 12; i++) {
                    puzzleLayouts.add(new SixStraightLayout(i));
                }
                break;
            case 7:
                for (int i = 0; i < 9; i++) {
                    puzzleLayouts.add(new SevenStraightLayout(i));
                }
                break;
            case 8:
                for (int i = 0; i < 11; i++) {
                    puzzleLayouts.add(new EightStraightLayout(i));
                }
                break;
            case 9:
                for (int i = 0; i < 8; i++) {
                    puzzleLayouts.add(new NineStraightLayout(i));
                }
                break;
        }

        return puzzleLayouts;
    }
}

