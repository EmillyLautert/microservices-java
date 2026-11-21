package br.edu.atitus.currencyapi.servicesimpl;

import br.edu.atitus.currencyapi.clients.BcbClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class BcbClientService {

    private final BcbClient cbClient;

    public BcbClientService(BcbClient cbClient) {
        this.cbClient = cbClient;
    }

    @Retry(name = "bcb-currency")
    @CircuitBreaker(name = "bcb-currency")
    @Cacheable(
            value = "bcb-currency",
            key = "#currency"
    )
    public Double getConversionRate(String currency) {
        //Busca no BCB se existe cotação para a moeda solicitada
        LocalDate quoteDate = LocalDate.now(ZoneId.of("America/Sao_Paulo")).minusDays(1);
        while (quoteDate.getDayOfWeek().getValue() > 5) quoteDate = quoteDate.minusDays(1);
        String formattedDate = "'" + quoteDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "'";
        var conversionRate = cbClient.getBcbCurrency(String.format("'%s'", currency.toUpperCase()), formattedDate, "json");
        //Se o retorno for útli, retorna o valor da última cotação de venda do dia
        // Caso não haja cotações, ou ocorra algum outro erro, é lançado uma exception
        return conversionRate.value().getLast().cotacaoVenda();
    }
}
