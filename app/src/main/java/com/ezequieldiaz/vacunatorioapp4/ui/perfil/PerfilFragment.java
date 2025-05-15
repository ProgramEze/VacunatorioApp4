package com.ezequieldiaz.vacunatorioapp4.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentPerfilBinding;
import com.ezequieldiaz.vacunatorioapp4.model.Agente;
import com.google.android.material.navigation.NavigationView;

public class PerfilFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private PerfilFragmentViewModel vm;
    private FragmentPerfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PerfilFragmentViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm.getMAgente().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Agente agente) {
                binding.etMatricula.setText(agente.getMatricula() + "");
                binding.etNombre.setText(agente.getNombre());
                binding.etApellido.setText(agente.getApellido());
                binding.etEmailPerfil.setText(agente.getEmail());
                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String nombre = sharedPreferences.getString("nombre completo", "example");
                String email = sharedPreferences.getString("email", "email@example.com");
                View headerView = navigationView.getHeaderView(0);
                TextView navHeaderTitle = headerView.findViewById(R.id.nav_header_title);
                TextView navHeaderSubtitle = headerView.findViewById(R.id.nav_header_subtitle);
                navHeaderTitle.setText(nombre);
                navHeaderSubtitle.setText(email);
            }
        });
        vm.getMGuardar().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnEditar.setText(s);
            }
        });
        vm.getMHabilitar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etMatricula.setFocusableInTouchMode(aBoolean);
                binding.etMatricula.clearFocus();
                binding.etNombre.setFocusableInTouchMode(aBoolean);
                binding.etNombre.clearFocus();
                binding.etApellido.setFocusableInTouchMode(aBoolean);
                binding.etApellido.clearFocus();
                binding.etEmailPerfil.setFocusableInTouchMode(aBoolean);
                binding.etEmailPerfil.clearFocus();
            }
        });
        binding.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agente a = new Agente();
                a.setMatricula(Integer.parseInt(binding.etMatricula.getText().toString()));
                a.setNombre(binding.etNombre.getText().toString());
                a.setApellido(binding.etApellido.getText().toString());
                a.setEmail(binding.etEmailPerfil.getText().toString());
                a.setClave("clave");
                a.setEstado(true);
                Log.d("salida", a.toString());
                vm.editarDatos(binding.btnEditar.getText().toString(), a);
            }
        });
        binding.btnCambiarPasswordVieja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_cambiar_password, null);
            }
        });
        vm.miPerfil();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}