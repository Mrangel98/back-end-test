package vehiculos.api.controlador;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import vehiculos.api.dto.CrearVehiculo;
import vehiculos.api.servicio.VehiculoServicio;

@Controller("/vehiculos")
public class VehiculoControlador {

    private final VehiculoServicio vehiculoServicio;

    public VehiculoControlador(VehiculoServicio vehiculoServicio) {
        this.vehiculoServicio = vehiculoServicio;
    }

    @Post
    public HttpResponse<?> crear(@Body CrearVehiculo dto) {
        return HttpResponse.created(
                vehiculoServicio.crearVehiculo(dto)
        );
    }
}
