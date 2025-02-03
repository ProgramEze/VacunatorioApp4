package com.ezequieldiaz.vacunatorioapp4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.ezequieldiaz.vacunatorioapp4.databinding.ActivityLoginBinding;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityViewModel vm;
    private ActivityLoginBinding binding;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        solicitarPermisos();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LoginActivityViewModel.class);
        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etMatricula.getText().toString();
            String password = binding.etPassword.getText().toString();
            vm.logueo(email, password);
            binding.etPassword.setText("");
        });
        binding.tvCambiarPassword.setOnClickListener(v -> vm.enviarEmail(binding.etMatricula.getText().toString()));
        binding.btnLogin.setOnClickListener(view -> vm.logueo(binding.etMatricula.getText().toString(), binding.etPassword.getText().toString()));
        binding.btnFingerprint.setOnClickListener(v -> iniciarAutenticacionBiometrica());
        binding.tvCambiarPassword.setOnClickListener(v -> vm.enviarEmail(binding.etMatricula.getText().toString()));
        executor = ContextCompat.getMainExecutor(this);
        vm.iniciarAutenticacionBiometrica(this);
        iniciarAutenticacionBiometrica();
    }


    // Método para llamar al método en el ViewModel
    private void iniciarAutenticacionBiometrica() {
        vm.iniciarAutenticacionBiometrica(this);
    }


    private void solicitarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
    }
}

