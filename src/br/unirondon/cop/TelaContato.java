package br.unirondon.cop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TelaContato extends Activity {
	
	private Intent intCall;
	private Contato contato, contatoOrigin;
	private EditText etNomeContato, etNumeroContato;
	private Spinner spTiposContato;
	private Button btnContato;
	private Toast aviso;
	private HandlerSqlite aux;
	private Context contexto;
	private int tempo;
	private String[] nomes, numeros;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_contato);
		
		contatoOrigin = (Contato) getIntent().getSerializableExtra("contato");
		nomes = getIntent().getStringArrayExtra("nomes");
		numeros = getIntent().getStringArrayExtra("numeros");
		
		btnContato = (Button) findViewById(R.button.btnContato);
		btnContato.setOnClickListener(new ButtonOnClickListener());
		btnContato.setVisibility(View.INVISIBLE);
		
		String[] tipos = getResources().getStringArray(R.array.tipos);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
		int pos = 0;
		
		for (int i = 0; i < tipos.length; i++) {
			if(tipos[i].equals(contatoOrigin.getTipo())){
				pos = i;
				break;
			}
		}
		
		spTiposContato = (Spinner) findViewById(R.spinner.spTiposContato);
		spTiposContato.setAdapter(adapter);
		spTiposContato.setSelection(pos);
		
		etNomeContato = (EditText) findViewById(R.editText.etNomeContato);
		etNomeContato.setText(contatoOrigin.getNome());
						
		etNumeroContato = (EditText) findViewById(R.editText.etNumeroContato);
		etNumeroContato.setText(contatoOrigin.getNumero());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_contato, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.menu.menu_update: {
				btnContato.setVisibility(View.VISIBLE);
				return true;
			}
			case R.menu.menu_delete: {
				btnContato.setVisibility(View.INVISIBLE);
				
				aux = new HandlerSqlite(getApplicationContext());
				
				aux.abrir();
				aux.deletarDados(contatoOrigin.getId());
				mensagem("Contato deletado com sucesso!");
				aux.fechar();
				
				finish();
				
				return true;
			}
			case R.menu.menu_call: {
				Uri numero = Uri.parse("tel:"+contatoOrigin.getNumero());
				intCall = new Intent(Intent.ACTION_CALL, numero);
				startActivity(intCall);
				
				return true;
			}
			default:
				return false;
		}
	}
	
	private class ButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			aux = new HandlerSqlite(getApplicationContext());
			boolean existe = false, limite = false;
			
			if(!etNomeContato.getText().toString().isEmpty() && !etNumeroContato.getText().toString().isEmpty()) {
				for (int i = 0; i < nomes.length; i++) {
					if(nomes[i].equals(etNomeContato.getText().toString()) && 
							!nomes[i].equals(contatoOrigin.getNome())){
						existe = true;
						break;
					}
					if(numeros[i].equals(etNumeroContato.getText().toString()) && 
							!nomes[i].equals(contatoOrigin.getNome())){
						existe = true;
						break;
					}
				}
				
				if(etNomeContato.getText().toString().length() >= 20 
						|| etNumeroContato.getText().toString().length() >= 20)
					limite = true;
				
				if(!existe) {
					if(!limite) {
						aux = new HandlerSqlite(getApplicationContext());
						aux.abrir();
						
						contato = new Contato(contatoOrigin.getId() ,etNomeContato.getText().toString(),
								etNumeroContato.getText().toString(), spTiposContato.getSelectedItem().toString());
						
						aux.atualizarDados(contato.getId(), contato.getNome(),
								contato.getNumero(), contato.getTipo());
						
						mensagem("Contato alterado com sucesso!!!");
						
						aux.fechar();
						
						contatoOrigin = contato;
						
						finish();
					}else
						mensagem("Limite de letras, estrapolaram o limite");
				} else
					mensagem("Este contato já existe");
			} else
				mensagem("Os campos estão em branco, preencha-os");
		}		
	}
	
	public void mensagem(String texto) {
		contexto = getApplicationContext();
		tempo = Toast.LENGTH_SHORT;
		aviso = Toast.makeText(contexto, texto, tempo);
		aviso.show();
	}
	
}
