package dap.entities.actions;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Servers {
    private int id;
    private String serverName;
    private String serverIp;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "SERVER_IP", nullable = true, length = 20)
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servers servers = (Servers) o;
        return id == servers.id &&
                Objects.equals(serverName, servers.serverName) &&
                Objects.equals(serverIp, servers.serverIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverName, serverIp);
    }
}
