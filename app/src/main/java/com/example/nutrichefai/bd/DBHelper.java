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
    private static final int DATABASE_VERSION = 8; //V7

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
        addGrupo(db, "Carbohidratos", "img1");
        addGrupo(db, "Proteínas", "img2");
        addGrupo(db, "Frutas", "img3");
        addGrupo(db, "Lácteos", "img4");
        addGrupo(db, "Grasas y Aceites", "img5");
        addGrupo(db, "Vegetales", "img6");
        addGrupo(db, "Legumbres", "img7");
        addGrupo(db, "Azúcares y Endulzantes", "img8");
        addGrupo(db, "Bebidas", "img9");
        addGrupo(db, "Snacks y Dulces", "img10");
        addGrupo(db, "Hierbas y Especias", "img11");
    }

    private void addGrupo(SQLiteDatabase db, String nombre, String imagen) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_Nombre_Grupo, nombre);
        values.put(COLUMN_Imagen_Grupo, imagen);
        db.insert(TABLA_GRUPO, null, values);
    }

    // Método para insertar Tipos de Alimentos iniciales
    private void insertTipos(SQLiteDatabase db) {
        addTipo(db, "Granos y Cereales", "tipo1", 1);
        addTipo(db, "Panes y Pastas", "tipo2", 1);
        addTipo(db, "Tubérculos y Raíces", "tipo3", 1);
        addTipo(db, "Legumbres", "tipo4", 1);
        addTipo(db, "Verduras C.up", "tipo5", 1);
        addTipo(db, "Carnes", "tipo6", 2);
        addTipo(db, "Pescados y Mariscos", "tipo7", 2);
        addTipo(db, "Huevos", "tipo8", 2);
        addTipo(db, "Tofu y Tempeh", "tipo9", 2);
        addTipo(db, "Frutos Secos y Semillas", "tipo10", 2);
        addTipo(db, "Frutas varias", "tipo11", 3);
        addTipo(db, "Leche y Derivados", "tipo12", 4);
        addTipo(db, "Quesos", "tipo13", 4);
        addTipo(db, "Yogur", "tipo14", 4);
        addTipo(db, "Aceites", "tipo15", 5);
        addTipo(db, "Otros", "tipo16", 5);
        addTipo(db, "Vegetales Varios", "tipo17", 6);
        addTipo(db, "Frijoles", "tipo18", 7);
        addTipo(db, "Lentejas", "tipo19", 7);
        addTipo(db, "Guisantes", "tipo20", 7);
        addTipo(db, "Azúcar", "tipo21", 8);
        addTipo(db, "Miel y Sirope", "tipo22", 8);
        addTipo(db, "Endulzantes Naturales", "tipo23", 8);
        addTipo(db, "Bebidas No Alcohólicas", "tipo24", 9);
        addTipo(db, "Bebidas Fermentadas", "tipo25", 9);
        addTipo(db, "Bebidas Alcohólicas", "tipo26", 9);
        addTipo(db, "Dulces", "tipo27", 10);
        addTipo(db, "Snacks Salados", "tipo28", 10);
        addTipo(db, "Hierbas Frescas", "tipo29", 11);
        addTipo(db, "Especias", "tipo30", 11);

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

        // Carbohidratos
        addIngredient(db, "Arroz", "Rico en carbohidratos", "arroz", 1);
        addIngredient(db, "Trigo", "Rico en carbohidratos", "trigo", 1);
        addIngredient(db, "Avena", "Rica en fibra", "avena", 1);
        addIngredient(db, "Cebada", "Rica en fibra y nutrientes", "default_image", 1);
        addIngredient(db, "Centeno", "Rico en carbohidratos", "default_image", 1);
        addIngredient(db, "Maíz", "Rico en carbohidratos", "default_image", 1);
        addIngredient(db, "Quinoa", "Rica en proteínas", "default_image", 1);
        addIngredient(db, "Mijo", "Rico en fibra", "default_image", 1);

        addIngredient(db, "Pan integral", "Rico en fibra", "pani", 2);
        addIngredient(db, "Pan blanco", "Rico en carbohidratos", "pan", 2);
        addIngredient(db, "Pasta", "Rica en carbohidratos", "fideo", 2);
        addIngredient(db, "Fideos", "Rico en carbohidratos", "default_image", 2);
        addIngredient(db, "Tortillas", "Rico en carbohidratos", "default_image", 2);
        addIngredient(db, "Papa", "Rica en carbohidratos", "default_image", 3);
        addIngredient(db, "Batata", "Rica en carbohidratos", "default_image", 3);
        addIngredient(db, "Yuca", "Rica en carbohidratos", "default_image", 3);
        addIngredient(db, "Ñame", "Rico en carbohidratos", "default_image", 3);


        addIngredient(db, "Lentejas", "Ricas en proteínas y fibra", "lentejas", 4);
        addIngredient(db, "Garbanzos", "Ricos en proteínas y fibra", "default_image", 4);
        addIngredient(db, "Frijoles", "Ricos en proteínas y fibra", "default_image", 4);
        addIngredient(db, "Alubias", "Ricas en proteínas y fibra", "default_image", 4);
        addIngredient(db, "Guisantes", "Ricos en carbohidratos", "default_image", 4);

        addIngredient(db, "Calabaza", "Rica en carbohidratos", "default_image", 5);
        addIngredient(db, "Zanahorias", "Ricas en vitamina A", "zana", 5);
        addIngredient(db, "Remolacha", "Rica en antioxidantes", "default_image", 5);
        addIngredient(db, "Morron Rojo", "Rico en vitamina C", "ingrediente1", 5);
        // Proteínas
        addIngredient(db, "Pollo", "Rico en proteínas", "pollo", 6);
        addIngredient(db, "Res", "Rica en proteínas", "res", 6);
        addIngredient(db, "Cerdo", "Rico en proteínas", "default_image", 6);
        addIngredient(db, "Cordero", "Rico en proteínas", "default_image", 6);
        addIngredient(db, "Pavo", "Rico en proteínas", "default_image", 6);

        addIngredient(db, "Salmón", "Rico en omega 3", "salmon", 7);
        addIngredient(db, "Atún", "Rico en proteínas y omega 3", "atun", 7);
        addIngredient(db, "Bacalao", "Rico en proteínas", "default_image", 7);
        addIngredient(db, "Sardinas", "Ricas en omega 3", "default_image", 7);
        addIngredient(db, "Camarones", "Ricos en proteínas", "default_image", 7);
        addIngredient(db, "Mejillones", "Ricos en proteínas", "default_image", 7);

        addIngredient(db, "Huevo entero", "Rico en proteínas", "huevo", 8);
        addIngredient(db, "Claras de huevo", "Ricas en proteínas", "clarah", 8);

        addIngredient(db, "Tofu", "Rico en proteínas", "tofu", 9);
        addIngredient(db, "Tempeh", "Rico en proteínas", "default_image", 9);

        addIngredient(db, "Almendras", "Ricas en grasas saludables", "alme", 10);
        addIngredient(db, "Nueces", "Ricas en omega 3", "default_image", 10);
        addIngredient(db, "Semillas de chía", "Ricas en omega 3", "default_image", 10);
        addIngredient(db, "Semillas de lino", "Ricas en fibra", "default_image", 10);
        addIngredient(db, "Semillas de girasol", "Ricas en proteínas", "default_image", 10);
        addIngredient(db, "Avellanas", "Ricas en vitamina E", "default_image", 10);
        addIngredient(db, "Semillas de sésamo", "Ricas en calcio", "default_image", 10);
        // Frutas
        addIngredient(db, "Naranjas", "Ricas en vitamina C", "naranja", 11);
        addIngredient(db, "Limones", "Ricos en vitamina C", "default_image", 11);
        addIngredient(db, "Mandarinas", "Ricas en vitamina C", "default_image", 11);
        addIngredient(db, "Pomelos", "Ricos en vitamina C", "default_image", 11);
        addIngredient(db, "Mango", "Rico en vitamina A", "default_image", 11);
        addIngredient(db, "Piña", "Rica en vitamina C", "default_image", 11);
        addIngredient(db, "Papaya", "Rica en vitamina C", "default_image", 11);
        addIngredient(db, "Coco", "Rico en grasas saludables", "default_image", 11);
        addIngredient(db, "Plátano", "Rico en potasio", "default_image", 11);
        addIngredient(db, "Fresas", "Ricas en antioxidantes", "default_image", 11);
        addIngredient(db, "Arándanos", "Ricos en antioxidantes", "default_image", 11);
        addIngredient(db, "Frambuesas", "Ricas en fibra", "default_image", 11);
        addIngredient(db, "Moras", "Ricas en vitamina C", "default_image", 11);
        addIngredient(db, "Duraznos", "Ricos en vitamina C", "default_image", 11);
        addIngredient(db, "Ciruelas", "Ricas en fibra", "default_image", 11);
        addIngredient(db, "Albaricoques", "Ricos en vitamina A", "default_image", 11);
        addIngredient(db, "Cerezas", "Ricas en antioxidantes", "default_image", 11);
        addIngredient(db, "Manzana", "Rica en fibra", "default_image", 11);
        addIngredient(db, "Pera", "Rica en fibra", "default_image", 11);
        addIngredient(db, "Uvas", "Ricas en antioxidantes", "default_image", 11);
        addIngredient(db, "Kiwi", "Rico en vitamina C", "default_image", 11);
        addIngredient(db, "Granada", "Rica en antioxidantes", "default_image", 11);
        addIngredient(db, "Uvas pasas", "Deshidratadas", "default_image", 11);
        addIngredient(db, "Higos secos", "Ricos en fibra", "default_image", 11);
        addIngredient(db, "Dátiles", "Ricos en azúcares naturales", "default_image", 11);
        addIngredient(db, "Orejones", "Deshidratados", "default_image", 11);

        // Lácteos
        addIngredient(db, "Leche de vaca", "Fuente de calcio", "default_image", 12);
        addIngredient(db, "Leche de cabra", "Rica en nutrientes", "default_image", 12);
        addIngredient(db, "Leche de oveja", "Rica en proteínas", "default_image", 12);
        addIngredient(db, "Crema", "Rica en grasas", "default_image", 12);
        addIngredient(db, "Mantequilla", "Rica en grasas", "default_image", 12);
        addIngredient(db, "Kéfir", "Probiótico natural", "default_image", 12);

        addIngredient(db, "Queso cheddar", "Rico en proteínas", "default_image", 13);
        addIngredient(db, "Mozzarella", "Fuente de calcio", "default_image", 13);
        addIngredient(db, "Queso azul", "Rico en calcio", "default_image", 13);
        addIngredient(db, "Queso cottage", "Bajo en grasa", "default_image", 13);
        addIngredient(db, "Parmesano", "Rico en proteínas", "default_image", 13);

        addIngredient(db, "Yogur natural", "Probiótico", "default_image", 14);
        addIngredient(db, "Yogur griego", "Alto en proteínas", "default_image", 14);
        addIngredient(db, "Yogur con frutas", "Rico en probióticos", "default_image", 14);

        // Grasas y Aceites
        addIngredient(db, "Aceite de oliva", "Rico en grasas saludables", "default_image", 15);
        addIngredient(db, "Aceite de coco", "Rico en grasas saturadas", "default_image", 15);
        addIngredient(db, "Aceite de aguacate", "Rico en ácidos grasos omega", "default_image", 15);
        addIngredient(db, "Aceite de linaza", "Rico en omega 3", "default_image", 15);

        addIngredient(db, "Mantequilla de maní", "Rica en grasas y proteínas", "default_image", 16);
        addIngredient(db, "Mantequilla de almendra", "Rica en grasas saludables", "default_image", 16);

        // Vegetales
        addIngredient(db, "Espinaca", "Rica en hierro", "default_image", 17);
        addIngredient(db, "Lechuga", "Baja en calorías", "default_image", 17);
        addIngredient(db, "Acelga", "Fuente de vitamina A", "default_image", 17);
        addIngredient(db, "Kale", "Rica en vitamina K", "default_image", 17);
        addIngredient(db, "Rúcula", "Fuente de antioxidantes", "default_image", 17);
        addIngredient(db, "Brócoli", "Rico en fibra", "default_image", 17);
        addIngredient(db, "Coliflor", "Baja en carbohidratos", "default_image", 17);
        addIngredient(db, "Coles de Bruselas", "Ricas en fibra", "default_image", 17);
        addIngredient(db, "Repollo", "Rico en vitamina C", "default_image", 17);
        addIngredient(db, "Pimiento", "Rico en vitamina C", "default_image", 17);
        addIngredient(db, "Tomate", "Fuente de licopeno", "default_image", 17);
        addIngredient(db, "Pepino", "Hidratante", "default_image", 17);
        addIngredient(db, "Calabacín", "Bajo en calorías", "default_image", 17);
        addIngredient(db, "Berenjena", "Rica en antioxidantes", "default_image", 17);
        addIngredient(db, "Ajo", "Antibacteriano", "default_image", 17);
        addIngredient(db, "Cebolla", "Antiinflamatoria", "default_image", 17);
        addIngredient(db, "Puerro", "Rico en fibra", "default_image", 17);
        addIngredient(db, "Cebollín", "Rico en antioxidantes", "default_image", 17);
        addIngredient(db, "Aguacate", "Rico en grasas saludables", "default_image", 17);

        // Legumbres
        addIngredient(db, "Frijoles negros", "Ricos en proteínas", "default_image", 18);
        addIngredient(db, "Frijoles rojos", "Ricos en proteínas", "default_image", 18);
        addIngredient(db, "Frijoles blancos", "Ricos en proteínas", "default_image", 18);

        addIngredient(db, "Lentejas verdes", "Ricas en hierro", "default_image", 19);
        addIngredient(db, "Lentejas rojas", "Ricas en fibra", "default_image", 19);
        addIngredient(db, "Lentejas marrones", "Ricas en proteínas", "default_image", 19);
        addIngredient(db, "Garbanzos", "Ricos en proteínas", "default_image", 19);
        addIngredient(db, "Habas", "Ricas en proteínas", "default_image", 19);

        addIngredient(db, "Guisantes verdes", "Ricos en proteínas", "default_image", 20);
        addIngredient(db, "Guisantes partidos", "Ricos en proteínas", "default_image", 20);

        // Azúcares y Endulzantes
        addIngredient(db, "Azúcar blanca", "Endulzante", "default_image", 21);
        addIngredient(db, "Azúcar morena", "Endulzante natural", "default_image", 21);

        addIngredient(db, "Miel", "Endulzante natural", "default_image", 22);
        addIngredient(db, "Sirope de arce", "Endulzante natural", "default_image", 22);
        addIngredient(db, "Sirope de agave", "Endulzante bajo en índice glucémico", "default_image", 22);

        addIngredient(db, "Stevia", "Endulzante natural", "default_image", 23);
        addIngredient(db, "Xilitol", "Endulzante bajo en calorías", "default_image", 23);
        addIngredient(db, "Eritritol", "Endulzante sin calorías", "default_image", 23);

        // Bebidas
        addIngredient(db, "Agua", "Bebida hidratante", "default_image", 24);
        addIngredient(db, "Jugos de frutas", "Ricos en vitaminas", "default_image", 24);
        addIngredient(db, "Té", "Rico en antioxidantes", "default_image", 24);
        addIngredient(db, "Café", "Estimulante natural", "default_image", 24);
        addIngredient(db, "Bebidas vegetales", "Alternativa a la leche", "default_image", 24);

        addIngredient(db, "Kombucha", "Bebida probiótica", "default_image", 25);
        addIngredient(db, "Kéfir de agua", "Rico en probióticos", "default_image", 25);

        addIngredient(db, "Vino", "Bebida alcohólica", "default_image", 26);
        addIngredient(db, "Cerveza", "Bebida alcohólica fermentada", "default_image", 26);
        addIngredient(db, "Licor", "Bebida alcohólica destilada", "default_image", 26);
        addIngredient(db, "Sidra", "Bebida alcohólica fermentada", "default_image", 26);

        // Snacks y Dulces
        addIngredient(db, "Palomitas", "Snack bajo en calorías", "default_image", 27);
        addIngredient(db, "Papas fritas", "Snack salado", "default_image", 27);
        addIngredient(db, "Galletas saladas", "Snack crujiente", "default_image", 27);
        addIngredient(db, "Nachos", "Snack salado", "default_image", 27);

        addIngredient(db, "Chocolates", "Snack dulce", "default_image", 28);
        addIngredient(db, "Galletas", "Dulce", "default_image", 28);
        addIngredient(db, "Caramelos", "Dulce", "default_image", 28);
        addIngredient(db, "Pasteles", "Dulce", "default_image", 28);

        // Hierbas y Especias
        addIngredient(db, "Albahaca", "Hierba fresca aromática", "default_image", 29);
        addIngredient(db, "Cilantro", "Hierba fresca", "default_image", 29);
        addIngredient(db, "Perejil", "Hierba fresca", "default_image", 29);
        addIngredient(db, "Romero", "Hierba aromática", "default_image", 29);
        addIngredient(db, "Tomillo", "Hierba aromática", "default_image", 29);

        addIngredient(db, "Canela", "Especia dulce", "default_image", 30);
        addIngredient(db, "Cúrcuma", "Especia antiinflamatoria", "default_image", 30);
        addIngredient(db, "Comino", "Especia aromática", "default_image", 30);
        addIngredient(db, "Pimienta", "Especia picante", "default_image", 30);
        addIngredient(db, "Pimentón", "Especia colorante", "default_image", 30);
        addIngredient(db, "Clavo de olor", "Especia aromática", "default_image", 30);

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