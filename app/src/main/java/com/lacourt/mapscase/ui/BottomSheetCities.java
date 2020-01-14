package com.lacourt.mapscase.ui;

import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lacourt.mapscase.R;

public class BottomSheetCities extends BottomSheetDialogFragment {
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_cities_layout, null);
        dialog.setContentView(contentView);


    }
}
