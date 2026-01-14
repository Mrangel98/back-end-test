package vehiculos.api.servicio;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import vehiculos.api.clases.*;
import vehiculos.api.dto.CrearVehiculo;
import vehiculos.api.repository.UsuarioRepository;
import vehiculos.api.repository.VehiculoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
public class VehiculoServicio {

    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public VehiculoServicio(VehiculoRepository vehiculoRepository,
                            UsuarioRepository usuarioRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Vehiculo crearVehiculo(CrearVehiculo request) {

    Usuario propietario = usuarioRepository.findById(request.getPropietarioId())
            .orElseThrow(() ->
                    new HttpStatusException(
                            HttpStatus.NOT_FOUND,
                            "Propietario no encontrado"
                    )
            );

    if (vehiculoRepository.findByMatricula(request.getMatricula()).isPresent()) {
        throw new HttpStatusException(
                HttpStatus.CONFLICT,
                "La matrícula ya existe"
        );
    }

    if (propietario.getPermisoValidoHasta().isBefore(LocalDate.now())) {
        throw new HttpStatusException(
                HttpStatus.BAD_REQUEST,
                "Permiso expirado"
        );
    }

    boolean permisoValido = permisoPermiteVehiculo(
            propietario.getTipoPermiso(),
            request.getTipo()
    );

    if (!permisoValido) {
        throw new HttpStatusException(
                HttpStatus.BAD_REQUEST,
                "El propietario no tiene permiso para este tipo de vehículo"
        );
    }

    Vehiculo vehiculo = new Vehiculo();
    vehiculo.setMarca(request.getMarca());
    vehiculo.setModelo(request.getModelo());
    vehiculo.setMatricula(request.getMatricula());
    vehiculo.setTipo(request.getTipo());
    vehiculo.setPropietarioId(propietario.getId());

    return vehiculoRepository.save(vehiculo);
}

public Vehiculo transferirPropietario(UUID vehiculoId, UUID nuevoPropietarioId) {

    Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
            .orElseThrow(() ->
                    new HttpStatusException(
                            HttpStatus.NOT_FOUND,
                            "Vehículo no encontrado"
                    )
            );

    Usuario nuevoPropietario = usuarioRepository.findById(nuevoPropietarioId)
            .orElseThrow(() ->
                    new HttpStatusException(
                            HttpStatus.NOT_FOUND,
                            "Nuevo propietario no encontrado"
                    )
            );

    if (vehiculo.getPropietarioId().equals(nuevoPropietarioId)) {
        throw new HttpStatusException(
                HttpStatus.BAD_REQUEST,
                "El nuevo propietario no puede ser el mismo"
        );
    }

    if (nuevoPropietario.getPermisoValidoHasta().isBefore(LocalDate.now())) {
        throw new HttpStatusException(
                HttpStatus.BAD_REQUEST,
                "Permiso del nuevo propietario expirado"
        );
    }

    if (!permisoPermiteVehiculo(
            nuevoPropietario.getTipoPermiso(),
            vehiculo.getTipo()
    )) {
        throw new HttpStatusException(
                HttpStatus.BAD_REQUEST,
                "El nuevo propietario no tiene permiso para este vehículo"
        );
    }

    vehiculo.setPropietarioId(nuevoPropietarioId);
    return vehiculoRepository.update(vehiculo);
}

private boolean permisoPermiteVehiculo(
        TipoPermiso permiso,
        TipoVehiculo tipoVehiculo
) {
    return switch (permiso) {
        case A -> tipoVehiculo == TipoVehiculo.moto;
        case B -> tipoVehiculo == TipoVehiculo.coche;
        case C -> tipoVehiculo == TipoVehiculo.camion;
    };
}

public List<Vehiculo> listarVehiculosPorUsuario(UUID usuarioId) {


    if (!usuarioRepository.existsById(usuarioId)) {
        throw new HttpStatusException(
                HttpStatus.NOT_FOUND,
                "Usuario no encontrado"
        );
    }


    return vehiculoRepository.findByPropietarioId(usuarioId);
}

}
