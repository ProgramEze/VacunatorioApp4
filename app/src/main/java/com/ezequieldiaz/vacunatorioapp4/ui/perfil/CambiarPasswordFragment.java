package com.ezequieldiaz.vacunatorioapp4.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentCambiarPasswordBinding;

public class CambiarPasswordFragment extends Fragment {
    private FragmentCambiarPasswordBinding binding;
    private CambiarPasswordFragmentViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CambiarPasswordFragmentViewModel.class);
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnGuardarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cambiarPassword(binding.etViejaPassword.getText().toString(), binding.etNuevaPassword.getText().toString(), binding.etRepetirNuevaPassword.getText().toString(), getView());
            }
        });
        return root;
    }

}