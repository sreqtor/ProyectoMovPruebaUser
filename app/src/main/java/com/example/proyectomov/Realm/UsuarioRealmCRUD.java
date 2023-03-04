package com.example.proyectomov.Realm;

import io.realm.Realm;

public class UsuarioRealmCRUD {
    public final static void aniadirUsuario(final UsuarioRealm usuarioRealm){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(usuarioRealm);
        realm.commitTransaction();
    }

    public final static void actualizarUsuario(String email, String usuario){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        UsuarioRealm usuarioRealm = realm.where(UsuarioRealm.class).equalTo("email", email).findFirst();
        usuarioRealm.setUsuario(usuario);
        realm.insertOrUpdate(usuarioRealm);
        realm.commitTransaction();
    }
}
