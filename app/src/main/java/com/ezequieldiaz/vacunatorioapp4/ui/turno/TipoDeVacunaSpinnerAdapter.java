package com.ezequieldiaz.vacunatorioapp4.ui.turno;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;

import java.util.List;

public class TipoDeVacunaSpinnerAdapter extends ArrayAdapter<TipoDeVacuna> {

    private final LayoutInflater inflater;

    public TipoDeVacunaSpinnerAdapter(@NonNull Context context, @NonNull List<TipoDeVacuna> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    private View createCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView != null ? convertView : inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);

        TipoDeVacuna tipo = getItem(position);
        if (tipo != null) {
            textView.setText(tipo.getNombre());
        }

        return view;
    }
}
