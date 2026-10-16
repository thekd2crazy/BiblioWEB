package be.ecam.biblioweb.repository;

import be.ecam.biblioweb.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivreRepository extends JpaRepository<Livre, UUID> {
}
