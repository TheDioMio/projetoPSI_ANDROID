package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentActionListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class CommentAdaptador
        extends RecyclerView.Adapter<CommentAdaptador.ViewHolderComment> {

    private Context context;
    private ArrayList<Comment> comments;
    private CommentActionListener listener;
    private boolean isEditMode;
    private LayoutInflater inflater;

    public CommentAdaptador(Context context, ArrayList<Comment> comments, CommentActionListener listener, boolean isEditMode) {
        this.context = context;
        this.comments = comments;
        this.listener = listener;
        this.isEditMode = isEditMode;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolderComment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComment holder, int position) {
        Comment comment = comments.get(position);
        holder.update(comment, position);
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
        notifyDataSetChanged();
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    /* ---------------- ViewHolder ---------------- */

    class ViewHolderComment extends RecyclerView.ViewHolder {

        ImageView imgUserAvatar;
        TextView tvUserName, tvDate, tvCommentText;
        ImageButton btnEdit, btnDelete;
        LinearLayout layoutActions;

        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);

            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            btnEdit = itemView.findViewById(R.id.btnEditComment);
            btnDelete = itemView.findViewById(R.id.btnDeleteComment);
            layoutActions = itemView.findViewById(R.id.layoutActions);
        }



        public void update(Comment comment, int position) {

            tvUserName.setText(comment.getUserName());
            tvDate.setText(comment.getDate());
            tvCommentText.setText(comment.getText());

            String avatar = comment.getUserAvatar();

            if (avatar != null && !avatar.isEmpty()) {

                String imageUrl;

                if (avatar.startsWith("http")) {
                    imageUrl = avatar;
                } else {
                    imageUrl = AppSingleton.getInstance(context).getEndereco() + AppSingleton.getInstance(context).FRONTEND_BASE_URL + avatar;
                }

                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.mipmap.default_avatar)
                        .error(R.mipmap.default_avatar)
                        .circleCrop()
                        .into(imgUserAvatar);

            } else {
                imgUserAvatar.setImageResource(R.mipmap.default_avatar);
            }

            if (isEditMode) {
                layoutActions.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);

                int userId = AppSingleton.getInstance(context).getUserLoggedId();
                if (comment.getUserId() == userId) {
                    btnEdit.setVisibility(View.VISIBLE);
                } else {
                    btnEdit.setVisibility(View.GONE);
                }

            }else {
                layoutActions.setVisibility(View.GONE);
            }


            // EDITAR
            btnEdit.setOnClickListener(v -> {
                if (listener != null)
                    listener.onEditComment(comment, position);
            });

            // APAGAR
            btnDelete.setOnClickListener(v -> {
                if (listener != null)
                    listener.onDeleteComment(comment, position);
            });
        }
    }
}
