package com.orange.proposta.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BloqueioRepository extends JpaRepository<Bloqueio, UUID> {

}
