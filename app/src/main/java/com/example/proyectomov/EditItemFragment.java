package com.example.proyectomov;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
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
import androidx.fragment.app.DialogFragment;

import com.example.proyectomov.Realm.MovieRealm;
import com.example.proyectomov.Realm.MovieRealmCRUD;

import java.io.IOException;

public class EditItemFragment extends DialogFragment {

    private MovieRealm item;
    private EditText title;
    private EditText maker;
    private EditText description;
    private ImageView photo;
    private Bitmap image;
    private Uri selectedImage;
    private int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private VentInicial context;
    View rootView;

    public EditItemFragment() {
        // Required empty public constructor
    }

    public EditItemFragment(MovieRealm item) {
        this.item = item;
    }


    public static EditItemFragment newInstance() {
        EditItemFragment editItemFragment = new EditItemFragment();
        return editItemFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = (VentInicial) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_edit_item, null);
        title = (EditText) rootView.findViewById(R.id.title);
        title.setText(item.getTitulo());
        maker = (EditText) rootView.findViewById(R.id.maker);
        maker.setText(item.getDirector());
        description = (EditText) rootView.findViewById(R.id.description);
        description.setText(item.getGenero());
        photo = (ImageView) rootView.findViewById(R.id.image_view);
        image = item.getImg();
        photo.setImageBitmap(image);
        Button saveButton = (Button) rootView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem(view);
            }
        });

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelItem(view);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(rootView);
        //return alertDialogBuilder.create();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(R.drawable.logo_up);
    }

    public void saveItem (View view) {
        String title_str = title.getText().toString();
        String maker_str = maker.getText().toString();
        String description_str = description.getText().toString();

        if (title_str.equals("")) {
            title.setError("Introduce título");
            return;
        }

        if (maker_str.equals("")) {
            maker.setError("Introduce director");
            return;
        }

        if (description_str.equals("")) {
            description.setError("Introduce género");
            return;
        }

        try {
            MovieRealmCRUD.actualizarMovie(title_str, maker_str, description_str, context.getUsuario());
            Toast.makeText(context, "Película editada correctamente", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ResetFragmentFields() {
        title.setText("");
        maker.setText("");
        description.setText("");
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    public void cancelItem(View view) {
        dismiss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            try {
                photo.setImageURI(selectedImage);
                image = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}