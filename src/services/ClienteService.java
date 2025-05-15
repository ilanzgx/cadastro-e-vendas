package services;

import repositories.ClienteRepository;

public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
        clienteRepository.criarTabela();
    }

    public void criarCliente() {}
    public void listarClientes() {}
    public void editarCliente() {}
    public void deletarCliente() {}
}
