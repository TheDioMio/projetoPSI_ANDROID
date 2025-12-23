package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;

public class AnimalFilter implements Serializable {

    public String tipo;
    public String raca;
    public String tamanho;
    public String idade;
    public String vacinacao;
    public String dono;
    public String nome;

    public boolean isDefault(String valor) {
        return valor == null
                || valor.trim().isEmpty()
                || valor.startsWith("Todos");
    }
}
