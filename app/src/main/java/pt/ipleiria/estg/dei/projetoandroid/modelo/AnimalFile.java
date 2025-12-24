package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;

public class AnimalFile implements Serializable {

    private int idFile;
    private int idAnimal;
    private String fileAddress;

    public AnimalFile(int idFile, int idAnimal, String fileAddress) {
        this.idFile = idFile;
        this.idAnimal = idAnimal;
        this.fileAddress = fileAddress;
    }

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
}
