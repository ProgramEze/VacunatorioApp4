package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarTutorFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<String> resultadoGuardado = new MutableLiveData<>();

    public AgregarTutorFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getResultadoGuardado() {
        return resultadoGuardado;
    }

    public void guardarTutor(String nombre, String apellido, String dni, String telefono, String email) {
        // Crear un objeto Tutor
        Tutor tutor = new Tutor(); // Asumo constructor vacío o setters disponibles
        tutor.setNombre(nombre);
        tutor.setApellido(apellido);
        tutor.setDni(dni);
        tutor.setEmail(email);
        tutor.setTelefono(telefono);

        // Obtener el token de autenticación
        String token = ApiClient.leerToken(getApplication());

        // Llamar a la API para guardar el tutor
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<Void> call = api.registrarTutor(token, dni, nombre, apellido, telefono, email); // Ajusta parámetros si es necesario
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultadoGuardado.setValue("Tutor guardado correctamente");
                } else {
                    //  Captura y muestra el error
                    int errorCode = response.code();
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer el cuerpo del error", e);
                    }
                    if (errorCode == 400 && errorBody != null) {
                        resultadoGuardado.setValue(errorBody);
                    } else {
                        resultadoGuardado.setValue("Error al guardar el tutor: " + errorCode + " - " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMessage = "Error de conexión: " + t.getMessage();
                Log.e("API_ERROR", errorMessage, t);
                resultadoGuardado.setValue(errorMessage);
            }
        });
    }
}