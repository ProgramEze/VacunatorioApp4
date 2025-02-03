package com.ezequieldiaz.vacunatorioapp4.ui.deslogeo;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ezequieldiaz.vacunatorioapp4.LoginActivity;
import com.ezequieldiaz.vacunatorioapp4.MainActivity;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

public class SalirDialogo extends AndroidViewModel {
    public SalirDialogo(@NonNull Application application) {
        super(application);
    }

    public static void mostrarDialogo(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar salida");
        builder.setMessage("¿Está seguro que desea salir?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Desloguear", ApiClient.leerToken(context.getApplicationContext()));

                // Eliminar el token
                ApiClient.eliminarToken(context.getApplicationContext());

                // Crear un Intent para iniciar la actividad de inicio de sesión
                Intent intent = new Intent(context, LoginActivity.class);

                // Establecer los flags para limpiar la pila de actividades
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // Iniciar la actividad de inicio de sesión
                context.startActivity(intent);

                // Finalizar la actividad actual
                ((MainActivity) context).finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
