package br.edu.atitus.currencyapi.clients;

import br.edu.atitus.currencyapi.dtos.BcbQuoteResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface BcbClient {

    @GetExchange(
            "/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)?@moeda='{currency}'&@dataCotacao='{date}'&$format=json"
    )
    BcbQuoteResponse getQuotes(
            @PathVariable("currency") String currency,
            @PathVariable("date") String date
    );

}