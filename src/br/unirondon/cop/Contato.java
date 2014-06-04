package br.unirondon.cop;

import java.io.Serializable;

public class Contato implements Serializable {

	private String id, nome, numero, tipo;

	public Contato() {
		this.id = null;
		this.nome = null;
		this.numero = null;
		this.tipo = null;
	}

	public Contato(String id, String nome, String numero, String tipo) {
		this.id = id;
		this.nome = nome;
		this.numero = numero;
		this.tipo = tipo;
	}

	public Contato(String nome, String numero, String tipo) {
		this.nome = nome;
		this.numero = numero;
		this.tipo = tipo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
