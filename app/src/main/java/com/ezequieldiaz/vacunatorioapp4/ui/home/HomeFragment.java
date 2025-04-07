package com.ezequieldiaz.vacunatorioapp4.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentHomeBinding;

// En HomeFragment
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeFragmentViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.irAFragment(new HomeFragmentViewModel.Navegacion(R.id.nav_agregar_paciente, null));
            }
        });

        binding.btnCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.irAFragment(new HomeFragmentViewModel.Navegacion(R.id.nav_cita, null));
            }
        });

        binding.btnAplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.irAFragment(new HomeFragmentViewModel.Navegacion(R.id.nav_aplicacion, null));
            }
        });

        binding.btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.irAFragment(new HomeFragmentViewModel.Navegacion(R.id.nav_perfil, null));
            }
        });

        binding.btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.irAFragment(new HomeFragmentViewModel.Navegacion(R.id.nav_deslogeo, null));
            }
        });

        viewModel.getNavegacion().observe(getViewLifecycleOwner(), new Observer<HomeFragmentViewModel.Navegacion>() {
            @Override
            public void onChanged(HomeFragmentViewModel.Navegacion navegacion) {
                if (navegacion != null) {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(navegacion.destino, navegacion.args);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}