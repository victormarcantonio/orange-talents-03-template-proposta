package com.orange.proposta.criaProposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    boolean existsByDocumento(String documento);
    List<Proposta> findByCartaoIsNullAndStatusIs(Status status);
    Optional<Proposta>findById(UUID id);
}
