package com.example.edy_projeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome e versão do banco de dados
    private static final String DATABASE_NAME = "users.db"; // Nome do banco de dados
    private static final int DATABASE_VERSION = 1; // Versão do banco de dados

    // Nome da tabela e colunas
    private static final String TABLE_USERS = "USERS"; // Nome da tabela de usuários
    private static final String COLUMN_ID = "ID"; // Nome da coluna de ID
    private static final String COLUMN_USERNAME = "USERNAME"; // Nome da coluna de nome de usuário
    private static final String COLUMN_EMAIL = "EMAIL"; // Nome da coluna de e-mail
    private static final String COLUMN_PASSWORD = "PASSWORD"; // Nome da coluna de senha

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de usuários
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualização do banco de dados
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Método para inserir um novo usuário
    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtém uma instância do banco de dados para escrita
        ContentValues contentValues = new ContentValues(); // Objeto para armazenar valores
        contentValues.put(COLUMN_USERNAME, username); // Adiciona o nome de usuário
        contentValues.put(COLUMN_EMAIL, email); // Adiciona o e-mail
        contentValues.put(COLUMN_PASSWORD, password); // Adiciona a senha
        long result = db.insert(TABLE_USERS, null, contentValues); // Insere os valores na tabela
        return result != -1; // Retorna true se a inserção foi bem-sucedida
    }

    // Método para obter todos os usuários
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, null, null, null, null, null);
    }
}
