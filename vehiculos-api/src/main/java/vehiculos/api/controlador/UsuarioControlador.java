package vehiculos.api.controlador;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import vehiculos.api.servicio.VehiculoServicio;

import java.util.UUID;

@Controller("/usuarios")
public class UsuarioControlador {

    private final VehiculoServicio vehiculoServicio;

    public UsuarioControlador(VehiculoServicio vehiculoServicio) {
        this.vehiculoServicio = vehiculoServicio;
    }

    @Get("/{id}/vehiculos")
    public HttpResponse<?> listarVehiculosDeUsuario(@PathVariable UUID id) {
        return HttpResponse.ok(
                vehiculoServicio.listarVehiculosPorUsuario(id)
        );
    }
}
