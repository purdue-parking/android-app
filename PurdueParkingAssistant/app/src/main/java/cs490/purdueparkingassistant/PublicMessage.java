package cs490.purdueparkingassistant;

/**
 * Created by Craig on 10/24/2015.
 */
public class PublicMessage {

    private String content;
    private String poster;
    private boolean helpNeeded = false;
    private int votes = 0;
    private int id;


    public PublicMessage(int id, String content, String poster, boolean helpNeeded, int votes) {
        this.content = content;
        this.poster = poster;
        this.id = id;
        this.helpNeeded = helpNeeded;
        this.votes = votes;
    }

    //For New Messages
    public PublicMessage(String content, String poster, boolean helpNeeded) {
        this.content = content;
        this.poster = poster;
        this.id = -1;
        this.helpNeeded = helpNeeded;
        this.votes = 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public boolean isHelpNeeded() {
        return helpNeeded;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
