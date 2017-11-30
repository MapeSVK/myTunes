package mytunes.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;


public class UserMedia {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty path = new SimpleStringProperty();
    private Media media;
    private int time; 

    public UserMedia(int id, String title, String artist, String category, String path, Media media, int time)
    {
        this.id.set(id);
        this.title.set(title);
        this.artist.set(artist);
        this.category.set(category);
        this.path.set(path);
        this.media = media;
        this.time = time;
    }

    public UserMedia() 
    {
            
    }
    
    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String value)
    {
        title.set(value);
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public String getPath()
    {
        return path.get();
    }

    public void setPath(String value)
    {
        path.set(value);
    }

    public StringProperty pathProperty()
    {
        return path;
    }

    public String getCategory()
    {
        return category.get();
    }

    public void setCategory(String value)
    {
        category.set(value);
    }

    public StringProperty categoryProperty()
    {
        return category;
    }

    public String getArtist()
    {
        return artist.get();
    }

    public void setArtist(String value)
    {
        artist.set(value);
    }

    public StringProperty artistProperty()
    {
        return artist;
    }
    
    public int getId()
    {
        return id.get();
    }
    
    public void setId(int value)
    {
        id.set(value);
    }

    public IntegerProperty idProperty()
    {
        return id;
    }
    
    public void setTime(int time)
    {
        this.time = time;
    }
    
    public int getTime()
    {
        return time;
    }

    @Override
    public String toString()
    {
        return "Title: " + getTitle() + " Artist: " + getArtist() + " Category: " + getCategory() + " Time: " + getTime();
    }
 
}

    

