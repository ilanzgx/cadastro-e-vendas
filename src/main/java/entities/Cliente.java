package main.java.entities;

import main.java.utils.CpfUtils;

import java.util.UUID;

/*
 TODO: Validação do CPF
 */
public class Cliente {
    private UUID id;
    private String nome;
    private String cpf;

    public Cliente() {
        this.id = UUID.randomUUID();
    }

    public Cliente(UUID id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public Cliente(String nome, String cpf) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.cpf = cpf;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getId() {
        return id.toString();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return String.format("%s (CPF: %s)", nome, CpfUtils.format(cpf));
    }
}
