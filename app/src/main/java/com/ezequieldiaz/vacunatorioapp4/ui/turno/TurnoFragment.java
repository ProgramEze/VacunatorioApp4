package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentTurnoBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.ui.registro.EscanerActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TurnoFragment extends Fragment {
    private TurnoFragmentViewModel vm;
    private FragmentTurnoBinding binding;
    private NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTurnoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(TurnoFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("fechaSeleccionadaCompleta").observe(getViewLifecycleOwner(), fechaIso -> {
            String hora = navController.getCurrentBackStackEntry().getSavedStateHandle().get("horaSeleccionada");
            String otorgarOCargar = navController.getCurrentBackStackEntry().getSavedStateHandle().get("darTurnoOCargarTurno");
            if (fechaIso != null && hora != null) {
                try {
                    String cita = fechaIso + "T" + hora + ":00";
                    Log.d("Cita", cita);
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = isoFormat.parse(fechaIso.toString());
                    Log.d("cargarTurno", date.toString());
                    SimpleDateFormat argFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String fechaFormateada = argFormat.format(date);
                    if (otorgarOCargar.equalsIgnoreCase("Dar turno")) {
                        vm.setFechaYHora(fechaFormateada + " - " + hora);
                    } else {
                        String citaIso = fechaIso + "T" + hora + ":00";
                        vm.cargarTurno(citaIso);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.etdFecha.setText(fechaIso + " / " + hora);
                }
            }
        });

        vm.getMPaciente().observe(getViewLifecycleOwner(), paciente -> {
            if (paciente != null && vm.getMTutor().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                //vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(), binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        vm.getMListaTipo().observe(getViewLifecycleOwner(), tiposDeVacuna -> {
            ArrayAdapter<TipoDeVacuna> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_dialog, tiposDeVacuna) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }
            };
            adapter.setDropDownViewResource(R.layout.spinner_item_dialog);
            binding.spnTipoDeVacuna.setAdapter(adapter);

            // 🔹 Restaurar la selección guardada en el ViewModel
            Integer index = vm.getSelectedTipoVacunaIndex().getValue();
            if (index != null && index < adapter.getCount()) {
                binding.spnTipoDeVacuna.setSelection(index);
            }
        });

        vm.getMTurno().observe(getViewLifecycleOwner(), turno -> {
            vm.getMTurnoCargado().observe(getViewLifecycleOwner(), cargado -> {
                if (turno != null && !cargado) {
                    binding.etDNIPaciente.setText(turno.getPacienteId() != 0 ? String.valueOf(turno.getPacienteId()) : "");
                    binding.etDNITutor.setText(turno.getTutorId() != 0 ? String.valueOf(turno.getTutorId()) : "");
                    binding.spnTipoDeVacuna.setSelection(turno.getTipoDeVacunaId());
                    binding.spnRelacionTutor.setSelection(turno.getRelacionTutor().equals("Madre") ? 1 : turno.getRelacionTutor().equals("Padre") ? 2 : turno.getRelacionTutor().equals("Tutor") ? 3 : 4);
                    formatearFechaHora(turno.getCita());
                    vm.setTurnoCargado(true);
                }
            });
        });

        vm.getMTutor().observe(getViewLifecycleOwner(), tutor -> {
            /*if (tutor != null && vm.getMPaciente().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
            vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(), binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
                //
            }*/
        });

        binding.etDNIPaciente.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String dniStr = binding.etDNIPaciente.getText().toString().trim();
                if (!dniStr.isEmpty()) {
                    try {
                        vm.buscarDNIPaciente(dniStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "DNI inválido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.etDNITutor.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String dniStr = binding.etDNITutor.getText().toString().trim();
                if (!dniStr.isEmpty()) {
                    try {
                        vm.buscarDNITutor(dniStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "DNI inválido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        vm.getMMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        vm.getMRelaciones().observe(getViewLifecycleOwner(), relaciones -> {
            ArrayAdapter<String> relacionAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_dialog, relaciones) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }
            };
            relacionAdapter.setDropDownViewResource(R.layout.spinner_item_dialog);
            binding.spnRelacionTutor.setAdapter(relacionAdapter);

            // 🔹 Restaurar la selección guardada en el ViewModel
            Integer index = vm.getSelectedRelacionIndex().getValue();
            if (index != null && index < relacionAdapter.getCount()) {
                binding.spnRelacionTutor.setSelection(index);
            }
        });

        vm.getMFechaYHora().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String fecha) {
                binding.etdFecha.setText(fecha);
            }
        });

        vm.getMTurno().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Turno turno) {
                if (turno != null) {
                    try {
                        binding.etDNIPaciente.setText(turno.getPaciente().getDni());
                        binding.etDNITutor.setText(turno.getTutor().getDni());
                        binding.spnTipoDeVacuna.setSelection(turno.getTipoDeVacuna().getId());
                        String relacion = turno.getRelacionTutor();
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spnRelacionTutor.getAdapter();
                        int pos = adapter.getPosition(relacion);
                        if (pos >= 0) {
                            binding.spnRelacionTutor.setSelection(pos);
                        }
                    } catch (NullPointerException e) {
                        Log.d("turno", e.getMessage());
                    }
                }
            }
        });

        vm.getMConfirmar().observe(getViewLifecycleOwner(), s -> binding.btnConfirmarTurno.setText(s));

        vm.getMLimpiar().observe(getViewLifecycleOwner(), limpiar -> {
            if (limpiar != null && limpiar) {
                vm.limpiarMutables();
                binding.etDNIPaciente.setText("");
                binding.etDNITutor.setText("");
                binding.spnTipoDeVacuna.setSelection(0);
                binding.spnRelacionTutor.setSelection(0);
                binding.etdFecha.setText("");
                vm.setLimpiar(false);
            }
        });

        vm.getMMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                vm.limpiarMensaje();
            }
        });

        vm.getMCancelarYConfirmar().observe(getViewLifecycleOwner(), mostrar -> {
            binding.btnCancelarTurno.setVisibility(mostrar != null && mostrar ? View.VISIBLE : View.GONE);
            binding.btnConfirmarAplicacion.setVisibility(mostrar != null && mostrar ? View.VISIBLE : View.GONE);
        });

        vm.getMFechaSeleccionada().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null) {
                if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == R.id.nav_turno) {
                    Bundle bundle = new Bundle();
                    if (!binding.etDNIPaciente.getText().toString().isEmpty()) {
                        if (vm.getMPaciente().getValue() != null && binding.etDNITutor.getText().toString().isEmpty()) {
                            bundle.putInt("dniPaciente", vm.getMPaciente().getValue().getId());
                        } else {
                            bundle.putInt("dniPaciente", -1);
                        }
                    } else {
                        bundle.putInt("dniPaciente", -1);
                    }
                    bundle.putSerializable("fechaSeleccionada", fecha);
                    navController.navigate(R.id.action_nav_turno_to_nav_fechas, bundle);
                    vm.setFechaSeleccionada(null);
                }
            }
        });

        vm.getMMostrarDialog().observe(getViewLifecycleOwner(), mostrar -> {
            if (mostrar != null && mostrar) {
                MesAnioDialog dialog = new MesAnioDialog();
                dialog.setOnFechaSeleccionadaListener((mes, anio) -> {
                    vm.setFechaSeleccionada(new FechaSeleccionada(mes, anio));
                });
                dialog.show(getParentFragmentManager(), "MesAnioDialog");
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

        binding.spnTipoDeVacuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.setSelectedTipoVacunaIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.spnRelacionTutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.setSelectedRelacionIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.etdFecha.setOnClickListener(v -> {
            vm.solicitarSeleccionFecha(binding.etDNIPaciente.getText().toString());
        });

        binding.btnLimpiar.setOnClickListener(v -> {
            vm.limpiarMutables();
        });

        binding.spnTipoDeVacuna.setOnTouchListener((v, event) -> {
            binding.etDNIPaciente.clearFocus();
            binding.etDNITutor.clearFocus();
            return false;
        });

        binding.spnRelacionTutor.setOnTouchListener((v, event) -> {
            binding.etDNIPaciente.clearFocus();
            binding.etDNITutor.clearFocus();
            return false;
        });

        binding.btnConfirmarTurno.setOnClickListener(v -> {
            String dniPaciente = binding.etDNIPaciente.getText().toString().trim();
            String dniTutor = binding.etDNITutor.getText().toString().trim();
            String fechaYHora = binding.etdFecha.getText().toString().trim();

            if (dniPaciente.isEmpty() || dniTutor.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese DNI de paciente y tutor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fechaYHora.isEmpty()) {
                Toast.makeText(getContext(), "Seleccione fecha y hora", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] partes = fechaYHora.split(" - ");
            if (partes.length != 2) {
                Toast.makeText(getContext(), "Formato de fecha/hora incorrecto", Toast.LENGTH_SHORT).show();
                return;
            }

            String fechaStr = partes[0].trim();
            String horaStr = partes[1].trim();
            Turno turno = new Turno();

            try {
                LocalDateTime cita = LocalDateTime.of(LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())), LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())));
                turno.setCita(cita.toString());

                if (vm.getMPaciente().getValue() != null) {
                    turno.setPacienteId(vm.getMPaciente().getValue().getId());
                } else {
                    Toast.makeText(getContext(), "Paciente no encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (vm.getMTutor().getValue() != null) {
                    turno.setTutorId(vm.getMTutor().getValue().getId());
                } else {
                    Toast.makeText(getContext(), "Tutor no encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                TipoDeVacuna tipo = (TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem();
                if (tipo != null && tipo.getId() != 0) {
                    turno.setTipoDeVacunaId(tipo.getId());
                }

                String relacion = (String) binding.spnRelacionTutor.getSelectedItem();
                if (relacion != null && !relacion.equals("Seleccione la relación con el tutor")) {
                    turno.setRelacionTutor(relacion);
                }

                // Agente del usuario logueado
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String matricula = sharedPreferences.getString("matricula", "00000000");
                turno.setAgenteId(Integer.parseInt(matricula));

                // Verificamos si estamos modificando un turno existente
                Turno turnoExistente = vm.getMTurno().getValue();
                if (turnoExistente != null) {
                    turno.setId(turnoExistente.getId());
                    turno.setAplicacionId(turnoExistente.getAplicacionId()); // Mantener ID de aplicación existente
                    vm.modificarTurno(turno);
                } else {
                    vm.guardarTurno(turno);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al procesar fecha y hora", Toast.LENGTH_SHORT).show();
            }
        });

        vm.getMMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
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

        return binding.getRoot();
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
    private void formatearFechaHora(String fechaISO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime fecha = LocalDateTime.parse(fechaISO, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String fechaStr = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaStr = fecha.format(DateTimeFormatter.ofPattern("HH:mm"));
            binding.etdFecha.setText(fechaStr + " - " + horaStr);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            try {
                Date date = sdf.parse(fechaISO);
                SimpleDateFormat fechaFmt = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
                binding.etdFecha.setText(fechaFmt.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}