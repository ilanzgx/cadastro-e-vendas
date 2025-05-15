package entities;

import java.util.UUID;

/*
 TODO: Validação do CPF
 TODO: Método ToString
 */
public class Cliente {
    private UUID id;
    private String nome;
    private String cpf;

    public Cliente(String nome, String cpf) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.cpf = cpf;
    }

    public UUID getId() {
        return id;
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
}
