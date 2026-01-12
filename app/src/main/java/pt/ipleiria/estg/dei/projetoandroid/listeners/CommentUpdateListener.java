package pt.ipleiria.estg.dei.projetoandroid.listeners;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;

public interface CommentUpdateListener {
    void onUpdateCommentSuccess(Comment comment);

    void onUpdateCommentError(String error);
}
