package com.example.proyectomov;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectomov.Realm.MovieRealm;
import com.example.proyectomov.Realm.MovieRealmCRUD;
import com.example.proyectomov.items.ItemAdapter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ItemListFragment extends Fragment {

    View rootView;
    VentInicial context;

    private RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = (VentInicial) context;
        super.onAttach(context);
    }

    public static ItemListFragment newInstance() {
        ItemListFragment itemListFragment = new ItemListFragment();
        return itemListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //RealmListSingleton.getItemList().listar();  //listar
        //getAllMovies();  //listar en Logcat


        rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = rootView.findViewById(R.id.itemsList);
        //layoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(layoutManager);

        ItemAdapter.OnItemClickListener onItemClickListener = new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieRealm mItem) {
                MovieRealmCRUD.actualizarMovie(mItem.getTitulo(), mItem.getDirector(), mItem.getGenero(), context.getUsuario());
                FragmentManager fm = context.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, new EditItemFragment(mItem));
                ft.commit();
            }
        };


        //adaptar a realm
        ItemAdapter.OnItemLongClickListener onItemLongClickListener = new ItemAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(MovieRealm item) {
                MovieRealmCRUD.eliminarMovie(item.getTitulo(), context.getUsuario());
                Toast.makeText(getContext(), item.getTitulo() + " se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                FragmentManager fm = context.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, new ItemListFragment());
                ft.commit();
            }
        };

        recyclerView.setAdapter(new ItemAdapter(onItemClickListener, onItemLongClickListener, MovieRealmCRUD.listarMovies(context.getUsuario())));
        return rootView;
    }

    /*public final static List<MovieRealm> getAllMovies(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MovieRealm> movies = realm.where(MovieRealm.class).findAll();
        Log.d("TAG1","listando...");
        for (MovieRealm movie: movies){
            Log.d("TAG", "Título: " + movie.getTitulo() + " Director: " + movie.getDirector() + " Género: " + movie.getGenero());
        }
        return movies;
    }*/

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ItemListSingleton.getItemList().loadItems(context);
        rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.itemList);
        ItemAdapter.OnItemClickListener onItemClickListener = new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                DialogFragment editItemFragment = new EditItemFragment(item);
                FragmentManager fm = context.getSupportFragmentManager();
                editItemFragment.show(fm, "EditDialogFragment");
            }
        };

        ItemAdapter.OnItemLongClickListener onItemLongClickListener = new ItemAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(Item item) {
                ItemListSingleton itemList = ItemListSingleton.getInstance();
                int itemIndex = itemList.getItemList().getIndex(item);
                itemList.getItemList().deleteItem(itemIndex);
                itemList.getItemList().saveItems(context);
                Toast.makeText(getContext(), item.getTitle() + " se ha eliminado.", Toast.LENGTH_SHORT).show();
                FragmentManager fm = context.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, new ItemListFragment());
                ft.commit();
                return true;
            }
        };

        recycler.setAdapter(new ItemAdapter(onItemClickListener, onItemLongClickListener));
        return rootView;
    }*/
}