package com.orange.proposta.biometria;

import com.orange.proposta.biometria.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BiometriaRepository extends JpaRepository<Biometria, UUID> {
}
