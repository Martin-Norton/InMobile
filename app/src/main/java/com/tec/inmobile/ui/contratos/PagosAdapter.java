package com.tec.inmobile.ui.contratos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.inmobile.R;
import com.tec.inmobile.models.Pagos;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolderPago> {

    private Context context;
    private List<Pagos> lista;

    public PagosAdapter(Context context, List<Pagos> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolderPago onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_pagos, parent, false);
        return new ViewHolderPago(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPago holder, int position) {
        Pagos pago = lista.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        holder.tvCodigo.setText("Código de pago: " + pago.getIdPagos());
        holder.tvNumero.setText("Detalle: " + (pago.getDetalle() != null ? pago.getDetalle() : "-"));
        holder.tvFecha.setText("Fecha: " + (pago.getFechaPago() != null ? dateFormat.format(pago.getFechaPago()) : "-"));
        holder.tvImporte.setText(String.format("Importe: $%.2f", pago.getMonto()));
        holder.tvEstado.setText("Estado: " + (pago.isEstado() ? "Activo" : "Inactivo"));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolderPago extends RecyclerView.ViewHolder {
        TextView tvCodigo, tvNumero, tvFecha, tvImporte, tvEstado;

        public ViewHolderPago(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvImporte = itemView.findViewById(R.id.tvImporte);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
