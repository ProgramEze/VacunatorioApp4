package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentTurnoBinding;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.ui.registro.EscanerActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TurnoFragment extends Fragment {

    private TurnoFragmentViewModel vm;
    private FragmentTurnoBinding binding;

    // En tu Fragment o Activity, fuera de cualquier método, como constantes
    private static final int REQUEST_CODE_SCAN_PACIENTE_DNI = 1; // Un número único para el paciente
    private static final int REQUEST_CODE_SCAN_TUTOR_DNI = 2;   // Otro número único para el tutor


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTurnoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm = new ViewModelProvider(this).get(TurnoFragmentViewModel.class);

        vm.getMListaTipo().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<TipoDeVacuna> tipos) {
                ArrayAdapter<TipoDeVacuna> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, tipos);
                binding.spnTipoDeVacuna.setAdapter(tipoAdapter);
            }
        });

        vm.getMRelaciones().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<String> relaciones) {
                ArrayAdapter<String> relacionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, relaciones);
                binding.spnRelacionTutor.setAdapter(relacionAdapter);
            }
        });

        vm.getMHorarios().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<String> horarios) {
                ArrayAdapter<String> horarioAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, horarios);
                binding.spnHorarios.setAdapter(horarioAdapter);
            }
        });

        vm.getMFecha().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String fecha) {
                binding.etdFecha.setText(fecha);
            }
        });

        // Observa el evento para mostrar el selector de fecha
        vm.getShowDatePickerEvent().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Long initialDateMillis) {
                showDatePicker(initialDateMillis);
            }
        });

        vm.getMPaciente().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Paciente p) {
                binding.etDNIPaciente.setText(p.getDni());
            }
        });

        vm.getMTutor().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Tutor t) {
                binding.etDNITutor.setText(t.getDni());
            }
        });

        binding.ibCargarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del escáner para el PACIENTE, usando su requestCode
                Intent intent = new Intent(requireContext(), EscanerActivity.class); // O tu actividad de escaneo
                startActivityForResult(intent, REQUEST_CODE_SCAN_PACIENTE_DNI);
            }
        });

        binding.ibCargarTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del escáner para el TUTOR, usando su requestCode
                Intent intent = new Intent(requireContext(), EscanerActivity.class); // O tu actividad de escaneo
                startActivityForResult(intent, REQUEST_CODE_SCAN_TUTOR_DNI);
            }
        });

        binding.btnLimpiar.setOnClickListener(v -> {
            limpiarCampos();
        });

        binding.btnConfirmarCita.setOnClickListener(v -> {
            Turno turno = new Turno();
            vm.buscarDNITutor(binding.etDNITutor.getText().toString());
            vm.buscarDNIPaciente(binding.etDNIPaciente.getText().toString());
            turno.setTipoDeVacunaId(binding.spnTipoDeVacuna.getSelectedItemPosition()+1);
            turno.setRelacionTutor(binding.spnRelacionTutor.getSelectedItem().toString());
            // Obtener la fecha del EditText
            String fechaString = binding.etdFecha.getText().toString();
            // Obtener la hora seleccionada del Spinner
            String horaString = (String) binding.spnHorarios.getSelectedItem();
            // Definir los formateadores para parsear las cadenas
            // Asegúrate de que el formato coincida con cómo se muestran la fecha y la hora en tus Views
            DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
            try {
                // Parsear la cadena de fecha a LocalDate
                LocalDate fecha = LocalDate.parse(fechaString, fechaFormatter);

                // Parsear la cadena de hora a LocalTime
                LocalTime hora = LocalTime.parse(horaString, horaFormatter);

                // Combinar LocalDate y LocalTime para obtener un LocalDateTime
                LocalDateTime fechaHoraTurno = LocalDateTime.of(fecha, hora);
                turno.setCita(fechaHoraTurno.toString());

                // Ahora tienes un objeto LocalDateTime con la fecha y hora seleccionadas
                // Puedes usar fechaHoraTurno para tus operaciones

                // Ejemplo: Imprimir el LocalDateTime
                Log.d("TurnoFragment", "LocalDateTime creado: " + fechaHoraTurno.toString());
                vm.guardarTurno(turno);

            } catch (DateTimeParseException e) {
                // Manejar el error si las cadenas de fecha u hora no tienen el formato esperado
                Log.e("TurnoFragment", "Error al parsear fecha u hora: " + e.getMessage());
                // Puedes mostrar un mensaje al usuario indicando un formato incorrecto
            }
        });

        binding.etdFecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("debug", "click on date input");
                    vm.onDateInputClicked(); // Notifica al ViewModel que se hizo clic
                    return true;
                }
                return false;
            }
        });

        vm.cargarSpinners();

        return root;
    }


    private void limpiarCampos() {
        binding.etDNIPaciente.setText("");
        binding.etDNITutor.setText("");
        binding.etdFecha.setText("");
        binding.spnRelacionTutor.setSelection(0);
        binding.spnHorarios.setSelection(0);
        binding.spnTipoDeVacuna.setSelection(0);
    }

    // Método para mostrar el DatePickerDialog
    private void showDatePicker(long initialDateMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(initialDateMillis);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(), // Usa el Context válido del Fragment
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Maneja la fecha seleccionada
                        Log.d("debug", "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                        // Notifica al ViewModel la fecha seleccionada
                        vm.onDateSelected(year, month, dayOfMonth);
                    }
                },
                currentYear, // Año inicial
                currentMonth, // Mes inicial (0-indexed)
                currentDay // Día inicial
        );
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String datosEscaneados = data.getStringExtra("DATOS_ESCANEADOS");
            Log.d("ScanResult", "Datos escaneados: " + datosEscaneados);

            if (datosEscaneados != null) {
                String[] partes = datosEscaneados.split("@");

                // Asegurarse de que se tengan al menos 5 partes según tu formato de DNI
                if (partes.length >= 5) {
                    String dniEscaneado = partes[4];
                    Log.d("ScanResult", "DNI escaneado: " + dniEscaneado);

                    // **¡Aquí está la lógica clave!**
                    if (requestCode == REQUEST_CODE_SCAN_PACIENTE_DNI) {
                        // Podrías llamar a tu lógica para buscar el paciente por DNI aquí si necesitas validarlo inmediatamente
                        vm.buscarDNIPaciente(dniEscaneado);

                    } else if (requestCode == REQUEST_CODE_SCAN_TUTOR_DNI) {
                        // Podrías llamar a tu lógica para buscar el tutor por DNI aquí
                        vm.buscarDNITutor(dniEscaneado);

                    }
                } else {
                    Toast.makeText(getContext(), "Formato del código de barras inválido", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Manejar el caso en que el usuario cancela el escaneo si es necesario
            Log.d("ScanResult", "Escaneo cancelado");
        }
        // Puedes agregar más else if para otros requestCodes si los tienes
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}