package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import static android.app.Activity.RESULT_OK;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentAgregarPacienteBinding;

public class AgregarPacienteFragment extends Fragment {

    private AgregarPacienteFragmentViewModel viewModel;
    private FragmentAgregarPacienteBinding binding; // Binding para el layout
    private static final int REQUEST_CODE_SCAN = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout usando View Binding
        binding = FragmentAgregarPacienteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(AgregarPacienteFragmentViewModel.class);

        // Configurar el clic del botón de escaneo
        binding.btnEscanear.setOnClickListener(v -> {
            // Iniciar la actividad de escaneo
            Intent intent = new Intent(getActivity(), EscanerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        });

        // Configurar el clic del botón de guardar
        binding.btnGuardar.setOnClickListener(v -> {
            // Obtener los datos ingresados
            String nombre = binding.etNombre.getText().toString();
            String apellido = binding.etApellido.getText().toString();
            String dni = binding.etDNI.getText().toString();
            String telefono = binding.etTelefono.getText().toString();
            String email = binding.etEmail.getText().toString();

            // Validar los datos
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al ViewModel para guardar el paciente
            viewModel.guardarPaciente(nombre, apellido, dni, telefono, email);
        });

        // Configurar el clic del botón de cancelar
        binding.btnCancelar.setOnClickListener(v -> {
            // Cerrar el fragmento
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Observar cambios en el ViewModel
        viewModel.getResultadoGuardado().observe(getViewLifecycleOwner(), resultado -> {
            if (resultado != null) {
                Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
                if (resultado.equals("Paciente/Tutor guardado correctamente")) {
                    requireActivity().getSupportFragmentManager().popBackStack(); // Cerrar el fragmento después de guardar
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            // Obtener los datos escaneados
            String datosEscaneados = data.getStringExtra("DATOS_ESCANEADOS");
            System.out.println("Datos escaneados: " + datosEscaneados);
            if (datosEscaneados != null) {
                // Dividir la cadena usando el delimitador '@'
                String[] partes = datosEscaneados.split("@");
                if (partes.length >= 5) {  // Asegurarse de que se tengan al menos 5 partes
                    // Asignar los datos a los campos correspondientes
                    binding.etApellido.setText(partes[1]);
                    binding.etNombre.setText(partes[2]);
                    binding.etDNI.setText(partes[4]);
                } else {
                    Toast.makeText(getContext(), "Formato del código de barras inválido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar el binding cuando la vista se destruya
        binding = null;
    }
}