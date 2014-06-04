package br.unirondon.cop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MenuInicial extends Activity {
	
	private ListView lvContatos;
	private HandlerSqlite aux;
	private Intent intAdd, intCont;
	private Contato[] contatos;
	private String[] nomes, numeros;
	private Integer[] imgs;
	private ListaCostumizada adpt;
	private EditText etNome;
	private Button btnPesquisar;
	private LayoutInflater layout;
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_inicial);
		aux = new HandlerSqlite(this);
		
		lvContatos = (ListView) findViewById(R.listView.lvContatos);
		lvContatos.setOnItemClickListener(new ContatosOnItemClickListener());
		atualizarContatos();
		
		intAdd = new Intent(this, AddContato.class);
		intCont = new Intent(this, TelaContato.class);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		atualizarContatos();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		atualizarContatos();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_inicial, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		aux.fechar();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.menu.menu_add:{
				intAdd.putExtra("nomes", nomes);
				intAdd.putExtra("numeros", numeros);
				startActivity(intAdd);
				return true;
			}
			case R.menu.menu_search:{
				AlertDialog.Builder janela = new AlertDialog.Builder(this);
				
				layout = getLayoutInflater();
				view = layout.inflate(R.layout.pesquisa_contato, null);
				
				aux.abrir();
				
				btnPesquisar = (Button) view.findViewById(R.button.btnPesquisar);
				btnPesquisar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						etNome = (EditText) view.findViewById(R.editText.etNomePesq);
						
						if(!etNome.getText().toString().isEmpty()) {
							
							contatos = aux.pesquisarContato(etNome.getText().toString());
							
							if(contatos.length != 0)
								verificarElementos();
							else
								atualizarContatos();
							
						} else
							mensagem("O campo está em branco, preencha-o", Toast.LENGTH_SHORT);
						
					}
				});
				
				aux.fechar();
				
				janela.setTitle("Pesquisar");
				janela.setIcon(R.drawable.ic_menu_search);
				janela.setView(view);
				janela.show();
				
				return true;
			}
			case R.menu.menu_more:{
				AlertDialog.Builder janela = new AlertDialog.Builder(this);
				
				layout = getLayoutInflater();
				view = layout.inflate(R.layout.alerta, null);
				
				janela.setTitle("Desenvolvedores");
				janela.setIcon(R.drawable.ic_menu_more);
				janela.setView(view);
				janela.show();
				
				return true;
			}
			default:
				return false;
		}
	}
	
	public void atualizarContatos() {
		aux.abrir();
		
		contatos = aux.Ler();
		
		verificarElementos();
		
		aux.fechar();		
	}
	
	public void verificarElementos() {
		if(contatos.length != 0) {
			nomes = new String[contatos.length];
			numeros = new String[contatos.length];
			imgs = new Integer[contatos.length];
			
			for (int i = 0; i < contatos.length; i++) {
				
				nomes[i] = contatos[i].getNome();
				numeros[i] = contatos[i].getNumero();
				
				if(contatos[i].getTipo().equals("Celular"))
					imgs[i] = R.drawable.ic_cel_icon;
				else if(contatos[i].getTipo().equals("Tabblet"))
					imgs[i] = R.drawable.ic_tabblet_icon;
				else if(contatos[i].getTipo().equals("Telefone Fixo"))
					imgs[i] = R.drawable.ic_tel_icon;
				else if(contatos[i].getTipo().equals("Fax"))
					imgs[i] = R.drawable.ic_fax_icon;
				
			}
			
			adpt = new ListaCostumizada(MenuInicial.this, nomes, imgs);
			
			lvContatos.setAdapter(adpt);
			
			aux.fechar();
		} else {
			mensagem("Lista não possue nenhum, contato", Toast.LENGTH_LONG);
			aux.fechar();
		}
	}
	
	public void mensagem(String texto, int tempo) {
		Toast t = Toast.makeText(getApplicationContext(), texto, tempo);
		t.show();
	}
	
	private class ContatosOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			intCont.putExtra("contato", contatos[position]);
			intCont.putExtra("nomes", nomes);
			intCont.putExtra("numeros", numeros);
			startActivity(intCont);
		}
	}
	
}
