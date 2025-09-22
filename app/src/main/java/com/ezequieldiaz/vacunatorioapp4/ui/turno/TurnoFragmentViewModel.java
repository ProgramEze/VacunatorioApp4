package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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

import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;
import com.ezequieldiaz.vacunatorioapp4.util.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnoFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Paciente> mPaciente;
    private MutableLiveData<List<TipoDeVacuna>> mListaTipo;
    private MutableLiveData<Tutor> mTutor;
    private MutableLiveData<List<String>> mRelaciones;
    private MutableLiveData<String> mFechaYHora;
    private MutableLiveData<Turno> mTurno;
    private MutableLiveData<String> mConfirmar;
    private MutableLiveData<Boolean> mLimpiar;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Boolean> mCancelarYConfirmar;
    private SharedPreferences sharedPreferences;
    private SingleLiveEvent<Long> mShowDatePickerEvent;
    private MutableLiveData<Boolean> mEventoMostrarDialogoMesAnio;
    private MutableLiveData<FechaSeleccionada> mFechaSeleccionada;
    private MutableLiveData<Boolean> mMostrarDialog;

    public TurnoFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Paciente> getMPaciente() {
        if (mPaciente == null) {
            mPaciente = new MutableLiveData<>();
        }
        return mPaciente;
    }

    public LiveData<List<TipoDeVacuna>> getMListaTipo() {
        if (mListaTipo == null) {
            mListaTipo = new MutableLiveData<>();
        }
        return mListaTipo;
    }

    public LiveData<Tutor> getMTutor() {
        if (mTutor == null) {
            mTutor = new MutableLiveData<>();
        }
        return mTutor;
    }

    public LiveData<List<String>> getMRelaciones() {
        if (mRelaciones == null) {
            mRelaciones = new MutableLiveData<>();
        }
        return mRelaciones;
    }

    public LiveData<String> getMFechaYHora() {
        if (mFechaYHora == null) {
            mFechaYHora = new MutableLiveData<>();
        }
        return mFechaYHora;
    }

    public LiveData<Turno> getMTurno() {
        if (mTurno == null) {
            mTurno = new MutableLiveData<>();
        }
        return mTurno;
    }

    public LiveData<String> getMConfirmar() {
        if (mConfirmar == null) {
            mConfirmar = new MutableLiveData<>();
        }
        return mConfirmar;
    }

    public LiveData<Boolean> getMLimpiar() {
        if (mLimpiar == null) {
            mLimpiar = new MutableLiveData<>();
        }
        return mLimpiar;
    }

    public LiveData<String> getMMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public LiveData<Boolean> getMCancelarYConfirmar(){
        if(mCancelarYConfirmar == null){
            mCancelarYConfirmar = new MutableLiveData<>();
        }
        return mCancelarYConfirmar;
    }

    public LiveData<FechaSeleccionada> getMFechaSeleccionada() {
        if (mFechaSeleccionada == null) {
            mFechaSeleccionada = new MutableLiveData<>();
        }
        return mFechaSeleccionada;
    }

    public LiveData<Boolean> getMMostrarDialog() {
        if (mMostrarDialog == null) {
            mMostrarDialog = new MutableLiveData<>();
        }
        return mMostrarDialog;
    }

    public void solicitarSeleccionFecha() {
        mMostrarDialog.setValue(true);
    }
    public void setFechaSeleccionada(FechaSeleccionada fecha) {
        mFechaSeleccionada.setValue(fecha);
        mMostrarDialog.setValue(false);
    }

    public void cargarTipos() {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<List<TipoDeVacuna>> call = apiService.getTiposDeVacunas(token);
        List<TipoDeVacuna> listaParaSpinner = new ArrayList<>();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TipoDeVacuna>> call, Response<List<TipoDeVacuna>> response) {
                if (response.isSuccessful()) {
                    List<TipoDeVacuna> tiposRecibidos = response.body();
                    TipoDeVacuna opcionPorDefecto = new TipoDeVacuna();
                    opcionPorDefecto.setId(0);
                    opcionPorDefecto.setNombre("Seleccione la vacuna que se aplicará en la cita");
                    listaParaSpinner.add(opcionPorDefecto);
                    if (tiposRecibidos != null) {
                        listaParaSpinner.addAll(tiposRecibidos);
                    }
                    mListaTipo.setValue(listaParaSpinner);
                } else {
                    Log.e("TurnoFragmentViewModel", "Error al obtener tipos de vacunas: " + response.code() + " " + response.message());
                    TipoDeVacuna opcionError = new TipoDeVacuna();
                    opcionError.setId(0);
                    opcionError.setNombre("Error al cargar vacunas");
                    listaParaSpinner.add(opcionError);
                    mListaTipo.setValue(listaParaSpinner);
                }
            }

            @Override
            public void onFailure(Call<List<TipoDeVacuna>> call, Throwable t) {
                Log.e("TurnoFragmentViewModel", "Falla de red al obtener tipos de vacunas: " + t.getMessage());
                TipoDeVacuna opcionError = new TipoDeVacuna();
                opcionError.setId(0);
                opcionError.setNombre("Error al cargar vacunas");
                listaParaSpinner.add(opcionError);
                mListaTipo.setValue(listaParaSpinner);
            }
        });
    }

    public void cargarRelaciones() {
        ArrayList<String> relaciones = new ArrayList<>();
        relaciones.add("Seleccione la relación con el tutor");
        relaciones.add("Madre");
        relaciones.add("Padre");
        relaciones.add("Tutor");
        relaciones.add("Otro");
        mRelaciones.setValue(relaciones);
    }

    public void cargarSpinners() {
        cargarTipos();
        cargarRelaciones();
    }

    public void cargarTurno(String fecha) {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Turno> call = apiService.obtenerTurnoPorFecha(token, fecha);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Turno> call, Response<Turno> response) {
                Log.d("turno", "Código HTTP: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    mConfirmar.setValue("Modificar turno");
                    mTurno.setValue(response.body());
                    mPaciente.setValue(response.body().getPaciente());
                    mTutor.setValue(response.body().getTutor());
                    mCancelarYConfirmar.setValue(true);
                } else {
                    try {
                        // Si hay mensaje de error del backend
                        String errorMsg = response.errorBody() != null
                                ? response.errorBody().string()
                                : "Error desconocido";

                        Log.e("turno", "Error del backend: " + errorMsg);
                        mMensaje.setValue(errorMsg);

                    } catch (IOException e) {
                        e.printStackTrace();
                        mMensaje.setValue("Error al procesar la respuesta del servidor");
                    }
                    mCancelarYConfirmar.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Turno> call, Throwable throwable) {
                Log.e("turno", "Fallo de red o conversión", throwable);
                mMensaje.setValue("Error de conexión: " + throwable.getMessage());
                mCancelarYConfirmar.setValue(false);
            }
        });
    }

    public void buscarDNIPaciente(String dni){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        try{
            int dniParseado = Integer.parseInt(dni);
            Call<Paciente> call = apiService.getPaciente(token, dniParseado);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                    if (response.isSuccessful()) {
                        mPaciente.postValue(response.body());
                        Log.d("turno", "Paciente: " + response.body().getDni());
                    } else {
                        mMensaje.postValue("Error al obtener el paciente");
                        mPaciente.postValue(null);
                        Log.e("TurnoFragmentViewModel", "Error al obtener un paciente: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Paciente> call, Throwable throwable) {
                    mPaciente.setValue(null);
                }
            });
        }catch (NumberFormatException e){
            mMensaje.setValue("Falta ingresar el DNI del paciente");
            Log.d("buscar DNI", e.getMessage());
        }
    }

    public void cargarImagen(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && data != null) {
            String datosEscaneados = data.getStringExtra("DATOS_ESCANEADOS");
            if (datosEscaneados != null) {
                String[] partes = datosEscaneados.split("@");
                if (partes.length >= 5) {
                    mMensaje.setValue("DNI escaneado correctamente");
                    if (requestCode == 1) {
                        buscarDNIPaciente(partes[4]);
                    } else if (requestCode == 2) {
                        buscarDNITutor(partes[4]);
                    }
                } else {
                    mMensaje.setValue("Formato del código de barras inválido");
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            mMensaje.setValue("Escaneo cancelado");
        }
    }

    public void buscarDNITutor(String dni){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        Log.d("turno", dni);
        String token = ApiClient.leerToken(getApplication());
        try{
            int dniParseado = Integer.parseInt(dni);
            Call<Tutor> call = apiService.getTutor(token, Integer.parseInt(dni));
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                    if (response.isSuccessful()) {
                        Log.d("turno", "Tutor: " + response.body().getNombre());
                        mTutor.postValue(response.body());
                    } else {
                        mTutor.postValue(null);
                        Log.e("TurnoFragmentViewModel", "Error al obtener el tutor: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Tutor> call, Throwable throwable) {

                }
            });
        }catch (NumberFormatException e){
            mMensaje.setValue("Falta ingresar el DNI del tutor");
            Log.d("buscar DNI", e.getMessage());
        }
    }

    public void limpiarMutables(){
        mPaciente.setValue(null);
        mTutor.setValue(null);
        mTurno.setValue(null);
        mConfirmar.setValue("Otorgar turno");
        mCancelarYConfirmar.setValue(false);
    }

    public void guardarTurno(TipoDeVacuna tDV, String rT, String fecha, String boton) {
        Log.d("guardar", rT+"");
        try {
            if (tDV == null || rT.equalsIgnoreCase("Seleccione la relación con el tutor")) {
                mMensaje.setValue("Seleccione el tipo de vacuna, la relación con el tutor y/o el horario de la cita");
                return;
            }
            if(mPaciente.getValue() == null){
                mMensaje.setValue("Hubo un error al intentar cargar el paciente, vuelva a intentarlo por favor");
                return;
            }
            if(mTutor.getValue() == null){
                mMensaje.setValue("Hubo un error al intentar cargar el tutor, vuelva a intentarlo por favor");
                return;
            }
            String token = ApiClient.leerToken(getApplication());
            sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String matricula = sharedPreferences.getString("matricula", "00000000");
            Turno turno = new Turno();
            turno.setId(mTurno.getValue().getId());
            turno.setPacienteId(mPaciente.getValue().getId());
            turno.setTipoDeVacunaId(tDV.getId());
            turno.setTutorId(mTutor.getValue().getId());
            turno.setAgenteId(matricula);
            turno.setRelacionTutor(rT);
            LocalDateTime cita = LocalDateTime.of(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())), LocalTime.parse(mFechaYHora.getValue()+"", DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())));
            turno.setCita(cita+":00");
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();
            if (token != null) {
                Log.d("turno", boton.equals("Otorgar turno")+"");
                if(boton.equals("Otorgar turno")){
                    Log.d("turno", mTutor.getValue().getDni());
                    Log.d("turno", mPaciente.getValue().getDni());
                    Log.d("otorga", "Turno otorgado");
                    turno.setPacienteId(mPaciente.getValue().getId());
                    turno.setTutorId(mTutor.getValue().getId());
                    Log.d("turno", turno.toString());
                    Call<Turno> call = api.registrarTurno(token, turno.getPacienteId(), turno.getTipoDeVacunaId(), turno.getTutorId(), turno.getAgenteId(), 0, turno.getCita(), turno.getRelacionTutor());
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Turno> call, Response<Turno> response) {
                            if (response.isSuccessful()) {
                                Log.d("salida", "Éxito: " + response.code() + " - " + response.message());
                                mLimpiar.setValue(true);
                                mPaciente.setValue(null);
                                mTutor.setValue(null);
                                Toast.makeText(getApplication(), "Turno dado de alta con éxito", Toast.LENGTH_LONG).show();
                            } else {
                                String errorMessage = "Falla en el alta del Turno. Código: " + response.code();
                                String detailedErrorMessage = response.message();

                                if (response.errorBody() != null) {
                                    try {
                                        // Convierte el errorBody a String para ver el mensaje del backend
                                        detailedErrorMessage = response.errorBody().string();
                                        // Ahora 'detailedErrorMessage' debería contener mensajes como
                                        // "El paciente no existe.", "La fecha y hora seleccionadas ya están ocupadas...", etc.
                                    } catch (IOException e) {
                                        Log.e("salida", "Error al leer errorBody", e);
                                        // detailedErrorMessage se queda con el response.message() si hay error al leer
                                    }
                                }

                                // Muestra el mensaje detallado en el Toast y en el Log
                                Toast.makeText(getApplication(), errorMessage + ": " + detailedErrorMessage, Toast.LENGTH_LONG).show();
                                Log.d("salida", "Falla: " + response.code() + " - " + detailedErrorMessage);
                            }
                        }

                        @Override
                        public void onFailure(Call<Turno> call, Throwable throwable) {
                            // Esto es para errores de red o problemas antes de que la petición llegue al servidor
                            // o antes de que la respuesta sea procesada.
                            Log.e("salida", "Falla de conexión/petición: " + throwable.getMessage(), throwable);
                            Toast.makeText(getApplication(), "Error de red: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.d("turno", "Turno modificado");
                    Turno turnoViejo = mTurno.getValue();
                    Log.d("turno cargado", turnoViejo.toString());
                    turnoViejo.setId(mTurno.getValue().getId());
                    turnoViejo.setPacienteId(mPaciente.getValue().getId());
                    turnoViejo.setTipoDeVacunaId(turno.getTipoDeVacunaId());
                    turnoViejo.setTutorId(mTutor.getValue().getId());
                    turnoViejo.setAgenteId(matricula);
                    turnoViejo.setCita(turno.getCita());
                    Log.d("turno", "Turno cambiado: " + turno);
                    Log.d("turno", "Turno viejo cambiado: " + turnoViejo);
                    Call<Turno> call = api.modificarTurno(token, turnoViejo.getId(), turnoViejo.getPacienteId(), turnoViejo.getTipoDeVacunaId(), turnoViejo.getTutorId(), matricula, turnoViejo.getAplicacionId(), turnoViejo.getCita(), turnoViejo.getRelacionTutor());
                     call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Turno> call, Response<Turno> response) {
                            if (response.isSuccessful()) {
                                Log.d("salida", response.isSuccessful() + "");
                                mLimpiar.setValue(true);
                                mConfirmar.setValue("Otorgar turno");
                                Toast.makeText(getApplication(), "Turno modificado con exito", Toast.LENGTH_LONG).show();
                            } else {
                                String errorMessage = "Falla en el alta del Turno. Código: " + response.code();
                                String detailedErrorMessage = response.message(); // Mensaje genérico como "Bad Request"


                                if (response.errorBody() != null) {
                                    try {
                                        // Convierte el errorBody a String para ver el mensaje del backend
                                        detailedErrorMessage = response.errorBody().string();
                                        // Ahora 'detailedErrorMessage' debería contener mensajes como
                                        // "El paciente no existe.", "La fecha y hora seleccionadas ya están ocupadas...", etc.
                                    } catch (IOException e) {
                                        Log.e("salida", "Error al leer errorBody", e);
                                        // detailedErrorMessage se queda con el response.message() si hay error al leer
                                    }
                                }

                                // Muestra el mensaje detallado en el Toast y en el Log
                                Toast.makeText(getApplication(), errorMessage + ": " + detailedErrorMessage, Toast.LENGTH_LONG).show();
                                Log.d("salida", "Falla: " + response.code() + " - " + detailedErrorMessage);
                            }
                        }

                        @Override
                        public void onFailure(Call<Turno> call, Throwable throwable) {
                            Log.d("salida", "Falla: " + throwable.getMessage());
                        }
                    });
                }
            }
        } catch (DateTimeParseException e) {
            Log.d("fecha", e.getMessage());
            mMensaje.setValue("Seleccione la fecha de la cita");
        } catch (NumberFormatException e) {
            mMensaje.setValue("Los campos DNI de paciente y tutor son obligatorios");
        }  catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void cancelarTurno() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        if (token != null) {
            Call<Void> call = api.cancelarTurno(token, mTurno.getValue().getId());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mMensaje.setValue("Turno cancelado correctamente");
                        mCancelarYConfirmar.setValue(false); // Ocultar el botón
                        Log.d("cancelar", "Turno cancelado correctamente");
                        mLimpiar.setValue(true);
                    } else {
                        mMensaje.setValue("No se pudo cancelar el turno");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    mMensaje.setValue("Error: " + throwable.getMessage());
                    Log.d("cancelar", "Falla: " + throwable.getMessage());
                }
            });
        }
    }

    public void confirmarAplicacion(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        if (token != null) {
            Call<Void> call = api.confirmarAplicacion(token, mTurno.getValue().getAplicacionId());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mMensaje.setValue("Vacuna aplicada correctamente");
                        mCancelarYConfirmar.setValue(false); // Ocultar el botón
                        Log.d("confirmar", "Vacuna aplicada correctamente");
                        mLimpiar.setValue(true);
                    } else {
                        mMensaje.setValue(response.toString());
                        Log.d("confirmar", response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    mMensaje.setValue("Error: " + throwable.getMessage());
                    Log.d("confirmar", "Falla: " + throwable.getMessage());
                }
            });
        }
    }

    public void cargarHorariosLibres(String fechaSeleccionada) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        if (token != null) {
            Call<Void> call = api.confirmarAplicacion(token, mTurno.getValue().getAplicacionId());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mMensaje.setValue("Vacuna aplicada correctamente");
                        mCancelarYConfirmar.setValue(false);
                        Log.d("confirmar", "Vacuna aplicada correctamente");
                        mLimpiar.setValue(true);
                    } else {
                        mMensaje.setValue(response.toString());
                        Log.d("confirmar", response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    mMensaje.setValue("Error: " + throwable.getMessage());
                    Log.d("confirmar", "Falla: " + throwable.getMessage());
                }
            });
        }
    }
}