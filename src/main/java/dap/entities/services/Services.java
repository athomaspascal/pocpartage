package dap.entities.services;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Services {
    private int id;
    private String serviceName;
    private String componentName;
    private String serviceFamily;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SERVICE_NAME", nullable = true, length = 100)
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Basic
    @Column(name = "COMPONENT_NAME", nullable = true, length = 50)
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Basic
    @Column(name = "SERVICE_FAMILY", nullable = true, length = 100)
    public String getServiceFamily() {
        return serviceFamily;
    }

    public void setServiceFamily(String serviceFamily) {
        this.serviceFamily = serviceFamily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Services services = (Services) o;
        return id == services.id &&
                Objects.equals(serviceName, services.serviceName) &&
                Objects.equals(componentName, services.componentName) &&
                Objects.equals(serviceFamily, services.serviceFamily);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, componentName, serviceFamily);
    }
}
