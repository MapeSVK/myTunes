package mytunes.be;

import java.sql.Time;
import javafx.scene.media.Media;


public class UserMedia {
    
    private int id; 
    private String title;
    private String artist;
    private String category; 
    private String path;
    private Media media;
    private Time time; 

    public UserMedia(int id, String title, String artist, String category, String path, Media media, Time time)
    {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.path = path;
        this.media = media;
        this.time = time;
    }

    public UserMedia() 
    {
            
    }
    
    public String getArtist() {
        return artist;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String file) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Title: "+title+", Artist: "+artist+", Category: "+category+", Time: ["+time+"].";
    }
}

    

