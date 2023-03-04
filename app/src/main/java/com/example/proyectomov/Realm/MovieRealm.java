package com.example.proyectomov.Realm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MovieRealm extends RealmObject {
    @PrimaryKey
    int id;
    @Required
    String titulo = "";
    @Required
    String director = "";
    @Required
    String genero = "";
    String image_base64 = null;

    private transient Bitmap img;

    @LinkingObjects("movieRealms")
    final RealmResults<UsuarioRealm> usuarios = null;

    public MovieRealm() {}

    public MovieRealm(String titulo, String director, String genero, Bitmap img) {
        this.titulo = titulo;
        this.director = director;
        this.genero = genero;
        this.img = img;

        setImg(img);
    }

    public Bitmap getImg() {
        if (img == null && image_base64 != null) {
            byte [] decodeString = Base64.decode(image_base64, Base64.DEFAULT);
            img = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        }
        return img;
    }

    public void setImg(Bitmap imgN) {
        if (imgN != null) {
            img = imgN;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            imgN.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
            byte [] b = byteArrayBitmapStream.toByteArray();
            image_base64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    @Override
    public String toString() {
        return "MovieRealm{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", genero='" + genero + '\'' +
                ", image_base64='" + image_base64 + '\'' +
                '}';
    }


}




