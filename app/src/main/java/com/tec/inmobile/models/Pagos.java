package com.tec.inmobile.models;

import java.io.Serializable;
import java.util.Date;

public class Pagos implements Serializable {

    private int idPago;
    private Date fechaPago;
    private double monto;
    private String detalle;
    private boolean estado;

    public Pagos() {
    }

    public Pagos(String detalle, boolean estado, Date fechaPago, int idPago, double monto) {
        this.detalle = detalle;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.idPago = idPago;
        this.monto = monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdPagos() {
        return idPago;
    }

    public void setIdPagos(int idPagos) {
        this.idPago = idPagos;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}

