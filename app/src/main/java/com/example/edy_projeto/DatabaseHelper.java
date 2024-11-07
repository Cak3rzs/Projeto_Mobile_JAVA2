package com.example.edy_projeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome e versão do banco de dados
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;

    // Tabela de usuários e colunas
    private static final String TABLE_USERS = "USERS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_EMAIL = "EMAIL";
    private static final String COLUMN_PASSWORD = "PASSWORD";

    // Tabela de projetos e colunas
    private static final String TABLE_PROJECTS = "PROJECTS";
    private static final String COLUMN_PROJECT_ID = "PROJECT_ID";
    private static final String COLUMN_PROJECT_TITLE = "TITLE";
    private static final String COLUMN_PROJECT_DESCRIPTION = "DESCRIPTION";

    // Tabela de tarefas e colunas
    private static final String TABLE_TASKS = "TASKS";
    private static final String COLUMN_TASK_ID = "TASK_ID";
    private static final String COLUMN_TASK_NAME = "TASK_NAME";
    private static final String COLUMN_TASK_DESCRIPTION = "DESCRIPTION"; // Nova coluna para descrição
    private static final String COLUMN_PROJECT_FOREIGN_KEY = "PROJECT_ID_FK";
    private static final String COLUMN_TASK_DUE_DATE = "TASK_DUE_DATE"; // Nova coluna para data de vencimento

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

        // Criação da tabela de projetos
        String createProjectsTable = "CREATE TABLE " + TABLE_PROJECTS + " ("
                + COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PROJECT_TITLE + " TEXT, "
                + COLUMN_PROJECT_DESCRIPTION + " TEXT)";
        db.execSQL(createProjectsTable);

        // Criação da tabela de tarefas
        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " ("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_NAME + " TEXT, "
                + COLUMN_TASK_DESCRIPTION + " TEXT, "
                + COLUMN_PROJECT_FOREIGN_KEY + " INTEGER, "
                + COLUMN_TASK_DUE_DATE + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_PROJECT_FOREIGN_KEY + ") REFERENCES " + TABLE_PROJECTS + "(" + COLUMN_PROJECT_ID + "))";
        db.execSQL(createTasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Método para inserir um novo usuário
    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Método para inserir um novo projeto
    public boolean insertProject(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PROJECT_TITLE, title);
        contentValues.put(COLUMN_PROJECT_DESCRIPTION, description);
        long result = db.insert(TABLE_PROJECTS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Método para inserir uma nova tarefa associada a um projeto
    public boolean insertTask(String taskName, String projectId, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASK_NAME, taskName);
        contentValues.put(COLUMN_PROJECT_FOREIGN_KEY, projectId);  // Armazena o ID do projeto
        contentValues.put(COLUMN_TASK_DUE_DATE, dueDate);  // Armazena a data de vencimento
        long result = db.insert(TABLE_TASKS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Método para obter todos os usuários
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, null, null, null, null, null);
    }

    // Método para obter todos os projetos
    public Cursor getAllProjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PROJECTS, null, null, null, null, null, null);
    }

    // Método para obter os títulos de todos os projetos
    public ArrayList<String> getAllProjectTitles() {
        ArrayList<String> projectTitles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROJECTS, new String[]{COLUMN_PROJECT_TITLE}, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(COLUMN_PROJECT_TITLE);
            if (titleColumnIndex != -1) {
                do {
                    String title = cursor.getString(titleColumnIndex);
                    projectTitles.add(title);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return projectTitles;
    }

    // Método para obter todas as tarefas de um projeto específico
    public Cursor getTasksForProject(int projectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PROJECT_FOREIGN_KEY + " = ?";
        String[] selectionArgs = {String.valueOf(projectId)};
        return db.query(TABLE_TASKS, null, selection, selectionArgs, null, null, null);
    }

    // Método para deletar um projeto
    public boolean deleteProject(int projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PROJECTS, COLUMN_PROJECT_ID + " = ?", new String[]{String.valueOf(projectId)});
        db.close();
        return result > 0;
    }

    // Método para deletar uma tarefa
    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return result > 0;
    }

    // Método para atualizar informações do projeto
    public boolean updateProject(int projectId, String newTitle, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PROJECT_TITLE, newTitle);
        contentValues.put(COLUMN_PROJECT_DESCRIPTION, newDescription);
        int result = db.update(TABLE_PROJECTS, contentValues, COLUMN_PROJECT_ID + " = ?", new String[]{String.valueOf(projectId)});
        db.close();
        return result > 0;
    }

    // Método para atualizar informações da tarefa
    public boolean updateTask(int taskId, String newTaskName, String newTaskDescription, String newDueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASK_NAME, newTaskName);
        contentValues.put(COLUMN_TASK_DESCRIPTION, newTaskDescription); // Atualiza a descrição da tarefa
        contentValues.put(COLUMN_TASK_DUE_DATE, newDueDate); // Atualiza a data de vencimento
        int result = db.update(TABLE_TASKS, contentValues, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return result > 0;
    }
}
