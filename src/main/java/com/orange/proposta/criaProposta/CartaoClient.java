package com.orange.proposta.criaProposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@FeignClient(value = "cartoes", url = "${cartoes.host}")
public interface CartaoClient {

    @RequestMapping(method = RequestMethod.GET, value="/api/cartoes")
    Optional<CartaoResponse> retornaCartao(@RequestParam(value = "idProposta") String idProposta);
}
