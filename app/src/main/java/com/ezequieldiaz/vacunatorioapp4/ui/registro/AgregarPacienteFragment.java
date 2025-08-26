package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentAgregarPacienteBinding;


public class AgregarPacienteFragment extends Fragment {

    private FragmentAgregarPacienteBinding binding;
    private AgregarPacienteFragmentViewModel vm;
    protected static final int REQUEST_CODE_SCAN = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgregarPacienteBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(AgregarPacienteFragmentViewModel.class);

        configurarSpinnerGenero();

        binding.btnEscanear.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EscanerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        });

        binding.btnGuardar.setOnClickListener(v -> {
            vm.guardarPaciente(binding.etNombre.getText().toString(), binding.etApellido.getText().toString(), binding.etDNI.getText().toString(), binding.etFechaDeNacimiento.getText().toString(), binding.spinnerGenero.getSelectedItem().toString());
        });

        binding.btnCancelar.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        vm.getMMensaje().observe(getViewLifecycleOwner(), resultado -> {
            vm.agregadoExitoso(resultado, requireActivity());
        });

        vm.getMPaciente().observe(getViewLifecycleOwner(), paciente -> {
            binding.etNombre.setText(paciente.getNombre());
            binding.etApellido.setText(paciente.getApellido());
            binding.etDNI.setText(paciente.getDni());
            Log.d("fechaDeNacimiento", paciente.getFechaDeNacimiento().toString());
            binding.etFechaDeNacimiento.setText(paciente.getFechaDeNacimiento().toString());
            binding.spinnerGenero.setSelection(paciente.getGenero().equals("Masculino") ? 1 : paciente.getGenero().equals("Femenino") ? 2 : 3);
        });

        return binding.getRoot();
    }

    private void configurarSpinnerGenero() {
        String[] generos = getResources().getStringArray(R.array.genero_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item_genero,
                generos
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenero.setAdapter(adapter);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vm.cargarDatos(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
