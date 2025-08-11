package com.ezequieldiaz.vacunatorioapp4.ui.aplicacion;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentAplicacionBinding;

import java.util.Calendar;

public class AplicacionFragment extends Fragment {

    private AplicacionFragmentViewModel vm;
    private FragmentAplicacionBinding binding;

    public static AplicacionFragment newInstance() {
        return new AplicacionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAplicacionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm = new ViewModelProvider(this).get(AplicacionFragmentViewModel.class);

        vm.getShowDatePickerEvent().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Long fechaSeleccionada) {
                mostrarFechaSelecionada(fechaSeleccionada);
            }
        });



        return root;
    }

    private void mostrarFechaSelecionada(long fechaSeleccionada) {
        Calendar fecha = Calendar.getInstance();
        fecha.setTimeInMillis(fechaSeleccionada);
        int anioElegido = fecha.get(Calendar.YEAR);
        int mesElegido = fecha.get(Calendar.MONTH);
        int diaElegido = fecha.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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
        datePickerDialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(this).get(AplicacionFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}