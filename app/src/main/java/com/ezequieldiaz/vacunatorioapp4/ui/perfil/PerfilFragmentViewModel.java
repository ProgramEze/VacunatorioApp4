package com.ezequieldiaz.vacunatorioapp4.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezequieldiaz.vacunatorioapp4.LoginActivity;
import com.ezequieldiaz.vacunatorioapp4.model.Agente;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragmentViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private MutableLiveData<Agente> mAgente;
    private MutableLiveData<String> mGuardar;
    private MutableLiveData<Boolean> mHabilitar;
    private MutableLiveData<Integer> mVisible;

    public PerfilFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Agente> getMAgente() {
        if(mAgente == null){
            mAgente = new MutableLiveData<>();
        }
        return mAgente;
    }

    public LiveData<String> getMGuardar() {
        if(mGuardar == null){
            mGuardar = new MutableLiveData<>();
        }
        return mGuardar;
    }

    public LiveData<Boolean> getMHabilitar() {
        if(mHabilitar == null){
            mHabilitar = new MutableLiveData<>();
        }
        return mHabilitar;
    }

    public void editarDatos(String boton, Agente agente){
        if(boton.equals("Editar perfil")){
            mGuardar.setValue("Guardar perfil");
            mHabilitar.setValue(true);
        } else {
            mGuardar.setValue("Editar perfil");
            mHabilitar.setValue(false);
            String token = ApiClient.leerToken(getApplication());
            if (token != null) {
                ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                Call<String> call = api.modificarUsuario(token, agente);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_LONG).show();
                            sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nombre completo", agente.toString());
                            editor.putString("email", agente.getEmail());
                            editor.apply();
                            ApiClient.guardarToken("Bearer " + response.body(), getApplication());
                            mAgente.postValue(agente);
                        } else {
                            Toast.makeText(getApplication(), "Falla en la actualización", Toast.LENGTH_LONG).show();
                            Log.d("salida", response.message());
                            Log.d("salida", token);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        Log.d("salida", "Falla: " + throwable.getMessage());
                    }
                });
            }
        }

    }

    public void miPerfil(){
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();
            Call<Agente> call = api.miPerfil(token);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Agente> call, Response<Agente> response) {
                    if (response.isSuccessful()) {
                        mHabilitar.setValue(false);
                        mAgente.postValue(response.body());
                    } else {
                        Log.d("salida", "Incorrecto");
                    }
                }
                @Override
                public void onFailure(Call<Agente> call, Throwable throwable) {
                    Log.d("salida", "Falla: " + throwable.getMessage());
                }
            });
        } else {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
            getApplication().startActivity(intent);
        }
    }
}