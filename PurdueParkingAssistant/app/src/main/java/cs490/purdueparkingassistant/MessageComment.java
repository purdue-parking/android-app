package cs490.purdueparkingassistant;

/**
 * Created by Craig on 12/5/2015.
 */
public class MessageComment {

    private String content;
    private int parent;
    private String author;

    public MessageComment() {
        content = "None";
        author = "None";
        parent = -1;
    }

    public MessageComment(String content, String author, int parent) {
        this.content = content;
        this.author = author;
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
