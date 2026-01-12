package pt.ipleiria.estg.dei.projetoandroid.listeners;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;

public interface CommentActionListener {
    void onEditComment(Comment comment, int position);
    void onDeleteComment(Comment comment, int position);
}
