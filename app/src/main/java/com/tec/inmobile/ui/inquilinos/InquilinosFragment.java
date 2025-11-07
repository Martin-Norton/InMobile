package com.tec.inmobile.ui.inquilinos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tec.inmobile.databinding.FragmentInquilinosBinding;
import com.tec.inmobile.models.Inquilino;

public class InquilinosFragment extends Fragment {
    private InquilinosViewModel vm;
    private FragmentInquilinosBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InquilinosViewModel.class);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Detalles del Inquilino");

        Inquilino inquilino = (Inquilino) getArguments().getSerializable("inquilinoBundle");

        vm.traerInquilino(inquilino);
        vm.getMInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                binding.tvCodigo.setText(String.valueOf(inquilino.getIdInquilino()));
                binding.tvNombre.setText(inquilino.getNombre());
                binding.tvApellido.setText(inquilino.getApellido());
                binding.tvDni.setText(inquilino.getDni());
                binding.tvEmail.setText(inquilino.getEmail());
                binding.tvTelefono.setText(inquilino.getTelefono());
            }
        });

        return binding.getRoot();
    }
}
