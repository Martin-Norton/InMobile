package com.tec.inmobile.ui.contratos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Inquilino;
import com.tec.inmobile.models.Pagos;

import java.util.List;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding binding;
    private PagosViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Lista de Pagos");
        View root = binding.getRoot();

        vm.getMPagos().observe(getViewLifecycleOwner(), new Observer<List<Pagos>>() {
            @Override
            public void onChanged(List<Pagos> pagos) {
                PagosAdapter adapter = new PagosAdapter(getContext(), pagos);
                LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                binding.listaPagos.setLayoutManager(layout);
                binding.listaPagos.setAdapter(adapter);
            }
        });

        Contrato contrato = (Contrato) getArguments().getSerializable("contratoBundle");
        vm.recuperarPagos(contrato);
        return root;
    }
}