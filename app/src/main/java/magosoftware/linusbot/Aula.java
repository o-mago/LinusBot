package magosoftware.linusbot;

public class Aula {

    String titulo;
    String data;
    String professor;
    String descricao;
    String local;

    public Aula(String titulo, String data, String professor, String descricao, String local) {
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.professor = professor;
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocal() {
        return local;
    }

    public String getProfessor() {
        return professor;
    }

    public String getTitulo() {
        return titulo;
    }
}
