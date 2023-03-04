package com.example.proyectomov;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomov.Realm.MovieRealm;
import com.example.proyectomov.Realm.MovieRealmCRUD;

import io.realm.Realm;

public class AddItemFragment extends Fragment {
    //private String id= UUID.randomUUID().toString();

    private MovieRealm movieRealm;
    private static Realm myRealm;
    private String id;
    private int cont=0;
    private EditText title;
    private EditText maker;
    private EditText description;
    private ImageView photo;

    private Button saveButton;
    //private String image;
    private Bitmap image;
    private Uri selectedImage;
    private int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private VentInicial context;


    View rootView;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = (VentInicial) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myRealm = Realm.getDefaultInstance();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myRealm = Realm.getDefaultInstance();


        rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        title = (EditText) rootView.findViewById(R.id.title);
        maker = (EditText) rootView.findViewById(R.id.maker);
        description = (EditText) rootView.findViewById(R.id.description);
        photo = (ImageView) rootView.findViewById(R.id.image_view);
        photo.setImageResource(R.drawable.logo_up);
        saveButton = (Button) rootView.findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();

                /*myRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        cont++;
                        id = "id"+cont;

                        MovieRealm myMovie = myRealm.createObject(MovieRealm.class, MovieRealmCRUD.calculateIndex());


                        myMovie.setTitulo(title_str);
                        myMovie.setDirector(maker_str);
                        myMovie.setGenero(description_str);


                    }
                });*/

            }
        });

        ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    image = (Bitmap) extras.get("data");
                    photo.setImageBitmap(image);
                }
            }
        });


        ImageButton addGalleryPhotoImageButton = (ImageButton) rootView.findViewById(R.id.add_gallery_image_button);
        addGalleryPhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent, 3);
            }
        });

        ImageButton addPhotoImageButton = (ImageButton) rootView.findViewById(R.id.add_image_button);
        addPhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        });

        ImageButton deletePhotoImageButton = (ImageButton) rootView.findViewById(R.id.cancel_image_button);
        deletePhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { deletePhoto(view); }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(R.drawable.logo_up);
    }

    private void ResetFragmentFields() {
        title.setText("");
        maker.setText("");
        description.setText("");
        photo.setImageResource(R.drawable.logo_up);
    }
    public void saveItem () {
        String title_str = title.getText().toString();
        String maker_str = maker.getText().toString();
        String description_str = description.getText().toString();

        if (title_str.equals("")) {
            title.setError("Obligatorio");
            return;
        }

        if (maker_str.equals("")) {
            maker.setError("Obligatorio");
            return;
        }

        if (description_str.equals("")) {
            description.setError("Obligatorio");
            return;
        }

        try {
            MovieRealm mov = new MovieRealm(title_str, maker_str, description_str, image);
            MovieRealmCRUD.aniadirMovie(context.getUsuario(), mov);
            Toast.makeText(context, "Película añadida correctamente", Toast.LENGTH_SHORT).show();
            ResetFragmentFields();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}