package main.java.services;

import main.java.entities.Cliente;
import main.java.repositories.ClienteRepository;

import java.util.List;

public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
        clienteRepository.criarTabela();
    }

    public void criarCliente(Cliente cliente) {
        clienteRepository.salvar(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    public Boolean editarCliente(Cliente cliente) {
        try {
            clienteRepository.atualizar(cliente);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletarCliente(String id) {
        try {
            clienteRepository.deletar(id);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
