package br.unirondon.cop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HandlerSqlite extends SQLiteOpenHelper {

	private Contato[] contatos;
	
	public HandlerSqlite(Context context) {
		super(context, "DbContato", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "CREATE TABLE IF NOT EXISTS contato(" +
				"cod_contato INTEGER PRIMARY KEY AUTOINCREMENT," +
				"nome_contato VARCHAR(30) NOT NULL," +
				"numero VARCHAR(15) NOT NULL," +
				"tipo VARCHAR(10) NOT NULL);";
		
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS contato;";
		
		db.execSQL(sql);
		onCreate(db);
	}
	
	public void inserirDados(String nome, String numero, String tipo) {
		ContentValues valores = new ContentValues();
		valores.put("nome_contato", nome);
		valores.put("numero", numero);
		valores.put("tipo", tipo);
		
		this.getWritableDatabase().insert("contato", null, valores);
	}
	
	public void atualizarDados(String id, String nome, String numero, String tipo) {
		String sql = "UPDATE contato SET nome_contato = '"+nome+"'," +
				"numero = '"+numero+"', tipo = '"+tipo+"' WHERE cod_contato = "+id+";";
		this.getWritableDatabase().execSQL(sql);
	}
	
	public void deletarDados(String id) {
		String sql = "DELETE FROM contato WHERE cod_contato = "+id+";";
		this.getWritableDatabase().execSQL(sql);
	}
	
	public Contato[] Ler() {
		
		String[] colunas = {"cod_contato", "nome_contato", "numero", "tipo"};
		
		Cursor cursor = this.getReadableDatabase().query("contato", colunas, 
				null, null, null, null, "nome_contato");
		
		int id, nome, numero, tipo;
		id = cursor.getColumnIndex(colunas[0]);
		nome = cursor.getColumnIndex(colunas[1]);
		numero = cursor.getColumnIndex(colunas[2]);
		tipo = cursor.getColumnIndex(colunas[3]);
		
		int cont = 0;
		contatos = new Contato[cursor.getCount()];
		
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			contatos[cont] = new Contato(cursor.getString(id), cursor.getString(nome),
					cursor.getString(numero), cursor.getString(tipo));
			cont++;
		}
		
		return contatos;
		
	}
	
	public Contato[] pesquisarContato(String nomePes) {
		
		String[] colunas = {"cod_contato", "nome_contato", "numero", "tipo"};
		String sql = "SELECT * FROM contato WHERE nome_contato LIKE '"+nomePes+"%';";
		
		Cursor cursor = this.getWritableDatabase().rawQuery(sql, null);
		
		int size = cursor.getCount();
		
		if(size > 0) {
			int id, nome, numero, tipo;
			id = cursor.getColumnIndex(colunas[0]);
			nome = cursor.getColumnIndex(colunas[1]);
			numero = cursor.getColumnIndex(colunas[2]);
			tipo = cursor.getColumnIndex(colunas[3]);
			
			int cont = 0;
			contatos = new Contato[cursor.getCount()];
			
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				contatos[cont] = new Contato(cursor.getString(id), cursor.getString(nome),
						cursor.getString(numero), cursor.getString(tipo));
				cont++;
			}
		}
		
		return contatos;
		
	}
	
	public void abrir() {
		this.getWritableDatabase();
	}
	
	public void fechar() {
		this.close();
	}
	
}
