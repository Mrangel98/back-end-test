package vehiculos.api.clases;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoPermiso tipoPermiso;

    private LocalDate permisoValidoHasta;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TipoPermiso getTipoPermiso() { return tipoPermiso; }
    public void setTipoPermiso(TipoPermiso tipoPermiso) { this.tipoPermiso = tipoPermiso; }

    public LocalDate getPermisoValidoHasta() { return permisoValidoHasta; }
    public void setPermisoValidoHasta(LocalDate permisoValidoHasta) {
        this.permisoValidoHasta = permisoValidoHasta;
    }
}
