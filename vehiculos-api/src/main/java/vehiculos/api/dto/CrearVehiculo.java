package vehiculos.api.dto;

import vehiculos.api.clases.TipoVehiculo;
import java.util.UUID;

public class CrearVehiculo {

    private String marca;
    private String modelo;
    private String matricula;
    private TipoVehiculo tipo;
    private UUID propietarioId;

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public TipoVehiculo getTipo() { return tipo; }
    public void setTipo(TipoVehiculo tipo) { this.tipo = tipo; }

    public UUID getPropietarioId() { return propietarioId; }
    public void setPropietarioId(UUID propietarioId) {
        this.propietarioId = propietarioId;
    }
}
