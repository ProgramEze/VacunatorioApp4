package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<List<TipoDeVacuna>> mListaTipo;
    private MutableLiveData<List<String>> mRelaciones;
    private MutableLiveData<String> mFecha;
    private MutableLiveData<List<String>> mHorarios;
    private MutableLiveData<Turno> mTurno;
    private MutableLiveData<Paciente> mPaciente;
    private MutableLiveData<String> mConfirmar;
    private MutableLiveData<Tutor> mTutor;
    private MutableLiveData<Boolean> mLimpiar;
    private MutableLiveData<String> mMensaje;
    private SharedPreferences sharedPreferences;

    // LiveData para señalizar al Fragment que muestre el DatePickerDialog
    private SingleLiveEvent<Long> mShowDatePickerEvent;

    public TurnoFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TipoDeVacuna>> getMListaTipo() {
        if (mListaTipo == null) {
            mListaTipo = new MutableLiveData<>();
        }
        return mListaTipo;
    }

    public LiveData<List<String>> getMRelaciones() {
        if (mRelaciones == null) {
            mRelaciones = new MutableLiveData<>();
        }
        return mRelaciones;
    }

    public LiveData<String> getMFecha() {
        if (mFecha == null) {
            mFecha = new MutableLiveData<>();
        }
        return mFecha;
    }

    public LiveData<List<String>> getMHorarios() {
        if (mHorarios == null) {
            mHorarios = new MutableLiveData<>();
        }
        return mHorarios;
    }

    public LiveData<Turno> getMTurno() {
        if (mTurno == null) {
            mTurno = new MutableLiveData<>();
        }
        return mTurno;
    }

    public LiveData<Paciente> getMPaciente() {
        if (mPaciente == null) {
            mPaciente = new MutableLiveData<>();
        }
        return mPaciente;
    }

    public LiveData<Tutor> getMTutor() {
        if (mTutor == null) {
            mTutor = new MutableLiveData<>();
        }
        return mTutor;
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

    public LiveData<String> getMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public SingleLiveEvent<Long> getShowDatePickerEvent() {
        if(mShowDatePickerEvent == null){
            mShowDatePickerEvent = new SingleLiveEvent<>();
        }
        return mShowDatePickerEvent;
    }

    public int cargarTutor(String tutor){
        ArrayList<String> relaciones = new ArrayList<>();
        relaciones.add("Madre");
        relaciones.add("Padre");
        relaciones.add("Tutor");
        relaciones.add("Otro");
        return relaciones.indexOf(tutor);
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

    public void cargarHorarios() {
        ArrayList<String> horarios = new ArrayList<>();
        horarios.add("Seleccione el horario de la cita");
        horarios.add("08:00");
        horarios.add("08:15");
        horarios.add("08:30");
        horarios.add("08:45");
        horarios.add("09:00");
        horarios.add("09:15");
        horarios.add("09:30");
        horarios.add("09:45");
        horarios.add("10:00");
        horarios.add("10:15");
        horarios.add("10:30");
        horarios.add("10:45");
        horarios.add("11:00");
        horarios.add("11:15");
        horarios.add("11:30");
        horarios.add("11:45");
        horarios.add("12:00");
        horarios.add("12:15");
        horarios.add("12:30");
        horarios.add("12:45");
        horarios.add("13:00");
        horarios.add("13:15");
        horarios.add("13:30");
        horarios.add("13:45");
        horarios.add("14:00");
        horarios.add("14:15");
        horarios.add("14:30");
        horarios.add("14:45");
        horarios.add("15:00");
        horarios.add("15:15");
        horarios.add("15:30");
        horarios.add("15:45");
        horarios.add("16:00");
        mHorarios.setValue(horarios);
    }

    public void cargarSpinners() {
        cargarTipos();
        cargarRelaciones();
        cargarHorarios();
    }

    public void cargarTurno(String fecha){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Turno> call = apiService.obtenerTurnoPorFecha(token, fecha);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Turno> call, Response<Turno> response) {
                mTurno.setValue(response.body());
                mPaciente.setValue(response.body().getPaciente());
                mTutor.setValue(response.body().getTutor());
                mConfirmar.setValue("Modificar turno");
            }

            @Override
            public void onFailure(Call<Turno> call, Throwable throwable) {
                Log.d("salida", throwable.getMessage());
                mMensaje.setValue("Error al obtener el turno");
            }
        });
    }

    public boolean fechaClickeada(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long fechaSeleccionada = Calendar.getInstance().getTimeInMillis();
            mShowDatePickerEvent.setValue(fechaSeleccionada);
            return true;
        }
        return false;

    }

    public void fechaSeleccionada(int year, int month, int dayOfMonth) {
        String fechaElegida = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        mFecha.setValue(fechaElegida);
    }

    public void buscarDNIPaciente(String dni){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Paciente> call = apiService.getPaciente(token, Integer.parseInt(dni));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    mPaciente.setValue(response.body());
                    Log.d("turno", "Paciente: " + response.body().getDni());
                } else {
                    mMensaje.setValue("Error al obtener el paciente");
                    mPaciente.setValue(null);
                    Log.e("TurnoFragmentViewModel", "Error al obtener un paciente: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable throwable) {
                mPaciente.setValue(null);
            }
        });
    }

    public void cambioDNIPaciente(String dni) {
        if(dni.length() == 7 || dni.length() == 8){
            buscarDNIPaciente(dni);
        }
    }

    public void cambioDNITutor(String dni) {
        if(mTutor.getValue() == null){
            if(dni.length() == 7 || dni.length() == 8){
                buscarDNIPaciente(dni);
            }
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
        Call<Tutor> call = apiService.getTutor(token, Integer.parseInt(dni));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if (response.isSuccessful()) {
                    Log.d("turno", "Tutor: " + response.body().getNombre());
                    mTutor.setValue(response.body());
                } else {
                    Log.e("TurnoFragmentViewModel", "Error al obtener el tutor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Tutor> call, Throwable throwable) {

            }
        });
    }


    public void guardarTurno(TipoDeVacuna tDV, int rT, int hor, String fecha, String boton) {
        try {
            if (tDV == null || rT == 0 || hor == 0) {
                mMensaje.setValue("Seleccione el tipo de vacuna, la relación con el tutor y/o el horario de la cita");
                return;
            }
            if(mPaciente.getValue() == null || mTutor.getValue() == null){
                mMensaje.setValue("Hubo un error al intentar cargar el paciente o el tutor, vuelva a intentarlo por favor");
                return;
            }
            String token = ApiClient.leerToken(getApplication());
            sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String matricula = sharedPreferences.getString("matricula", "00000000");
            Turno turno = new Turno();
            turno.setPacienteId(mPaciente.getValue().getId());
            turno.setTipoDeVacunaId(tDV.getId());
            turno.setTutorId(rT);
            turno.setAgenteId(matricula);
            LocalDateTime cita = LocalDateTime.of(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())), LocalTime.parse(hor+"", DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())));
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
                    turnoViejo.setPacienteId(mPaciente.getValue().getId());
                    turnoViejo.setTipoDeVacunaId(turno.getTipoDeVacunaId());
                    turnoViejo.setTutorId(mTutor.getValue().getId());
                    turnoViejo.setAgenteId(matricula);
                    turnoViejo.setCita(turno.getCita());
                    Log.d("turno", "Turno cambiado: " + turno);
                    Log.d("turno", "Turno viejo cambiado: " + turnoViejo);
                    Call<Turno> call = api.modificarTurno(token, turnoViejo.getPacienteId(), turnoViejo.getTipoDeVacunaId(), turnoViejo.getTutorId(), matricula, turnoViejo.getAplicacionId(), turnoViejo.getCita(), turnoViejo.getRelacionTutor());
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
            mMensaje.setValue("Seleccione la fecha de la cita");
        } catch (NumberFormatException e) {
            mMensaje.setValue("Los campos DNI de paciente y tutor son obligatorios");
        }  catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
