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
    private static final int DATABASE_VERSION = 3;

    //iNGREDIENTE

    public static final String TABLA_INGREDIENTES="Ingredientes";
    public static final String Id_Ingrediente="id";
    public static final String COLUMN_Nombre_Ingrediente="nombre";
    public static final String COLUMN_info="info";
    //INVENTARIO
    public static final String TABLA_INVENTARIO="Inventario";
    public static final String Id_Inventario="id";
    public static final String COLUMN_Ingrediente="ingrediente";
    public static final String COLUMN_Cantidad="cantidad";





    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLA_INGREDIENTES = " CREATE TABLE " +TABLA_INGREDIENTES+"("+
                Id_Ingrediente + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Nombre_Ingrediente + " TEXT, " +
                COLUMN_info + " INTEGER" + ")";
        db.execSQL(CREATE_TABLA_INGREDIENTES);
        String CREATE_TABLA_INVENTARIO= " CREATE TABLE " +TABLA_INVENTARIO+"("+
                Id_Inventario + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Ingrediente + " TEXT, " +
                COLUMN_Cantidad + " INTEGER" + ")";
        db.execSQL(CREATE_TABLA_INVENTARIO);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_INVENTARIO);
        onCreate(db);
    }



}
