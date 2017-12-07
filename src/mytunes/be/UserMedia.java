package mytunes.be;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;

/**
 *
 * @author Bence
 */
public class UserMedia {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty path = new SimpleStringProperty(); //The user-readable path of the media file
    private Media media;
    private double time;
    private String timeString;

    /**
     *
     * @param id
     * @param title
     * @param artist
     * @param category
     * @param path
     * @param media
     * @param time
     */
    public UserMedia(int id, String title, String artist, String category, String path, Media media, double time) {
        this.id.set(id);
        this.title.set(title);
        this.artist.set(artist);
        this.category.set(category);
        this.path.set(path);
        this.media = media;
        this.time = time;
    }

    /**
     *
     */
    public UserMedia() {

    }

    //Creates a media from the path (for examle, when loading a UserMedia object from hte DB)

    /**
     *
     * @throws Exception
     */
    public void createMediaFromPath() throws Exception {
        System.out.println("");
        try {
            File f = new File(path.get());
            this.media = new Media(f.toURI().toString());
        }
        catch (Exception ex) {
            //If the save did not occure on the current machine, an error will occur, and the Media object will no be created
            //The data, hovewer, will not be displayed (but it will appear on the tableView)
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title.get();
    }

    /**
     *
     * @param value
     */
    public void setTitle(String value) {
        title.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return path.get();
    }

    /**
     *
     * @param value
     */
    public void setPath(String value) {
        path.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty pathProperty() {
        return path;
    }

    /**
     *
     * @return
     */
    public String getCategory() {
        return category.get();
    }

    /**
     *
     * @param value
     */
    public void setCategory(String value) {
        category.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty categoryProperty() {
        return category;
    }

    /**
     *
     * @return
     */
    public String getArtist() {
        return artist.get();
    }

    /**
     *
     * @param value
     */
    public void setArtist(String value) {
        artist.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty artistProperty() {
        return artist;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id.get();
    }

    /**
     *
     * @param value
     */
    public void setId(int value) {
        id.set(value);
    }

    /**
     *
     * @return
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     *
     * @param time
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     *
     * @return
     */
    public double getTime() {
        return time;
    }

    /**
     *
     * @return
     */
    public Media getMedia() {
        return this.media;
    }

    /**
     *
     * @return
     */
    public String getTimeString() {
        long timeInLong = new Double(time).longValue();
        int day = (int) TimeUnit.SECONDS.toDays(timeInLong);
        long hours = TimeUnit.SECONDS.toHours(timeInLong) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(timeInLong) - (TimeUnit.SECONDS.toHours(timeInLong) * 60);
        long second = TimeUnit.SECONDS.toSeconds(timeInLong) - (TimeUnit.SECONDS.toMinutes(timeInLong) * 60);
        timeString = String.format("%02d:%02d:%02d", hours, minute, second);

        return timeString;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Title: " + getTitle() + " Artist: " + getArtist() + " Category: " + getCategory() + " Time: " + getTime();
    }

    /**
     *
     * @param media
     */
    public void setMedia(Media media) {
        this.media = media;
    }

}
