package br.edu.atitus.currencyapi.servicesimpl;

import br.edu.atitus.currencyapi.clients.BcbClient;
import br.edu.atitus.currencyapi.dtos.BcbQuote;
import br.edu.atitus.currencyapi.dtos.BcbQuoteResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BcbCurrencyService {

    private final BcbClient bcbClient;

    public BcbCurrencyService(BcbClient bcbClient) {
        this.bcbClient = bcbClient;
    }

    @Cacheable(
            value = "bcb-quotes",
            key = "#currency + '-' + #date",
            unless = "#result == null || #result <= 0"
    )
    @Retry(name = "bcb-api")
    @CircuitBreaker(name = "bcb-api")
    public Double getQuote(String currency, String date) {

        BcbQuoteResponse response = bcbClient.getQuotes(
                currency.toUpperCase(),
                date
        );

        if (response == null
                || response.value() == null
                || response.value().isEmpty()) {

            throw new BcbQuoteNotFoundException(
                    "O BCB não possui cotação para "
                            + currency + " na data " + date
            );
        }

        BcbQuote quote = response.value().get(0);
        Double sellRate = quote.sellRate();

        if (sellRate == null || sellRate <= 0) {
            throw new BcbQuoteNotFoundException(
                    "O BCB retornou uma cotação inválida para "
                            + currency
            );
        }

        return sellRate;
    }

    public static class BcbQuoteNotFoundException
            extends RuntimeException {

        public BcbQuoteNotFoundException(String message) {
            super(message);
        }
    }

}