package com.ezequieldiaz.vacunatorioapp4.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.ezequieldiaz.vacunatorioapp4.model.Agente;
import com.ezequieldiaz.vacunatorioapp4.model.Aplicacion;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;
import com.ezequieldiaz.vacunatorioapp4.model.Laboratorio;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {
    public static final String URL = "http://192.168.1.3:5000/";
    private static MisEndPoints mep;

    public static MisEndPoints getEndPoints(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mep = retrofit.create(MisEndPoints.class);
        return mep;
    }

    public interface MisEndPoints {
        @FormUrlEncoded
        @POST("Agentes/login")
        Call<String> login(@Field("Matricula") String u, @Field("Clave") String c);

        @GET("Agentes")
        Call<Agente> miPerfil(@Header("Authorization") String token);

        @PUT("Agentes")
        Call<String> modificarUsuario(@Header("Authorization") String token, @Body Agente agente);

        @FormUrlEncoded
        @PUT("Agentes/cambiarviejacontraseña")
        Call<Void> cambiarPassword(@Header("Authorization") String token, @Field("ClaveVieja") String claveVieja, @Field("ClaveNueva") String claveNueva, @Field("RepetirClaveNueva") String repetirClaveNueva);

        @FormUrlEncoded
        @POST("Agentes/olvidecontraseña")
        Call<Void> enviarEmail(@Field("matricula") String matricula);

        //Endpoints de Laboratorio
        @GET("Laboratorios")
        Call<List<Laboratorio>> getLaboratorios(@Header("Authorization") String token);

        @GET("Laboratorios/{id}")
        Call<Laboratorio> getLaboratorio(@Header("Authorization") String token, @Path("id") int id);

        //Endpoints de Aplicación
        @GET("Aplicaciones")
        Call<List<Aplicacion>> getAplicaciones(@Header("Authorization") String token);

        @PUT("Aplicaciones/{id}")
        Call<Void> confirmarAplicacion(@Header("Authorization") String token, @Path("id") int id);

        @FormUrlEncoded
        @PUT("Aplicaciones/{id}")
        Call<Void> modificarAplicacion(@Header("Authorization") String token, @Path("id") int id);

        //Endpoints de Tutores
        @GET("Tutores")
        Call<List<Tutor>> getTutores(@Header("Authorization") String token);

        @GET("Tutores/{dni}")
        Call<Tutor> getTutor(@Header("Authorization") String token, @Path("dni") int id);

        @GET("Pacientes/{dni}")
        Call<Paciente> getPaciente(@Header("Authorization") String token, @Path("dni") int id);

        @FormUrlEncoded
        @POST("tutores")
        Call<Void> registrarTutor(
                @Header("Authorization") String token,
                @Field("dni") String dni,
                @Field("nombre") String nombre,
                @Field("apellido") String apellido,
                @Field("telefono") String telefono,
                @Field("email") String email
        );

        @FormUrlEncoded
        @POST("pacientes")
        Call<Void> registrarPaciente(
            @Header("Authorization") String token,
            @Field("dni") String dni,
            @Field("nombre") String nombre,
            @Field("apellido") String apellido,
            @Field("fechaDeNacimiento") String fechaNacimiento,
            @Field("genero") String genero
        );

        @GET("turnos/porfecha")
        Call<Turno> obtenerTurnoPorFecha(
                @Header("Authorization") String token,
                @Query("fecha") String fecha
        );

        @FormUrlEncoded
        @POST("turnos")
        Call<Turno> registrarTurno(
            @Header("Authorization") String token,
            @Field("PacienteId") int pacienteId,
            @Field("TipoDeVacunaId") int tipoDeVacunaId,
            @Field("TutorId") int tutorId,
            @Field("AgenteId") String agenteId,
            @Field("AplicacionId") int aplicacionId,
            @Field("Cita") String cita,
            @Field("RelacionTutor") String relacionTutor
        );

        @FormUrlEncoded
        @PUT("turnos/{id}")
        Call<Turno> modificarTurno(
                @Header("Authorization") String token,
                @Path("id") int id,
                @Field("PacienteId") int pacienteId,
                @Field("TipoDeVacunaId") int tipoDeVacunaId,
                @Field("TutorId") int tutorId,
                @Field("AgenteId") String agenteId,
                @Field("AplicacionId") int aplicacionId,
                @Field("Cita") String cita,
                @Field("RelacionTutor") String relacionTutor
        );

        @PUT("turnos/cancelarturno/{id}")
        Call<Void> cancelarTurno(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @GET("Turnos/disponibilidad/{anio}/{mes}")
        Call<List<FechasResponse>> getDisponibilidadMes(
                @Header("Authorization") String token,
                @Path("anio") int anio,
                @Path("mes") int mes
        );

        @GET("Turnos/por-paciente")
        Call<List<FechasResponse>> getTurnosPorPaciente(
                @Header("Authorization") String token,
                @Query("pacienteId") int pacienteId,
                @Query("anio") int anio,
                @Query("mes") int mes
        );

        @GET("TipoDeVacunas")
        Call<List<TipoDeVacuna>> getTiposDeVacunas(@Header("Authorization") String token);

        @GET("tiposdevacunas/{id}")
        Call<TipoDeVacuna> getTipoDeVacuna(@Header("Authorization") String token, @Path("id") int id);

    }

    public static void guardarToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String leerToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token",null);
    }

    public static void eliminarToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", "");
        editor.apply();
    }
}
