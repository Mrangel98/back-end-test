package vehiculos.api.repository;

import vehiculos.api.clases.Usuario;

import java.util.UUID;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,  UUID> {
}
