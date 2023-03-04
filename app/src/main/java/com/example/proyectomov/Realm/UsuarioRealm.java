package com.example.proyectomov.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UsuarioRealm extends RealmObject {
    @PrimaryKey
    private String email="";
    @Required
    private String usuario="";

    private RealmList<MovieRealm> movieRealms = new RealmList<>();

    public UsuarioRealm() {
    }

    public UsuarioRealm(String email, String usuario) {
        this.email = email;
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public RealmList<MovieRealm> getMovieRealms() {
        return movieRealms;
    }

    public void setMovieRealms(RealmList<MovieRealm> movieRealms) {
        this.movieRealms = movieRealms;
    }
}
