package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ezequieldiaz.vacunatorioapp4.databinding.DialogMesAnoBinding;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MesAnioDialog extends DialogFragment {

    public interface OnFechaSeleccionadaListener {
        void onFechaSeleccionada(int mes, int anio); // ahora todo en int
    }

    private OnFechaSeleccionadaListener listener;

    public void setOnFechaSeleccionadaListener(OnFechaSeleccionadaListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // ViewBinding
        DialogMesAnoBinding binding = DialogMesAnoBinding.inflate(LayoutInflater.from(getContext()));

        // Obtener mes y año actual
        Calendar calendar = Calendar.getInstance();
        int anioActual = calendar.get(Calendar.YEAR);
        int mesActual = calendar.get(Calendar.MONTH); // 0 = enero, 11 = diciembre

        // Meses en castellano
        String[] meses = new DateFormatSymbols(new Locale("es", "ES")).getMonths();

        // Rango de años (ejemplo: actual hasta +5)
        List<Integer> anios = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            anios.add(anioActual + i);
        }

        ArrayAdapter<Integer> anioAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                anios
        );
        anioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAnio.setAdapter(anioAdapter);

        // Guardar índice real de los meses en paralelo
        List<Integer> indicesMesesVisibles = new ArrayList<>();

        // --- Método para cargar meses visibles ---
        Runnable cargarMeses = () -> {
            int anioSeleccionado = (int) binding.spinnerAnio.getSelectedItem();
            List<String> mesesVisibles = new ArrayList<>();
            indicesMesesVisibles.clear();
            int indexSeleccionar = 0;

            for (int i = 0; i < 12; i++) {
                if (anioSeleccionado > anioActual || i >= mesActual) {
                    if (anioSeleccionado == anioActual && i == mesActual) {
                        indexSeleccionar = mesesVisibles.size(); // mes actual
                    }
                    mesesVisibles.add(meses[i]);
                    indicesMesesVisibles.add(i); // guardamos el índice real
                }
            }

            ArrayAdapter<String> mesAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    mesesVisibles
            );
            mesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerMes.setAdapter(mesAdapter);

            // Seleccionar automáticamente el mes correcto
            binding.spinnerMes.setSelection(indexSeleccionar);
        };

        // Cargar meses iniciales
        cargarMeses.run();

        // Recalcular meses cuando cambia el año
        binding.spinnerAnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarMeses.run();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Botón OK dentro del layout
        binding.btnOk.setOnClickListener(v -> {
            int anioSeleccionado = (int) binding.spinnerAnio.getSelectedItem();
            int posMes = binding.spinnerMes.getSelectedItemPosition();
            int mesSeleccionado = indicesMesesVisibles.get(posMes); // número real de mes (0–11)

            if (listener != null) {
                listener.onFechaSeleccionada(mesSeleccionado+1, anioSeleccionado);
            }
            dismiss();
        });

        return builder.setView(binding.getRoot()).create();
    }
}
