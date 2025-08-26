package com.ezequieldiaz.vacunatorioapp4.ui.registro;
import static android.app.Activity.RESULT_OK;

import static com.ezequieldiaz.vacunatorioapp4.ui.registro.AgregarPacienteFragment.REQUEST_CODE_SCAN;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarPacienteFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Paciente> mPaciente;
    public AgregarPacienteFragmentViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<String> getMMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }
    public MutableLiveData<Paciente> getMPaciente() {
        if (mPaciente == null) {
            mPaciente = new MutableLiveData<>();
        }
        return mPaciente;
    }
    public void agregadoExitoso(String exito, FragmentActivity activity){
        if(exito.contains("Paciente guardado correctamente")){
            mensaje(exito, activity);
            activity.getSupportFragmentManager().popBackStack();
        } else {
            mensaje(exito, activity);
        }
    }
    public void guardarPaciente(String nombre, String apellido, String dni, String fechaNacimientoTexto, String genero) {
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaNacimientoTexto.isEmpty()
                || genero.equals("Seleccione el género del paciente")) {
            mMensaje.setValue("Por favor, complete todos los campos");
            return;
        }

        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$")) {
            mMensaje.setValue("Nombre inválido. Solo letras y mínimo 2 caracteres.");
            return;
        }

        if (!apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$")) {
            mMensaje.setValue("Apellido inválido. Solo letras y mínimo 2 caracteres.");
            return;
        }

        if (!dni.matches("^[1-9][0-9]{6,7}$")) {
            mMensaje.setValue("DNI inválido. Debe tener 7-8 dígitos y no comenzar con 0.");
            return;
        }
        LocalDate fechaNacimiento = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fechaNacimiento = LocalDate.parse(fechaNacimientoTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate hoy = LocalDate.now();
                if (fechaNacimiento.isAfter(hoy)) {
                    mMensaje.setValue("La fecha de nacimiento no puede ser futura.");
                    return;
                }
                int edad = hoy.getYear() - fechaNacimiento.getYear();
                if (edad < 0 || edad > 120) {
                    mMensaje.setValue("Edad inválida.");
                    return;
                }
            }
        } catch (Exception e) {
            mMensaje.setValue("Fecha inválida. Formato esperado: dd/MM/yyyy");
            return;
        }
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<Void> call = api.registrarPaciente(token, dni, nombre, apellido, fechaNacimiento.toString(), genero);
        Log.d("fechaDeNacimiento", fechaNacimiento.toString());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int errorCode = 0;
                if (response.isSuccessful()) {
                    mMensaje.setValue("Paciente guardado correctamente");
                } else {
                    errorCode = response.code();
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                        if (errorCode == 400) {
                            mMensaje.setValue(errorBody);
                        } else {
                            mMensaje.setValue("Error al guardar el tutor: " + errorCode + " - " + response.message());
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer el cuerpo del error", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Error al guardar paciente: " + t.getMessage());
                mMensaje.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
    public void mensaje(String mensaje, FragmentActivity activity){
        Toast.makeText(activity.getApplication(), mensaje, Toast.LENGTH_LONG).show();
    }
    public void cargarDatos(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String datos = data.getStringExtra("DATOS_ESCANEADOS");
            if (datos != null) {
                String[] partes = datos.split("@");
                if (partes.length >= 8) {
                    Paciente p = new Paciente();
                    p.setApellido(partes[1]);
                    p.setNombre(partes[2]);
                    p.setDni(partes[4]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate fechaParseada = LocalDate.parse(partes[6], formatoEntrada);
                        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String fechaFormateadaSalida = fechaParseada.format(formatoSalida);
                        p.setFechaDeNacimiento(fechaFormateadaSalida);
                    }
                    p.setGenero(partes[3].equalsIgnoreCase("M") ? "Masculino" :
                            partes[3].equalsIgnoreCase("F") ? "Femenino" : "Otro");
                    mPaciente.setValue(p);
                } else {
                    mMensaje.setValue("Datos escaneados incompletos");
                }
            }
        }
    }
}

