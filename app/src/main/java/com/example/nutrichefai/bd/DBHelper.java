package com.example.nutrichefai.bd;

import static com.example.nutrichefai.utils.Utilidades.hashPassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nutrichefai.utils.Utilidades;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NutriChefAI.db";
    private static final int DATABASE_VERSION = 2;


    public static final String TABLA_USUARIO="Usuario";

    public static final String Id_Usuario="id";
    public static final String COLUMN_usuario="usuario";
    public static final String COLUMN_mail="mail";
    public static final String COLUMN_password="password";
    public static final String COLUMN_edad="edad";
    public static final String COLUMN_peso="peso";
    public static final String COLUMN_altura="altura";
    public static final String COLUMN_imc="imc";
    public static final String COLUMN_dieta="dieta";

            //en un futuro agregar para que el usuario se asocie a los chats y a su inventario futuros sprint;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLA_USUARIO = " CREATE TABLE " +TABLA_USUARIO+"("+
                Id_Usuario + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_usuario + " TEXT, " +
                COLUMN_mail+ " TEXT, " +
                COLUMN_password + " TEXT, " +
                COLUMN_edad+ " INTEGER, " +
                COLUMN_peso+ " INTEGER, " +
                COLUMN_altura+ " TEXT, " +
                COLUMN_dieta + " TEXT, " +
                COLUMN_imc + " INTEGER" + ")";
        db.execSQL(CREATE_TABLA_USUARIO);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        onCreate(db);
    }

    // Método para recuperar un usuario por ID
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_USUARIO, null, Id_Usuario + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }


    public long insertarUsuario(String usuario, String mail, String password, String edad, String peso, String altura, String imc, String dieta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_usuario, usuario);
        values.put(COLUMN_mail, mail);
        values.put(COLUMN_password, password);
        values.put(COLUMN_edad, edad);
        values.put(COLUMN_peso, peso);
        values.put(COLUMN_altura, altura);
        values.put(COLUMN_imc, imc);
        values.put(COLUMN_dieta, dieta);

        long id = db.insert(TABLA_USUARIO, null, values);
        db.close();
        return id;
    }

    public boolean validateUser(String identifier, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_USUARIO + " WHERE (" + COLUMN_usuario + " = ? OR " + COLUMN_mail + " = ?) AND " + COLUMN_password + " = ?";

        // Use the identifier for both the username and email check
        Cursor cursor = db.rawQuery(query, new String[]{identifier, identifier, hashPassword(password)});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    private String hashPassword(String password) {

        return password;
    }
}
