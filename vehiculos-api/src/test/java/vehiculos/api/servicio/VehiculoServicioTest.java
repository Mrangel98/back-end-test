package vehiculos.api.servicio;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vehiculos.api.clases.*;
import vehiculos.api.dto.CrearVehiculo;
import vehiculos.api.repository.UsuarioRepository;
import vehiculos.api.repository.VehiculoRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehiculoServicioTest {

    private VehiculoRepository vehiculoRepository;
    private UsuarioRepository usuarioRepository;
    private VehiculoServicio vehiculoServicio;

    @BeforeEach
    void setUp() {
        vehiculoRepository = Mockito.mock(VehiculoRepository.class);
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        vehiculoServicio = new VehiculoServicio(vehiculoRepository, usuarioRepository);
    }

    @Test
    void crearVehiculo_conPermisoValido_deberiaCrearVehiculo() {

        UUID usuarioId = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setTipoPermiso(TipoPermiso.B);
        usuario.setPermisoValidoHasta(LocalDate.now().plusYears(1));

        CrearVehiculo request = new CrearVehiculo();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setMatricula("1234ABC");
        request.setTipo(TipoVehiculo.coche);
        request.setPropietarioId(usuarioId);

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(vehiculoRepository.findByMatricula("1234ABC"))
                .thenReturn(Optional.empty());

        when(vehiculoRepository.save(any(Vehiculo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Vehiculo resultado = vehiculoServicio.crearVehiculo(request);


        assertNotNull(resultado);
        assertEquals(TipoVehiculo.coche, resultado.getTipo());
        assertEquals(usuarioId, resultado.getPropietarioId());
    }

    @Test
    void crearVehiculo_conPermisoIncorrecto_deberiaLanzarBadRequest() {


        UUID usuarioId = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setTipoPermiso(TipoPermiso.A); 
        usuario.setPermisoValidoHasta(LocalDate.now().plusYears(1));

        CrearVehiculo request = new CrearVehiculo();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setMatricula("9999ZZZ");
        request.setTipo(TipoVehiculo.coche); 
        request.setPropietarioId(usuarioId);

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));


        HttpStatusException ex = assertThrows(
                HttpStatusException.class,
                () -> vehiculoServicio.crearVehiculo(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
}
