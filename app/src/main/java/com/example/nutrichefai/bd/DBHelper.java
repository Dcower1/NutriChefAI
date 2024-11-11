package com.example.nutrichefai.bd;

import static com.example.nutrichefai.utils.Utilidades.hashPassword;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nutrichefai.utils.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NutriChefAI.db";
    private static final int DATABASE_VERSION = 4;

    // Tablas
    public static final String TABLA_GRUPO = "grupo_alimenticio";
    public static final String TABLA_TIPO_ALIMENTO = "tipo_alimento";
    public static final String TABLA_INGREDIENTES = "ingredientes";
    public static final String TABLA_INVENTARIO = "inventario";

    // Columnas de Grupo Alimenticio
    public static final String Id_Grupo = "id_grupo";
    public static final String COLUMN_Nombre_Grupo = "nombre_grupo";
    public static final String COLUMN_Imagen_Grupo = "imagen_grupo";

    // Columnas de Tipo de Alimento
    public static final String Id_Tipo = "id_tipo";
    public static final String COLUMN_Nombre_Tipo = "nombre_tipo";
    public static final String COLUMN_Imagen_Tipo = "imagen_tipo";
    public static final String COLUMN_Id_Grupo_FK = "id_grupo_fk";

    // Columnas de Ingredientes
    public static final String Id_Ingrediente = "id_ingrediente";
    public static final String COLUMN_Nombre_Ingrediente = "nombre_ingrediente";
    public static final String COLUMN_info = "info";
    public static final String COLUMN_Imagen_Nombre = "imagen_nombre";
    public static final String COLUMN_Id_Tipo_FK = "id_tipo_fk";

    // Columnas de Inventario
    public static final String Id_Inventario = "id_inventario";
    public static final String COLUMN_Ingrediente = "ingrediente";
    public static final String COLUMN_Cantidad = "cantidad";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Crear tabla de Grupo Alimenticio
        String CREATE_TABLA_GRUPO = "CREATE TABLE " + TABLA_GRUPO + "(" +
                Id_Grupo + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Nombre_Grupo + " TEXT, " +
                COLUMN_Imagen_Grupo + " TEXT)";
        db.execSQL(CREATE_TABLA_GRUPO);

        // Crear tabla de Tipo de Alimento
        String CREATE_TABLA_TIPO_ALIMENTO = "CREATE TABLE " + TABLA_TIPO_ALIMENTO + "(" +
                Id_Tipo + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Nombre_Tipo + " TEXT, " +
                COLUMN_Imagen_Tipo + " TEXT, " +
                COLUMN_Id_Grupo_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_Id_Grupo_FK + ") REFERENCES " + TABLA_GRUPO + "(" + Id_Grupo + "))";
        db.execSQL(CREATE_TABLA_TIPO_ALIMENTO);

        // Crear tabla de Ingredientes
        String CREATE_TABLA_INGREDIENTES = "CREATE TABLE " + TABLA_INGREDIENTES + "(" +
                Id_Ingrediente + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Nombre_Ingrediente + " TEXT, " +
                COLUMN_info + " TEXT, " +
                COLUMN_Imagen_Nombre + " TEXT, " +
                COLUMN_Id_Tipo_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_Id_Tipo_FK + ") REFERENCES " + TABLA_TIPO_ALIMENTO + "(" + Id_Tipo + "))";
        db.execSQL(CREATE_TABLA_INGREDIENTES);

        // Crear tabla de Inventario
        String CREATE_TABLA_INVENTARIO = "CREATE TABLE " + TABLA_INVENTARIO + "(" +
                Id_Inventario + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Ingrediente + " TEXT, " +
                COLUMN_Cantidad + " INTEGER)";
        db.execSQL(CREATE_TABLA_INVENTARIO);

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        insertGrupos(db);
        insertTipos(db);
        insertInitialIngredients(db);
    }

    // Método para insertar Grupos Alimenticios iniciales
    private void insertGrupos(SQLiteDatabase db) {
        addGrupo(db, "Carbohidratos", "default_image");
        addGrupo(db, "Proteínas", "default_image");
        addGrupo(db, "Frutas", "default_image");
        addGrupo(db, "Lácteos", "default_image");
        addGrupo(db, "Grasas y Aceites", "default_image");
    }

    private void addGrupo(SQLiteDatabase db, String nombre, String imagen) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_Nombre_Grupo, nombre);
        values.put(COLUMN_Imagen_Grupo, imagen);
        db.insert(TABLA_GRUPO, null, values);
    }

    // Método para insertar Tipos de Alimentos iniciales
    private void insertTipos(SQLiteDatabase db) {
        addTipo(db, "Granos y Cereales", "default_image", 1);
        addTipo(db, "Panes y Pastas", "default_image", 1);
        addTipo(db, "Tubérculos y Raíces", "default_image", 1);
        addTipo(db, "Carnes", "default_image", 2);
        addTipo(db, "Pescados y Mariscos", "default_image", 2);
    }

    private void addTipo(SQLiteDatabase db, String nombre, String imagen, int idGrupo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_Nombre_Tipo, nombre);
        values.put(COLUMN_Imagen_Tipo, imagen);
        values.put(COLUMN_Id_Grupo_FK, idGrupo);
        db.insert(TABLA_TIPO_ALIMENTO, null, values);
    }

    // Método para insertar los ingredientes iniciales
    private void insertInitialIngredients(SQLiteDatabase db) {
        addIngredient(db, "Morron Rojo", "Rico en vitamina C", "ingrediente1", 3); // 3 es el ID del tipo correspondiente
        addIngredient(db, "Ingrediente 2", "Descripción", "default_image", 4);
    }

    private void addIngredient(SQLiteDatabase db, String name, String info, String imageName, int idTipo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_Nombre_Ingrediente, name);
        values.put(COLUMN_info, info);
        values.put(COLUMN_Imagen_Nombre, imageName);
        values.put(COLUMN_Id_Tipo_FK, idTipo);
        db.insert(TABLA_INGREDIENTES, null, values);
    }

    // Método para obtener todos los grupos alimenticios
    @SuppressLint("Range")
    public List<Grupo> getAllGrupos() {
        List<Grupo> grupoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLA_GRUPO;

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Id_Grupo));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_Nombre_Grupo));
                    String imageName = cursor.getString(cursor.getColumnIndex(COLUMN_Imagen_Grupo));
                    grupoList.add(new Grupo(id, name, imageName));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grupoList;
    }
    @SuppressLint("Range")
    public List<TipoAlimento> getTipoAlimentosByGrupo(int grupoId) {
        List<TipoAlimento> tipoAlimentoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLA_TIPO_ALIMENTO + " WHERE " + COLUMN_Id_Grupo_FK + " = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(grupoId)})) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Id_Tipo));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_Nombre_Tipo));
                    String imageName = cursor.getString(cursor.getColumnIndex(COLUMN_Imagen_Tipo));
                    tipoAlimentoList.add(new TipoAlimento(id, name, imageName, grupoId));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipoAlimentoList;
    }
    @SuppressLint("Range")
    public List<Food> getIngredientesByTipo(int tipoId) {
        List<Food> ingredienteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLA_INGREDIENTES + " WHERE " + COLUMN_Id_Tipo_FK + " = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(tipoId)})) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Id_Ingrediente));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_Nombre_Ingrediente));
                    String info = cursor.getString(cursor.getColumnIndex(COLUMN_info));
                    String imageName = cursor.getString(cursor.getColumnIndex(COLUMN_Imagen_Nombre));
                    ingredienteList.add(new Food(id, name, info, imageName, tipoId));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredienteList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_GRUPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TIPO_ALIMENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_INGREDIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_INVENTARIO);
        onCreate(db);
    }
}