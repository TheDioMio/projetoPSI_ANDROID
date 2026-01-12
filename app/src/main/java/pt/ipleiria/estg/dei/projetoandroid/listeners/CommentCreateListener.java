package pt.ipleiria.estg.dei.projetoandroid.listeners;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;

public interface CommentCreateListener{
    void onCreateCommentSuccess(Comment comment);
    void onCreateCommentError(String error);
}
