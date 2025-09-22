package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ezequieldiaz.vacunatorioapp4.databinding.DialogHorariosBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;

public class HorariosDialog extends DialogFragment {

    private static final String ARG_FECHA = "fechaSeleccionada";
    private FechasResponse fechaSeleccionada;
    private DialogHorariosBinding binding;

    public static HorariosDialog newInstance(FechasResponse fecha) {
        HorariosDialog dialog = new HorariosDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FECHA, fecha);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fechaSeleccionada = (FechasResponse) getArguments().getSerializable(ARG_FECHA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogHorariosBinding.inflate(inflater, container, false);

        // Mostramos la fecha
        binding.tvFechaDialog.setText(fechaSeleccionada.getFecha());

        // Configuramos RecyclerView con GridLayoutManager
        binding.rvHorariosDialog.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Creamos adapter y manejamos click en horario
        HorariosAdapter adapter = new HorariosAdapter(fechaSeleccionada.getHorarios(), horario -> {
            // Acción al clickear un horario libre
            // Toast.makeText(getContext(), "Horario seleccionado: " + horario.getHora(), Toast.LENGTH_SHORT).show();
            dismiss(); // cerramos diálogo
        });

        binding.rvHorariosDialog.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
