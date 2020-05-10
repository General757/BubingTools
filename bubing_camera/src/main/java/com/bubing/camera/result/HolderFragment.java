package com.bubing.camera.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bubing.camera.callback.PuzzleCallback;
import com.bubing.camera.callback.SelectCallback;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.engine.ImageEngine;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.ui.EasyPhotosActivity;
import com.bubing.camera.ui.PuzzleActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @ClassName: HolderFragment
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 15:24
 */
public class HolderFragment extends Fragment {
    private static final int HOLDER_SELECT_REQUEST_CODE = 0x44;
    private static final int HOLDER_PUZZLE_REQUEST_CODE = 0x55;
    private SelectCallback mSelectCallback;
    private PuzzleCallback mPuzzleCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startEasyPhoto(SelectCallback callback) {
        mSelectCallback = callback;
        EasyPhotosActivity.start(this, HOLDER_SELECT_REQUEST_CODE);
    }

    public void startPuzzleWithPhotos(ArrayList<Photo> photos, String puzzleSaveDirPath, String puzzleSaveNamePrefix, boolean replaceCustom, @NonNull ImageEngine imageEngine, PuzzleCallback callback) {
        mPuzzleCallback = callback;
        PuzzleActivity.startWithPhotos(this, photos, puzzleSaveDirPath, puzzleSaveNamePrefix, HOLDER_PUZZLE_REQUEST_CODE, replaceCustom, imageEngine);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case HOLDER_SELECT_REQUEST_CODE:
                    if (mSelectCallback != null) {
                        ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(Constants.Key.RESULT_PHOTOS);
                        boolean selectedOriginal = data.getBooleanExtra(Constants.Key.RESULT_SELECTED_ORIGINAL, false);
                        mSelectCallback.onResult(resultPhotos, selectedOriginal);
                    }
                    break;
                case HOLDER_PUZZLE_REQUEST_CODE:
                    if (mPuzzleCallback != null) {
                        Photo puzzlePhoto = data.getParcelableExtra(Constants.Key.RESULT_PHOTOS);
                        mPuzzleCallback.onResult(puzzlePhoto);
                    }
                    break;
            }
        }
    }
}
