package com.orange.proposta.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(value = "cartoes", url = "${cartoes.host}")
public interface CartaoClient {

    @RequestMapping(method = RequestMethod.GET, value="/api/cartoes")
    Optional<CartaoResponse> retornaCartao(@RequestParam(value = "idProposta") String idProposta);

    @RequestMapping(method = RequestMethod.POST, value="/api/cartoes/{id}/bloqueios")
    void bloqueiaCartao(@PathVariable ("id") String id, @RequestBody BloqueioRequest bloqueioRequest);
}
