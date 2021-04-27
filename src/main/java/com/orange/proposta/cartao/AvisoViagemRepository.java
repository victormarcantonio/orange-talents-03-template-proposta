package com.orange.proposta.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvisoViagemRepository extends JpaRepository<AvisoViagem, UUID> {
}
