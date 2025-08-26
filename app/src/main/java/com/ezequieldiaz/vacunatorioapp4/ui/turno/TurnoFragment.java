package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentTurnoBinding;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.ui.registro.EscanerActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

public class TurnoFragment extends Fragment {
    private TurnoFragmentViewModel vm;
    private FragmentTurnoBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTurnoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm = new ViewModelProvider(this).get(TurnoFragmentViewModel.class);

        vm.getMListaTipo().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<TipoDeVacuna> tiposDeVacuna) {
                    ArrayAdapter<TipoDeVacuna> adapter = new ArrayAdapter<>(
                            requireContext(),
                            R.layout.spinner_item_dialog,
                            tiposDeVacuna
                    ) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    adapter.setDropDownViewResource(R.layout.spinner_item_dialog);
                    binding.spnTipoDeVacuna.setAdapter(adapter);
            }
        });

        vm.getMRelaciones().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<String> relaciones) {
                ArrayAdapter<String> relacionAdapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_dialog, relaciones){
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }
                };
                relacionAdapter.setDropDownViewResource(R.layout.spinner_item_dialog);
                binding.spnRelacionTutor.setAdapter(relacionAdapter);
            }
        });

        vm.getMHorarios().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<String> horarios) {
                ArrayAdapter<String> horarioAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_dialog, horarios){
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }
                };
                horarioAdapter.setDropDownViewResource(R.layout.spinner_item_dialog);
                binding.spnHorarios.setAdapter(horarioAdapter);
            }
        });

        vm.getMFecha().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String fecha) {
                binding.etdFecha.setText(fecha);
            }
        });

        vm.getShowDatePickerEvent().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Long fechaSeleccionada) {
                mostrarFechaSelecionada(fechaSeleccionada);
            }
        });

        // Observador para el paciente
        vm.getMPaciente().observe(getViewLifecycleOwner(), paciente -> {
            if (paciente != null && vm.getMTutor().getValue() != null && binding.btnConfirmarCita.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.spnHorarios.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarCita.getText().toString());
            }
        });

        // Observador para el tutor
        vm.getMTutor().observe(getViewLifecycleOwner(), tutor -> {
            if (tutor != null && vm.getMPaciente().getValue() != null && binding.btnConfirmarCita.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.spnHorarios.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarCita.getText().toString());
            }
        });


        vm.getMPaciente().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Paciente p) {
                if(p != null){
                    binding.etDNIPaciente.setText(p.getDni());
                } else {
                    binding.etDNIPaciente.setText("");
                }
            }
        });

        vm.getMTurno().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Turno turno) {
                if(turno != null){
                    binding.etDNIPaciente.setText(turno.getPaciente().getDni());
                    binding.etDNITutor.setText(turno.getTutor().getDni());
                    binding.spnTipoDeVacuna.setSelection(turno.getTipoDeVacuna().getId());
                    String relacion = turno.getRelacionTutor();
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spnRelacionTutor.getAdapter();
                    int pos = adapter.getPosition(relacion);
                    if (pos >= 0) {
                        binding.spnRelacionTutor.setSelection(pos);
                    }
                }
            }
        });


        vm.getMTutor().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Tutor t) {
                if(t != null){
                    binding.etDNITutor.setText(t.getDni());
                } else {
                    binding.etDNITutor.setText("");
                }
            }
        });

        vm.getMLimpiar().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                limpiarCampos();
            }
        });

        vm.getMMensaje().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String msj) {
                mostrarToast(msj);
            }
        });

        vm.getMConfirmar().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String s) {
                binding.btnConfirmarCita.setText(s);
            }
        });

        vm.getmCancelarYConfirmar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mostrar) {
                binding.btnCancelarTurno.setVisibility(
                        mostrar != null && mostrar ? View.VISIBLE : View.GONE
                );
                binding.btnConfirmarAplicacion.setVisibility(
                        mostrar != null && mostrar ? View.VISIBLE : View.GONE
                );
            }
        });

        binding.ibCargarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etDNIPaciente.setText("");
                Intent intent = new Intent(requireContext(), EscanerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.ibCargarTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etDNITutor.setText("");
                Intent intent = new Intent(requireContext(), EscanerActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        binding.btnLimpiar.setOnClickListener(v -> {
            limpiarCampos();
        });

        binding.btnCargarTurno.setOnClickListener(v -> {
            try {
                binding.etDNIPaciente.setText("");
                binding.etDNITutor.setText("");
                binding.spnTipoDeVacuna.setSelection(0);
                binding.spnRelacionTutor.setSelection(0);
                // Crear LocalDateTime desde la fecha y hora elegidas
                LocalDateTime cita = LocalDateTime.of(
                        LocalDate.parse(binding.etdFecha.getText().toString(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())),
                        LocalTime.parse(binding.spnHorarios.getSelectedItem().toString() + ":00",
                                DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault()))
                );

                String citaFormateada = cita.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                //Log.d("cargarTurno", cita.toString());
                vm.cargarTurno(citaFormateada);
            }catch (DateTimeParseException e) {
                Log.d("cargar", e.getMessage());
                mostrarToast("Seleccione una fecha y una hora para la cita");
            }
        });

        binding.btnConfirmarCita.setOnClickListener(v -> {
            if(binding.btnConfirmarCita.getText().toString().equalsIgnoreCase("Otorgar turno")){
                vm.buscarDNIPaciente(binding.etDNIPaciente.getText().toString());
                vm.buscarDNITutor(binding.etDNITutor.getText().toString());
            } else {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.spnHorarios.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarCita.getText().toString());
            }
        });

        binding.etdFecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vm.fechaClickeada(event);
            }
        });

        binding.btnCancelarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.cancelarTurno();
            }
        });

        binding.btnConfirmarAplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.confirmarAplicacion();
            }
        });

        vm.cargarSpinners();

        return root;
    }

    public void mostrarToast(String mensaje){
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    private void limpiarCampos() {
        binding.etDNIPaciente.setText("");
        binding.etDNITutor.setText("");
        binding.etdFecha.setText("");
        binding.spnRelacionTutor.setSelection(0);
        binding.spnHorarios.setSelection(0);
        binding.spnTipoDeVacuna.setSelection(0);
        vm.limpiarMutables();
    }

    private void mostrarFechaSelecionada(long fechaSeleccionada) {
        Calendar fecha = Calendar.getInstance();
        fecha.setTimeInMillis(fechaSeleccionada);
        int anioElegido = fecha.get(Calendar.YEAR);
        int mesElegido = fecha.get(Calendar.MONTH);
        int diaElegido = fecha.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                        vm.fechaSeleccionada(anio, mes, dia);
                    }
                },
                anioElegido,
                mesElegido,
                diaElegido
        );
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vm.cargarImagen(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}