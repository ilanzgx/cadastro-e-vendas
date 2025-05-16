package services;

import entities.Cliente;
import repositories.ClienteRepository;

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

    public void editarCliente(Cliente cliente) {
        clienteRepository.atualizar(cliente);
    }

    public void deletarCliente(String id) {
        clienteRepository.deletar(id);
    }
}
