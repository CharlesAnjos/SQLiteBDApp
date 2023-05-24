package io.github.charlesanjos.sqlitebdapp.dbconn;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.github.charlesanjos.sqlitebdapp.entities.Pessoa;

public class BDsqlite extends SQLiteOpenHelper {

    public static final String BD_NAME = "bdsqlite";
    //If you change the database schema, you must increment the database version.
    public static final int BD_VERSAO = 1;

    public BDsqlite(@Nullable Context context) {

        /**
         * super(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
         * @param context to use for locating paths to the the database This value may be null.
         * @param name of the database file, or null for an in-memory database This value may be null.
         * @param factory SQLiteDatabase.CursorFactory: to use for creating cursor objects, or null for the default This value may be null.
         * @param version int: number of the database (starting at 1); if the database is older, onUpgrade(SQLiteDatabase, int, int)
         * will be used to upgrade the database; if the database is newer, onDowngrade(SQLiteDatabase, int, int) will be used to downgrade the database
         */
        super(context, BD_NAME, null, BD_VERSAO);
    }

    /**
     * Responsável por GERAR A TABELA PESSOA NO BANCO
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE PESSOA(");
        query.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        query.append(" NOME TEXT NOT NULL,");
        query.append(" IDADE INT NOT NULL,");
        query.append(" TELEFONE TEXT NOT NULL,");
        query.append(" EMAIL TEXT NOT NULL)");

        db.execSQL(query.toString());
    }

    /**
     * Called when the database needs to be upgraded. The implementation should use this method to drop tables,
     * add tables, or do anything else it needs to upgrade to the new schema version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void inserirDados(){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME", "Pessoa 1"); // COLUNA / VALOR
        values.put("IDADE", "22");
        db.insert("PESSOA",null,values);

    }

    public void inserirPessoa(Pessoa pessoa){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME", pessoa.getNome()); // COLUNA / VALOR
        values.put("IDADE", pessoa.getIdade());
        values.put("TELEFONE", pessoa.getTelefone());
        values.put("EMAIL", pessoa.getEmail());
        db.insert("PESSOA",null,values);

    }

    @SuppressLint("Range")
    public List<Pessoa> consultarDados(){
        SQLiteDatabase dbselec = getReadableDatabase();

        String[] colunas = {
                "ID",
                "NOME",
                "IDADE",
                "TELEFONE",
                "EMAIL"
        };
        Cursor cursor = dbselec.query(
                "PESSOA",   // The table to query
                colunas,       // The array of columns to return (pass null to get all)
                null,          // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,          // don't group the rows
                null,          // don't filter by row groups
                null           // The sort order
        );

        List<Pessoa> pessoas = new ArrayList<>();
        while(cursor.moveToNext()) {
            Pessoa p=new Pessoa();
            p.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            p.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            p.setIdade(Integer.parseInt(cursor.getString(cursor.getColumnIndex("IDADE"))));
            p.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
            p.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            pessoas.add(p);
        }

        cursor.close();
        return pessoas;
    }

    public void excluir(int id){
        SQLiteDatabase db = getReadableDatabase();
        // Define 'where' part of query.
        String selection = "ID LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete("PESSOA", selection, selectionArgs);
    }

    public void update(int id, String coluna, String valor){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(coluna, valor);

        // Which row to update, based on the title
        String selection = "ID LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                "PESSOA",
                values,
                selection,
                selectionArgs);
    }
}