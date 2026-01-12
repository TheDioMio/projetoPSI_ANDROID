package pt.ipleiria.estg.dei.projetoandroid.listeners;

public interface CommentDeleteListener {
    void onDeleteCommentSuccess(int commentId);
    void onDeleteCommentError(String error);
}
