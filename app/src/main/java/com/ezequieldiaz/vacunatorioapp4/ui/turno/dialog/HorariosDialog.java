package com.ezequieldiaz.vacunatorioapp4.ui.turno.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.DialogHorariosBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;
import com.ezequieldiaz.vacunatorioapp4.model.HorariosResponse;
import com.ezequieldiaz.vacunatorioapp4.ui.turno.adapter.HorariosAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HorariosDialog extends DialogFragment {

    private static final String ARG_FECHA = "fechaSeleccionada";
    private FechasResponse fechaSeleccionada;
    private String darTurnoOCargarTurno;
    private DialogHorariosBinding binding;

    public static HorariosDialog newInstance(FechasResponse fecha, String darTurnoOCargarTurno) {
        HorariosDialog dialog = new HorariosDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FECHA, fecha);
        args.putString("darTurnoOCargarTurno", darTurnoOCargarTurno);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (getResources().getDisplayMetrics().widthPixels);
            int height = (getResources().getDisplayMetrics().heightPixels);
            getDialog().getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fechaSeleccionada = (FechasResponse) getArguments().getSerializable(ARG_FECHA);
            darTurnoOCargarTurno = getArguments().getString("darTurnoOCargarTurno");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogHorariosBinding.inflate(inflater, container, false);

        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = isoFormat.parse(fechaSeleccionada.getFecha());

            // Obtenemos día de la semana y mes en español
            SimpleDateFormat diaSemanaFormat = new SimpleDateFormat("EEEE", new Locale("es", "AR"));
            String diaSemana = diaSemanaFormat.format(date);

            SimpleDateFormat diaMesFormat = new SimpleDateFormat("d", Locale.getDefault());
            String diaMes = diaMesFormat.format(date);

            SimpleDateFormat mesFormat = new SimpleDateFormat("MMMM", new Locale("es", "AR"));
            String mes = mesFormat.format(date);

            SimpleDateFormat anioFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            String anio = anioFormat.format(date);

            // Armamos el título
            String titulo = "Horarios del " + diaSemana + " " + diaMes + " de " + mes + " del " + anio;
            binding.tvFechaDialog.setText(titulo);

        } catch (Exception e) {
            e.printStackTrace();
            binding.tvFechaDialog.setText(fechaSeleccionada.getFecha());
        }

        binding.rvHorarios.setLayoutManager(new GridLayoutManager(getContext(), 3));
        // Filtramos solo los pendientes
        List<HorariosResponse> horariosValidos = new ArrayList<>();
        for (HorariosResponse h : fechaSeleccionada.getHorarios()) {
            if (h.isLibre()) {
                horariosValidos.add(h);
            }
        }

        HorariosAdapter adapter = new HorariosAdapter(horariosValidos, horario -> {
            if (horario != null) {
                NavController navController = NavHostFragment.findNavController(HorariosDialog.this);
                navController.getBackStackEntry(R.id.nav_turno)
                        .getSavedStateHandle()
                        .set("fechaSeleccionadaCompleta", fechaSeleccionada.getFecha());

                navController.getBackStackEntry(R.id.nav_turno)
                        .getSavedStateHandle()
                        .set("horaSeleccionada", horario.getHora());

                navController.getBackStackEntry(R.id.nav_turno)
                        .getSavedStateHandle()
                        .set("darTurnoOCargarTurno", darTurnoOCargarTurno);

                Toast.makeText(
                        getContext(),
                        "Fecha seleccionada: " + fechaSeleccionada.getFecha() + " / " + horario.getHora(),
                        Toast.LENGTH_LONG
                ).show();

                dismiss();
                navController.popBackStack(R.id.nav_turno, false);
            }
        });



        binding.rvHorarios.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
