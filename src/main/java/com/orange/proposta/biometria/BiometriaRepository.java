package com.orange.proposta.biometria;

import com.orange.proposta.biometria.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiometriaRepository extends JpaRepository<Biometria, Long> {
}
