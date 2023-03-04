package com.example.proyectomov.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

public class MovieRealmCRUD {

    public final static ArrayList<MovieRealm> listarMovies(String email){
        ArrayList<MovieRealm> movieRealm = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        UsuarioRealm usuarioRealm = realm.where(UsuarioRealm.class).equalTo("email", email).findFirst();
        RealmList<MovieRealm> movieRealms = usuarioRealm.getMovieRealms();
        realm.commitTransaction();

        for (MovieRealm movie:
             movieRealms) {
            MovieRealm mov = new MovieRealm(movie.getTitulo(), movie.getDirector(), movie.getGenero(), null);
            movieRealm.add(mov);
        }
        return movieRealm;
    }

    public final static void aniadirMovie(final String email, final MovieRealm movieRealm){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        UsuarioRealm usuarioRealm = realm.where(UsuarioRealm.class).equalTo("email", email).findFirst();
        usuarioRealm.getMovieRealms().add(movieRealm);
        realm.insertOrUpdate(usuarioRealm);
        realm.commitTransaction();
    }

    public final static void actualizarMovie(String titulo, String director, String genero, String email){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        UsuarioRealm usuarioRealm = realm.where(UsuarioRealm.class).equalTo("email", email).findFirst();
        RealmList<MovieRealm> movieRealm = usuarioRealm.getMovieRealms();

        for (MovieRealm mov:
             movieRealm) {
            if (mov.getTitulo().equals(titulo)) {
                mov.setTitulo(titulo);
                mov.setDirector(director);
                mov.setGenero(genero);
                realm.insertOrUpdate(mov);
            }
            realm.commitTransaction();
        }
    }

    public final static void eliminarMovie(String titulo, String email){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UsuarioRealm usuarioRealm = realm.where(UsuarioRealm.class).equalTo("email", email).findFirst();
                if (usuarioRealm != null){
                    MovieRealm movieEliminar = realm.where(MovieRealm.class).equalTo("titulo", titulo).findFirst();
                    if (movieEliminar != null){
                        movieEliminar.deleteFromRealm();
                    }
                }
            }
        });
    }

}
