package com.ezequieldiaz.vacunatorioapp4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ezequieldiaz.vacunatorioapp4.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_perfil, R.id.nav_cambiar_password, R.id.nav_agregar_paciente, R.id.nav_agregar_tutor, R.id.nav_turno, R.id.nav_aplicacion, R.id.nav_deslogeo)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Leer la información del usuario desde SharedPreferences
        sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre completo", "example");
        String matricula = sharedPreferences.getString("matricula", "00000000");

        // Actualizar el encabezado del NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderTitle = headerView.findViewById(R.id.nav_header_title);
        TextView navHeaderSubtitle = headerView.findViewById(R.id.nav_header_subtitle);
        navHeaderTitle.setText("Agente: " + nombre);
        navHeaderSubtitle.setText("Mat: " + matricula);
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        if (navController.getCurrentDestination().getId() != R.id.nav_home) {
            navController.navigateUp();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}