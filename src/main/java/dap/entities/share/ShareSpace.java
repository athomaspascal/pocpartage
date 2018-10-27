package dap.entities.share;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "SHARE_SPACE", schema = "PUBLIC", catalog = "H2")
public class ShareSpace {
    private int id;
    private String shareSpaceName;
    private String nameDirectory;



    private Date dateCreation;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "DATE_CREATION", nullable = true, length = 50)
    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Basic
    @Column(name = "SHARE_SPACE_NAME", nullable = true, length = 50)
    public String getShareSpaceName() {
        return shareSpaceName;
    }

    public void setShareSpaceName(String shareSpaceName) {
        this.shareSpaceName = shareSpaceName;
    }

    @Basic
    @Column(name = "NAME_DIRECTORY", nullable = true, length = 50)
    public String getNameDirectory() {
        return nameDirectory;
    }

    public void setNameDirectory(String nameDirectory) {
        this.nameDirectory = nameDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareSpace that = (ShareSpace) o;
        return id == that.id &&
                Objects.equals(shareSpaceName, that.shareSpaceName) &&
                Objects.equals(nameDirectory, that.nameDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shareSpaceName, nameDirectory);
    }
}
