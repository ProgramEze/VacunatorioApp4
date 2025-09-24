package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private MutableLiveData<Boolean> mTurnoCargado;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<FechaSeleccionada> mFechaSeleccionada;
    private MutableLiveData<Boolean> mMostrarDialog;
    private MutableLiveData<Integer> selectedTipoVacunaIndex;
    private MutableLiveData<Integer> selectedRelacionIndex;
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

    public LiveData<Boolean> getMCancelarYConfirmar() {
        if (mCancelarYConfirmar == null) {
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

    public void solicitarSeleccionFecha(@NonNull String dni) {
        if (!dni.isBlank()) {
            buscarDNIPaciente(dni);
        }
        mMostrarDialog.setValue(true);
    }

    public void setFechaSeleccionada(FechaSeleccionada fecha) {
        mFechaSeleccionada.setValue(fecha);
        mMostrarDialog.setValue(false);
    }

    public void setFechaYHora(String fecha) {
        mFechaYHora.setValue(fecha);
    }

    public MutableLiveData<Boolean> getMTurnoCargado() {
        if (mTurnoCargado == null) {
            mTurnoCargado = new MutableLiveData<>();
        }
        return mTurnoCargado;
    }

    public void setTurnoCargado(boolean cargado) {
        mTurnoCargado.setValue(cargado);
    }

    public MutableLiveData<Integer> getSelectedTipoVacunaIndex() {
        if (selectedTipoVacunaIndex == null) {
            selectedTipoVacunaIndex = new MutableLiveData<>();
        }
        return selectedTipoVacunaIndex;
    }

    public MutableLiveData<Integer> getSelectedRelacionIndex() {
        if (selectedRelacionIndex == null) {
            selectedRelacionIndex = new MutableLiveData<>();
        }
        return selectedRelacionIndex;
    }

    public void setSelectedTipoVacunaIndex(int index) {
        selectedTipoVacunaIndex.setValue(index);
    }

    public void setSelectedRelacionIndex(int index) {
        selectedRelacionIndex.setValue(index);
    }

    public void cargarSpinners() {
        cargarTipos();
        cargarRelaciones();
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

    public void cargarTurno(String fecha) {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Turno> call = apiService.obtenerTurnoPorFecha(token, fecha);
        Log.d("cargarTurno", fecha);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Turno> call, Response<Turno> response) {
                Log.d("turno", "Código HTTP: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        mConfirmar.setValue("Modificar turno");
                        mTurno.setValue(response.body());
                        mPaciente.setValue(response.body().getPaciente());
                        mTutor.setValue(response.body().getTutor());
                        String citaIso = response.body().getCita();
                        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        Date citaDate = isoFormat.parse(citaIso);
                        SimpleDateFormat argFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
                        String fechaFormateada = argFormat.format(citaDate);
                        mFechaYHora.setValue(fechaFormateada);
                        mCancelarYConfirmar.setValue(true);
                    } catch (DateTimeParseException e) {
                        Log.d("fecha", e.getMessage());
                        mMensaje.setValue("Seleccione la fecha de la cita");
                    } catch (ParseException e) {
                        mMensaje.setValue("Seleccione la fecha de la cita");
                    }
                } else {
                    try {
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

    public void buscarDNIPaciente(String dni) {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        try {
            int dniParseado = Integer.parseInt(dni);
            Call<Paciente> call = apiService.getPaciente(token, dniParseado);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mPaciente.postValue(response.body());
                        Log.d("turno", "Paciente encontrado: " + response.body().getDni());
                    } else {
                        mPaciente.postValue(null);
                        mMensaje.postValue("No se encontró paciente con ese DNI");
                        Log.e("TurnoFragmentViewModel", "Paciente no encontrado: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Paciente> call, Throwable t) {
                    mPaciente.postValue(null);
                    mMensaje.postValue("Error al buscar paciente: " + t.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            mMensaje.setValue("DNI inválido");
            Log.d("buscar DNI", e.getMessage());
        }
    }

    public void buscarDNITutor(String dni) {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        try {
            int dniParseado = Integer.parseInt(dni);
            Call<Tutor> call = apiService.getTutor(token, dniParseado);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mTutor.postValue(response.body());
                        Log.d("turno", "Tutor encontrado: " + response.body().getDni());
                    } else {
                        mTutor.postValue(null);
                        mMensaje.postValue("No se encontró tutor con ese DNI");
                        Log.e("TurnoFragmentViewModel", "Tutor no encontrado: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Tutor> call, Throwable t) {
                    mTutor.postValue(null);
                    mMensaje.postValue("Error al buscar tutor: " + t.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            mMensaje.setValue("DNI inválido");
            Log.d("buscar DNI", e.getMessage());
        }
    }

    public void cargarImagen(int requestCode, int resultCode, Intent data) {
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

    public void limpiarMutables() {
        if (mPaciente != null) mPaciente.setValue(null);
        if (mTutor != null) mTutor.setValue(null);
        if (mFechaYHora != null) mFechaYHora.setValue(null);
        if (mTurno != null) mTurno.setValue(null);
        if (mConfirmar != null) mConfirmar.setValue("Otorgar turno");
        if (mLimpiar != null) mLimpiar.setValue(null);
        if (mMensaje != null) mMensaje.setValue("");
        if (mCancelarYConfirmar != null) mCancelarYConfirmar.setValue(null);
        if (mFechaSeleccionada != null) mFechaSeleccionada.setValue(null);
        if (mMostrarDialog != null) mMostrarDialog.setValue(null);
        if (selectedTipoVacunaIndex != null) selectedTipoVacunaIndex.setValue(0);
        if (selectedRelacionIndex != null) selectedRelacionIndex.setValue(0);
        mLimpiar.setValue(true);
        cargarSpinners();
    }

    public void setLimpiar(Boolean valor) {
        if (mLimpiar != null) mLimpiar.setValue(valor);
    }

    public void guardarTurno(Turno turno) {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();

            Call<Turno> call = api.registrarTurno(token, turno);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Turno> call, Response<Turno> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mTurno.postValue(response.body());
                        mMensaje.postValue("Turno registrado con éxito");
                        mLimpiar.setValue(true);
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Desconocido";
                            mMensaje.postValue("Error al registrar turno: " + response.code() + " - " + errorBody);
                            Log.e("TurnoVM", "Error 400/415: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mMensaje.postValue("Error al registrar turno: " + response.code());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Turno> call, Throwable t) {
                    mMensaje.postValue("Falla en el alta: " + t.getMessage());
                    Log.e("TurnoVM", "Falla en guardarTurno", t);
                }
            });
        } else {
            mMensaje.postValue("Token inválido o no disponible");
        }
    }

    public void modificarTurno(Turno turno) {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();

            Call<Turno> call = api.modificarTurno(token, turno.getId(), turno);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Turno> call, Response<Turno> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mTurno.postValue(response.body());
                        mMensaje.postValue("Turno modificado con éxito");
                        mLimpiar.setValue(true);
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Desconocido";
                            mMensaje.postValue("Error al modificar turno: " + response.code() + " - " + errorBody);
                            Log.e("TurnoVM", "Error 400/415 al modificar: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mMensaje.postValue("Error al modificar turno: " + response.code());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Turno> call, Throwable t) {
                    mMensaje.postValue("Falla en la modificación: " + t.getMessage());
                    Log.e("TurnoVM", "Falla en modificarTurno", t);
                }
            });
        } else {
            mMensaje.postValue("Token inválido o no disponible");
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

    public void confirmarAplicacion() {
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

    public void limpiarMensaje() {
        mMensaje.setValue(null);
    }

}