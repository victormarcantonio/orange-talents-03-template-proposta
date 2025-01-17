package com.orange.proposta.criaProposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "analises", url = "${analises.host}")
public interface PropostaClient {

    @RequestMapping(method = RequestMethod.POST, value="api/solicitacao")
    AnaliseResponse analisaProposta(@RequestBody AnaliseRequest request);
}
