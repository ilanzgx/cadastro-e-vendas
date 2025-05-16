package entities;

public class Livro extends Produto {
    private String autor;

    public Livro() {
        super();
    }

    public Livro(Integer id, String nome, Double preco, String autor) {
        super(id, nome, preco);
        this.autor = autor;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
