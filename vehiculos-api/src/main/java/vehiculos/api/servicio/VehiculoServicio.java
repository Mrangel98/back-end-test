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
                .orElseThrow(() -> new HttpStatusException(
                        HttpStatus.NOT_FOUND, "Propietario no encontrado"));

        validarPermiso(propietario, request.getTipo());

        vehiculoRepository.findByMatricula(request.getMatricula())
                .ifPresent(v -> {
                    throw new HttpStatusException(
                            HttpStatus.CONFLICT, "La matrícula ya existe");
                });

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(request.getMarca());
        vehiculo.setModelo(request.getModelo());
        vehiculo.setMatricula(request.getMatricula());
        vehiculo.setTipo(request.getTipo());
        vehiculo.setPropietarioId(propietario.getId());

        return vehiculoRepository.save(vehiculo);
    }

    public List<Vehiculo> listarVehiculosPorUsuario(UUID usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        return vehiculoRepository.findByPropietarioId(usuarioId);
    }

    private void validarPermiso(Usuario usuario, TipoVehiculo tipoVehiculo) {

        if (usuario.getPermisoValidoHasta().isBefore(LocalDate.now())) {
            throw new HttpStatusException(
                    HttpStatus.BAD_REQUEST, "Permiso de conducción expirado");
        }

        if (!permisoPermiteVehiculo(usuario.getTipoPermiso(), tipoVehiculo)) {
            throw new HttpStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario no tiene permiso para este tipo de vehículo");
        }
    }

    private boolean permisoPermiteVehiculo(TipoPermiso permiso,
                                           TipoVehiculo tipoVehiculo) {
        return switch (permiso) {
            case A -> tipoVehiculo == TipoVehiculo.moto;
            case B -> tipoVehiculo == TipoVehiculo.coche;
            case C -> tipoVehiculo == TipoVehiculo.camion;
        };
    }
}
