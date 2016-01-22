package cmri.snapshot.wall.server.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhuyin on 1/6/16.
 */
@Entity
public class Pic {
    private long id;
    private String title;
    private String path;
    private Timestamp uploadTime;
    public static Pic newOne(){
        return new Pic()
                .setTitle("")
                .setUploadTime(new Timestamp(System.currentTimeMillis()));
    }
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public Pic setTitle(String title) {
        this.title = title;
        return this;
    }

    @Basic
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public Pic setPath(String path) {
        this.path = path;
        return this;
    }

    @Basic
    @Column(name = "upload_time")
    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public Pic setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pic pic = (Pic) o;

        if (id != pic.id) return false;
        if (title != null ? !title.equals(pic.title) : pic.title != null) return false;
        if (path != null ? !path.equals(pic.path) : pic.path != null) return false;
        if (uploadTime != null ? !uploadTime.equals(pic.uploadTime) : pic.uploadTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (uploadTime != null ? uploadTime.hashCode() : 0);
        return result;
    }
}
