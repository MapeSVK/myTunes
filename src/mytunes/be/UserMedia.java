package mytunes.be;

import java.util.concurrent.TimeUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
<<<<<<< HEAD

=======
>>>>>>> b365324f0be59c81698fe5939907a868b62f5871

public class UserMedia {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty path = new SimpleStringProperty();
    private Media media;
    private double time;
    private String timeString;

    public UserMedia(int id, String title, String artist, String category, String path, Media media, double time) {
        this.id.set(id);
        this.title.set(title);
        this.artist.set(artist);
        this.category.set(category);
        this.path.set(path);
        this.media = media;
        this.time = time;
    }

    public UserMedia() {

    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String value) {
        path.set(value);
    }

    public StringProperty pathProperty() {
        return path;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String value) {
        category.set(value);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String value) {
        artist.set(value);
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public Media getMedia() {
        return this.media;
    }

    public String getTimeString() {
        long timeInLong = Double.doubleToLongBits(this.time);
        
        int day = (int) TimeUnit.SECONDS.toDays(timeInLong);
        long hours = TimeUnit.SECONDS.toHours(timeInLong) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(timeInLong) - (TimeUnit.SECONDS.toHours(timeInLong) * 60);
        long second = TimeUnit.SECONDS.toSeconds(timeInLong) - (TimeUnit.SECONDS.toMinutes(timeInLong) * 60);

        timeString = String.format("%02d:%02d:%02d", hours, minute, second);

        return timeString;
    }

    @Override
    public String toString() {
        return "Title: " + getTitle() + " Artist: " + getArtist() + " Category: " + getCategory() + " Time: " + getTime();
    }

    public void setMedia(Media media) {
        this.media = media;
    }

}
