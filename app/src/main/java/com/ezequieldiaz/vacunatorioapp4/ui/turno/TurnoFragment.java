package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.DialogMesAnoBinding;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentTurnoBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.ui.registro.EscanerActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private DialogMesAnoBinding dialogBinding; // Variable para el binding del diálogo

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

        vm.getMFechaHora().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String fecha) {
                binding.etdFecha.setText(fecha);
            }
        });

        vm.getMFechaHora().observe(getViewLifecycleOwner(), fechaHora -> {
            binding.etdFecha.setText(fechaHora);
        });


        // Observador para el paciente
        vm.getMPaciente().observe(getViewLifecycleOwner(), paciente -> {
            if (paciente != null && vm.getMTutor().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        // Observador para el tutor
        vm.getMTutor().observe(getViewLifecycleOwner(), tutor -> {
            if (tutor != null && vm.getMPaciente().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
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

        /*vm.getMEventoMostrarDialogoMesAnio().observe(getViewLifecycleOwner(), mostrar -> {
            if (mostrar != null && mostrar) {
                mostrarDialogoMesAno();
                // Opcional: Resetea el evento en el ViewModel para que no se muestre de nuevo
                // al rotar la pantalla si no es el comportamiento deseado.
                // viewModel.dialogoMostrado(); // Necesitarías un método en el VM para esto.
            }
        });*/

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
                NavController navController = NavHostFragment.findNavController(this);

                // Chequear que estemos en Turno antes de navegar
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() == R.id.nav_turno) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fechaSeleccionada", fecha);

                    navController.navigate(R.id.action_nav_turno_to_nav_fechas, bundle);

                    // Resetear LiveData para evitar loops
                    vm.setFechaSeleccionada(null);
                }
            }
        });


        /*binding.etdFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(selectedYear, selectedMonth, selectedDay);

                        // Verificar que sea lunes-viernes
                        int dayOfWeek = selected.get(Calendar.DAY_OF_WEEK);
                        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                            Toast.makeText(getContext(), "Solo se permiten días de lunes a viernes", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String fechaSeleccionada = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        binding.etdFecha.setText(fechaSeleccionada);

                        // Consultar horarios libres
                        vm.cargarHorariosLibres(fechaSeleccionada);
                    },
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });*/

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

        /*binding.btnCargarTurno.setOnClickListener(v -> {
            try {
                binding.etDNIPaciente.setText("");
                binding.etDNITutor.setText("");
                binding.spnTipoDeVacuna.setSelection(0);
                binding.spnRelacionTutor.setSelection(0);
                // Crear LocalDateTime desde la fecha y hora elegidas
                LocalDateTime turno = LocalDateTime.of(
                        LocalDate.parse(binding.etdFecha.getText().toString(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())),
                        LocalTime.parse(binding.spnHorarios.getSelectedItem().toString() + ":00",
                                DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault()))
                );

                String citaFormateada = turno.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                //Log.d("cargarTurno", cita.toString());
                vm.cargarTurno(citaFormateada);
            }catch (DateTimeParseException e) {
                Log.d("cargar", e.getMessage());
                mostrarToast("Seleccione una fecha y una hora para la cita");
            }
        });*/

        binding.btnConfirmarTurno.setOnClickListener(v -> {
            if(binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")){
                vm.buscarDNIPaciente(binding.etDNIPaciente.getText().toString());
                vm.buscarDNITutor(binding.etDNITutor.getText().toString());
            } else {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        /*binding.etdFecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vm.fechaClickeada(event);
            }
        });*/

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

    /*private void mostrarDialogoMesAno() {
        // Asegúrate de que dialogBinding se infle cada vez para evitar "The specified child already has a parent"
        dialogBinding = DialogMesAnoBinding.inflate(getLayoutInflater()); // Correcto aquí
        final Dialog dialog = new Dialog(requireContext()); // Correcto aquí
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(true);

        // Array de meses desde el ViewModel
        String[] meses = vm.getMesesArray();
        ArrayAdapter<String> adapterMes = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, meses);
        adapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogBinding.spinnerMes.setAdapter(adapterMes);

        // Array de años desde el ViewModel
        List<String> listaAnios = vm.getAniosArray();
        ArrayAdapter<String> adapterAno = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, listaAnios);
        adapterAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogBinding.spinnerAnio.setAdapter(adapterAno);

        // Selección por defecto
        dialogBinding.spinnerMes.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        dialogBinding.spinnerAnio.setSelection(0); // Asume que el año actual es el primero

        // Botón OK
        // Asegúrate de que el ID R.id.btnOk existe en tu layout dialog_mes_ano.xml
        // y que es un Button o una View clickeable.
        Button btnOk = dialogBinding.getRoot().findViewById(R.id.btnOk); // O usa dialogBinding.btnOk si está definido en el binding
        if (btnOk != null) {
            btnOk.setOnClickListener(v -> {
                int mesPosition = dialogBinding.spinnerMes.getSelectedItemPosition();
                String mesSeleccionado = (String) dialogBinding.spinnerMes.getSelectedItem();
                String anoSeleccionado = (String) dialogBinding.spinnerAnio.getSelectedItem();

                vm.onMesAnioConfirmado(mesSeleccionado, mesPosition, anoSeleccionado);
                dialog.dismiss();
            });
        }


        dialog.setOnCancelListener(dialogInterface -> {
            vm.dialogoMesAnioCancelado();
        });
        dialog.setOnDismissListener(dialogInterface -> {
            // Puedes llamar a dialogoMesAnioCancelado aquí también si quieres que
            // el evento se resetee tanto al cancelar como al cerrar (dismiss)
        });

        dialog.show();
    }*/

    /*private void mostrarFechaSelecionada(long fechaSeleccionada) {
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
    }*/

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