package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentTurnoBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.ui.registro.EscanerActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TurnoFragment extends Fragment {
    private TurnoFragmentViewModel vm;
    private FragmentTurnoBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTurnoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm = new ViewModelProvider(this).get(TurnoFragmentViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("fechaSeleccionadaCompleta")
                .observe(getViewLifecycleOwner(), fechaIso -> {
                    String hora = navController.getCurrentBackStackEntry()
                            .getSavedStateHandle()
                            .get("horaSeleccionada");
                    // 2025-08-20T08:15:00
                    if (fechaIso != null && hora != null) {
                        try {
                            String cita = fechaIso + "T" + hora+":00";
                            Log.d("Cita", cita);
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date = isoFormat.parse(fechaIso.toString());

                            SimpleDateFormat argFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String fechaFormateada = argFormat.format(date);
                            binding.etdFecha.setText(fechaFormateada + " - " + hora);
                        } catch (Exception e) {
                            e.printStackTrace();
                            binding.etdFecha.setText(fechaIso + " / " + hora);
                        }
                    }
                });

        vm.getMListaTipo().observe(getViewLifecycleOwner(), tiposDeVacuna -> {
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

        vm.getMFechaYHora().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String fecha) {
                binding.etdFecha.setText(fecha);
            }
        });

        vm.getMFechaYHora().observe(getViewLifecycleOwner(), fechaHora -> {
            binding.etdFecha.setText(fechaHora);
        });

        vm.getMPaciente().observe(getViewLifecycleOwner(), paciente -> {
            if (paciente != null && vm.getMTutor().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(), binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        vm.getMTutor().observe(getViewLifecycleOwner(), tutor -> {
            if (tutor != null && vm.getMPaciente().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(), binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
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
                binding.btnConfirmarTurno.setText(s);
            }
        });

        vm.getMCancelarYConfirmar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        binding.etdFecha.setOnClickListener(v -> {
            vm.solicitarSeleccionFecha();
        });

        vm.getMMostrarDialog().observe(getViewLifecycleOwner(), mostrar -> {
            if (mostrar != null && mostrar) {
                MesAnioDialog dialog = new MesAnioDialog();
                dialog.setOnFechaSeleccionadaListener((mes, anio) -> {
                    FechaSeleccionada fecha = new FechaSeleccionada(mes, anio);
                    vm.setFechaSeleccionada(fecha); // Solo actualizar el ViewModel
                });
                dialog.show(getParentFragmentManager(), "MesAnioDialog");
            }
        });

        vm.getMFechaSeleccionada().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null) {
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() == R.id.nav_turno) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fechaSeleccionada", fecha);
                    navController.navigate(R.id.action_nav_turno_to_nav_fechas, bundle);

                    vm.setFechaSeleccionada(null);
                }
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

        binding.btnConfirmarTurno.setOnClickListener(v -> {
            if(binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")){
                vm.buscarDNIPaciente(binding.etDNIPaciente.getText().toString());
                vm.buscarDNITutor(binding.etDNITutor.getText().toString());
            } else {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
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
        binding.spnTipoDeVacuna.setSelection(0);
        vm.limpiarMutables();
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