package com.example.nutrichefai.fragments.freezer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutrichefai.R;
import com.example.nutrichefai.adapters.FoodAdapter;
import com.example.nutrichefai.adapters.GrupoAdapter;
import com.example.nutrichefai.adapters.IngredienteAdapter;
import com.example.nutrichefai.adapters.TipoAlimentoAdapter;
import com.example.nutrichefai.bd.DBHelper;
import com.example.nutrichefai.bd.Food;
import com.example.nutrichefai.bd.Grupo;
import com.example.nutrichefai.bd.TipoAlimento;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Freezer_inv extends Fragment {
    private RecyclerView recyclerView;
    private GrupoAdapter grupoAdapter;
    private TipoAlimentoAdapter tipoAlimentoAdapter;
    private IngredienteAdapter ingredienteAdapter;
    private DBHelper dbHelper;
    private ImageView backButton;
    private boolean isLargeScreen; // Flag de tamaño de pantalla

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freezer_inv, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el flag de tamaño de pantalla desde los argumentos
        isLargeScreen = getArguments() != null && getArguments().getBoolean("isLargeScreen", false);

        // Inicializar RecyclerView y DBHelper
        recyclerView = view.findViewById(R.id.selector_ingredientes);
        dbHelper = new DBHelper(getContext());

        // Configurar el LayoutManager en función del tamaño de pantalla
        if (isLargeScreen) {
            int spanCount = Math.max(1, getResources().getConfiguration().screenWidthDp / 100);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        // Inicializar el botón de retroceso
        backButton = view.findViewById(R.id.bt_atras);
        backButton.setOnClickListener(v -> handleBackNavigation());

        // Cargar y mostrar los grupos al inicio
        loadGrupos();

        return view;
    }

    private void loadGrupos() {
        // Ocultar el botón de retroceso cuando se muestran los grupos
        backButton.setVisibility(View.GONE);

        // Obtener los datos de los grupos y configurar el adaptador
        List<Grupo> grupoList = dbHelper.getAllGrupos();
        grupoAdapter = new GrupoAdapter(grupoList, getContext(), isLargeScreen, grupoId -> loadTiposAlimento(grupoId));
        recyclerView.setAdapter(grupoAdapter);
    }

    private void loadTiposAlimento(int grupoId) {
        backButton.setVisibility(View.VISIBLE);
        List<TipoAlimento> tipoAlimentoList = dbHelper.getTipoAlimentosByGrupo(grupoId);
        tipoAlimentoAdapter = new TipoAlimentoAdapter(tipoAlimentoList, getContext(), isLargeScreen, tipoAlimentoId -> loadIngredientes(tipoAlimentoId));
        recyclerView.setAdapter(tipoAlimentoAdapter);
    }

    private void loadIngredientes(int tipoAlimentoId) {
        backButton.setVisibility(View.VISIBLE);
        List<Food> ingredienteList = dbHelper.getIngredientesByTipo(tipoAlimentoId);
        ingredienteAdapter = new IngredienteAdapter(ingredienteList, getContext(), isLargeScreen);
        recyclerView.setAdapter(ingredienteAdapter);
    }

    private void handleBackNavigation() {
        if (recyclerView.getAdapter() instanceof IngredienteAdapter) {
            loadGrupos();
        } else if (recyclerView.getAdapter() instanceof TipoAlimentoAdapter) {
            loadGrupos();
        }
    }
}