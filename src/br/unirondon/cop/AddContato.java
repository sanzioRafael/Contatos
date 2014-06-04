package br.unirondon.cop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddContato extends Activity {
	
	private EditText etNome, etNumero;
	private Spinner spTipos;
	private Toast aviso;
	private Contato contato;
	private Button btnSalvar;
	private HandlerSqlite aux;
	private Intent intecao;
	private Context contexto;
	private int tempo;
	private String[] nomes, numeros;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adiciona_contato);
		
		intecao = getIntent();
		contato = (Contato) intecao.getExtras().getSerializable("contato");
		nomes = intecao.getStringArrayExtra("nomes");
		numeros = intecao.getStringArrayExtra("numeros");
		
		String[] tipos = getResources().getStringArray(R.array.tipos);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
		
		spTipos = (Spinner) findViewById(R.spinner.spTipos);
		spTipos.setAdapter(adapter);
		
		btnSalvar = (Button) findViewById(R.button.btnSalvar);
		btnSalvar.setOnClickListener(new SalvarOnClickListener());
		
	}
	
	private class SalvarOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			etNome = (EditText) findViewById(R.editText.etNome);
			etNumero = (EditText) findViewById(R.editText.etNumero);
			boolean existe = false, limite = false;
			
			if(!etNome.getText().toString().isEmpty() && !etNumero.getText().toString().isEmpty()) {
				
				if(nomes != null) {
				
					for (int i = 0; i < nomes.length; i++) {
						if(nomes[i].equals(etNome.getText().toString())){
							existe = true;
							break;
						}
						if(numeros[i].equals(etNumero.getText().toString())){
							existe = true;
							break;
						}
					}
					
				}
				
				if(etNome.getText().toString().length() >= 20 
						|| etNumero.getText().toString().length() >= 20)
					limite = true;
				
				if(!existe) {
					if(!limite) {
						aux = new HandlerSqlite(getApplicationContext());
						aux.abrir();
						
						contato = new Contato(etNome.getText().toString(),
								etNumero.getText().toString(), spTipos.getSelectedItem().toString());
						
						aux.inserirDados(contato.getNome(), contato.getNumero(), contato.getTipo());
						
						mensagem("Contato adicionado com sucesso!!!");
						
						aux.fechar();
						
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
