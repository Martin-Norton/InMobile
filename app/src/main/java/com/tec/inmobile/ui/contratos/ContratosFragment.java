package com.tec.inmobile.ui.contratos;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.tec.inmobile.databinding.FragmentContratosBinding;
import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Inmueble;

import java.util.List;

public class ContratosFragment extends Fragment {
    private FragmentContratosBinding binding;
    private ContratosViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ContratosViewModel.class);
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm.getListaInmueblesContratos().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmueblesContratos) {
                ContratoAdapter ca = new ContratoAdapter(getContext(), inmueblesContratos, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.listaContratos.setLayoutManager(glm);
                binding.listaContratos.setAdapter(ca);
            }
        });
        vm.obtenerListaInmueblesContratos();
        return root;
    }
}
