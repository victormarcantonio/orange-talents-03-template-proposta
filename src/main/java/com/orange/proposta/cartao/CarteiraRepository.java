package com.orange.proposta.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarteiraRepository extends JpaRepository<Carteira, UUID> {
    Carteira findByCartao(Cartao cartao);
}
