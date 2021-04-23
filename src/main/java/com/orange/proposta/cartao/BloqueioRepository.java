package com.orange.proposta.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {

    Optional<Bloqueio> findByCartao(Cartao cartao);
}
