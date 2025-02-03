package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ezequieldiaz.vacunatorioapp4.R;

public class BarcodeScannerFragment extends Fragment {

    private BarcodeScannerFragmentViewModel mViewModel;

    public static BarcodeScannerFragment newInstance() {
        return new BarcodeScannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BarcodeScannerFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}