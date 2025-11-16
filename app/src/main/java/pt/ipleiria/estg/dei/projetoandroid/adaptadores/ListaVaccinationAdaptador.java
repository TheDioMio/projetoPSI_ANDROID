package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalSize;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Vaccination;

public class ListaVaccinationAdaptador extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<Vaccination> vaccinations;

    public ListaVaccinationAdaptador(Context context, ArrayList<Vaccination> vaccinations) {
        this.context = context;
        this.vaccinations = vaccinations;
    }


    @Override
    public int getCount() {
        return vaccinations.size();
    }

    @Override
    public Object getItem(int position) {
        return vaccinations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return vaccinations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return criarView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return criarView(position, convertView, parent);
    }

    private View criarView(int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
//        }
//
//        TextView tvText = convertView.findViewById(R.id.tvText);
//        AnimalType animalType = animalSizes.get(position);
//
//        tvText.setText(animalType.getDescription());

        return convertView;
    }

}
