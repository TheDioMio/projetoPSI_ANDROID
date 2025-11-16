package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalType;

public class ListaAnimalTypeAdaptador extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<AnimalType> animalTypes;

    public ListaAnimalTypeAdaptador(Context context, ArrayList<AnimalType> animalTypes) {
        this.context = context;
        this.animalTypes = animalTypes;
    }


    @Override
    public int getCount() {
        return animalTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return animalTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return animalTypes.get(position).getId();
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
//        AnimalType animalType = animalTypes.get(position);
//
//        tvText.setText(animalType.getDescription());

        return convertView;
    }
}
