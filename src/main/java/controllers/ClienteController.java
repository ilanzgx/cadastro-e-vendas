package main.java.controllers;

import main.java.entities.Cliente;
import main.java.services.ClienteService;

import java.util.List;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController() {
        this.clienteService = new ClienteService();
    }

    public Cliente salvarCliente(String nome, String cpf) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);

        clienteService.criarCliente(cliente);
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }
}
