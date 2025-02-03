package com.ezequieldiaz.vacunatorioapp4.ui.deslogeo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentSalirBinding;

public class SalirFragment extends Fragment {
    private FragmentSalirBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSalirBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SalirDialogo.mostrarDialogo(getContext());
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}