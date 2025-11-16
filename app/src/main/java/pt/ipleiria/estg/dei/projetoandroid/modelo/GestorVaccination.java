package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;

public class GestorVaccination {

    private ArrayList<Vaccination> vaccinations;

    public GestorVaccination() {
        vaccinations = new ArrayList<>();
        gerarDados();
    }

    private void gerarDados() {
        vaccinations.add(new Vaccination(1, "Completa"));
        vaccinations.add(new Vaccination(2, "Parcial"));
        vaccinations.add(new Vaccination(3, "Não Vacinado"));
    }

    public ArrayList<String> getVaccinationStrings() {
        ArrayList<String> lista = new ArrayList<>();
        for (Vaccination s : vaccinations) {
            lista.add(s.getDescription());
        }
        return lista;
    }

    public ArrayList<Vaccination> getVaccinations() {
        return new ArrayList<>(vaccinations);
    }

}
