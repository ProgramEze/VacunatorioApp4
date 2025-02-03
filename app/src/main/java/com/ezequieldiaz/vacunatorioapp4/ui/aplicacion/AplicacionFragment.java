package com.ezequieldiaz.vacunatorioapp4.ui.aplicacion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezequieldiaz.vacunatorioapp4.R;

public class AplicacionFragment extends Fragment {

    private AplicacionFragmentViewModel mViewModel;

    public static AplicacionFragment newInstance() {
        return new AplicacionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aplicacion, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AplicacionFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}