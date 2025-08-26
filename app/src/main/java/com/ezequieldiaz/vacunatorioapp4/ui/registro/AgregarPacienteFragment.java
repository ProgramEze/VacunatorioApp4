package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentAgregarPacienteBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AgregarPacienteFragment extends Fragment {

    private FragmentAgregarPacienteBinding binding;
    private AgregarPacienteFragmentViewModel viewModel;
    private static final int REQUEST_CODE_SCAN = 101;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgregarPacienteBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AgregarPacienteFragmentViewModel.class);

        configurarSpinnerGenero();

        binding.btnEscanear.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EscanerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        });

        binding.btnGuardar.setOnClickListener(v -> {
            String nombre = binding.etNombre.getText().toString();
            String apellido = binding.etApellido.getText().toString();
            String dni = binding.etDNI.getText().toString();
            String fechaNacimientoTexto = binding.etFechaDeNacimiento.getText().toString();
            String genero = binding.spinnerGenero.getSelectedItem().toString();

            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaNacimientoTexto.isEmpty()
                    || genero.equals("Seleccione el género del paciente")) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!dni.matches("\\d{7,8}")) {
                Toast.makeText(getContext(), "DNI inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si llegamos acá, ya sabemos que la fecha no está vacía. Ahora la parseamos:
            LocalDate fechaNacimiento;
            try {
                fechaNacimiento = LocalDate.parse(fechaNacimientoTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Fecha inválida. Formato esperado: dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.guardarPaciente(nombre, apellido, dni, fechaNacimiento.toString(), genero);
        });


        binding.btnCancelar.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        viewModel.getResultado().observe(getViewLifecycleOwner(), resultado -> {
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            if (resultado.contains("guardado correctamente")) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void configurarSpinnerGenero() {
        String[] generos = getResources().getStringArray(R.array.genero_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item_genero,  // el layout que creaste
                generos
        ) {
            @Override
            public boolean isEnabled(int position) {
                // El primer ítem ("Seleccione...") no se puede seleccionar
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY); // Placeholder en gris
                } else {
                    tv.setTextColor(Color.BLACK); // Normal en negro
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenero.setAdapter(adapter);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String datos = data.getStringExtra("DATOS_ESCANEADOS");
            if (datos != null) {
                String[] partes = datos.split("@");
                if (partes.length >= 8) {
                    binding.etApellido.setText(partes[1]);
                    binding.etNombre.setText(partes[2]);
                    binding.etDNI.setText(partes[4]);
                    binding.etFechaDeNacimiento.setText(partes[6]);

                    // Asignar género si coincide
                    String genero = partes[3].equalsIgnoreCase("M") ? "Masculino" :
                            partes[3].equalsIgnoreCase("F") ? "Femenino" : "Otro";
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spinnerGenero.getAdapter();
                    int pos = adapter.getPosition(genero);
                    if (pos >= 0) binding.spinnerGenero.setSelection(pos);
                } else {
                    Toast.makeText(getContext(), "Datos escaneados incompletos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
