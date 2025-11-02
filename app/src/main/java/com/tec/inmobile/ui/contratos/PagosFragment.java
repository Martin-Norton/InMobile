package com.tec.inmobile.ui.contratos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.inmobile.R;
import com.tec.inmobile.databinding.FragmentPagosBinding;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding binding;
    private PagosViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm.getMPagos().observe(getViewLifecycleOwner(), pagos -> {
            PagosAdapter adapter = new PagosAdapter(getContext(), pagos);
            LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.listaPagos.setLayoutManager(layout);
            binding.listaPagos.setAdapter(adapter);
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            vm.recuperarPagos(bundle);
        }

        return root;
    }
}