package io.github.charlesanjos.sqlitebdapp.entities;

import java.io.Serializable;

public class Pessoa implements Serializable {
    private int id;
    private String nome;
    private int idade;

    public Pessoa(){};

    public Pessoa(String nomePessoa, int idadePessoa) {
        this.nome = nomePessoa;
        this.idade = idadePessoa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String toString(){
        return this.nome+", "+this.idade+" anos";
    }
}
