package com.Angelvf3839.tarea3dwesangel.modelo;

import java.io.Serializable;

public class Sesion implements Serializable {

    private Long idUsuario;
    private String nombreUsuario;
    private TipoUsuario tipoUsuario;

    public Sesion() {
    }

    public Sesion(Long idUsuario, String nombreUsuario, TipoUsuario tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}
