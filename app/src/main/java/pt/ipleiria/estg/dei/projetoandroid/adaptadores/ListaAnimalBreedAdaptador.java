package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalBreed;

public class ListaAnimalBreedAdaptador extends BaseAdapter {


    private Context context;
    private ArrayList<AnimalBreed> breeds;
    private LayoutInflater inflater;

    public ListaAnimalBreedAdaptador(Context context, ArrayList<AnimalBreed> breeds) {
        this.context = context;
        this.breeds = breeds;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return breeds.size();
    }

    @Override
    public Object getItem(int position) {
        return breeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return breeds.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
//        }
//
//        TextView tv = convertView.findViewById(R.id.tvText);
//        Breed breed = breeds.get(position);
//        tv.setText(breed.getDescription());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
