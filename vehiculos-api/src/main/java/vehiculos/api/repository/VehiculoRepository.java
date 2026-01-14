package vehiculos.api.repository;

import vehiculos.api.clases.Vehiculo;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, UUID> {

    Optional<Vehiculo> findByMatricula(String matricula);

    List<Vehiculo> findByPropietarioId(UUID propietarioId);
}