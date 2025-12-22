package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Message;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class ListaMessagesAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Message> messages;

    public ListaMessagesAdaptador(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = messages.get(position);

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message, null);
        }

        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.update(message);

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvSubject, tvSender, tvPreview, tvDate;

        public ViewHolderLista(View view) {
            tvSubject = view.findViewById(R.id.tvSubject);
            tvSender  = view.findViewById(R.id.tvSender);
            tvPreview = view.findViewById(R.id.tvPreview);
            tvDate    = view.findViewById(R.id.tvDate);
        }

        public void update(Message message) {
            tvSubject.setText(message.getSubject());
            tvPreview.setText(message.getText());
            tvDate.setText(message.getCreated_at());

            // ir buscar o nome do remetente ao Singleton
            User sender = AppSingleton.getInstance(context).getUser(message.getSender_user_id());
            if (sender != null) {
                tvSender.setText("De: " + sender.getName());
            } else {
                tvSender.setText("De: Utilizador #" + message.getSender_user_id());
            }
        }
    }

    public void atualizar(ArrayList<Message> novaLista) {
        this.messages = novaLista;
        notifyDataSetChanged();
    }
}
