package dap.entities.actions;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "HISTORY_ACTIONS", schema = "PUBLIC", catalog = "H2")
public class HistoryActions {
    private int id;
    private String userName;
    private String serverName;
    private String action;
    private Date dateAction;
    private Integer exitCode;
    private Integer pid;
    private Date dateEnd;

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
    @Column(name = "USER_NAME", nullable = true, length = 20)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "SERVER_NAME", nullable = true, length = 50)
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Basic
    @Column(name = "ACTION", nullable = true, length = 50)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Basic
    @Column(name = "DATE_ACTION", nullable = true)
    public Date getDateAction() {
        return dateAction;
    }

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    @Basic
    @Column(name = "EXIT_CODE", nullable = true)
    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    @Basic
    @Column(name = "PID", nullable = true)
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "DATE_END", nullable = true)
    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryActions that = (HistoryActions) o;
        return id == that.id &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(serverName, that.serverName) &&
                Objects.equals(action, that.action) &&
                Objects.equals(dateAction, that.dateAction) &&
                Objects.equals(exitCode, that.exitCode) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(dateEnd, that.dateEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, serverName, action, dateAction, exitCode, pid, dateEnd);
    }
}
