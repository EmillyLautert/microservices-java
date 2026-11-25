package br.edu.atitus.currencyapi.servicesimpl;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import br.edu.atitus.currencyapi.services.CurrencyService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.util.NoSuchElementException;

@Service
public class CurrencyServiceJpa implements CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceJpa.class);

    private final CurrencyRepository repository;
    private final BcbClientService bcbClientService;

    @Value("${server.port:8080}")
    private String serverPort;

    public CurrencyServiceJpa(CurrencyRepository repository, BcbClientService bcbClientService) {
        this.repository = repository;
        this.bcbClientService = bcbClientService;
    }

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency) {
        String environment = "Currency API running in port " + serverPort;
        if (sourceCurrency.equalsIgnoreCase(targetCurrency)) {
            return new CurrencyResponse(sourceCurrency, targetCurrency, 1.0, environment + " (same currencies)");
        }

        try {
            Double sourceRefBrl = sourceCurrency.equalsIgnoreCase("BRL") ? 1 : bcbClientService.getConversionRate(sourceCurrency);
            Double targetRefBrl = targetCurrency.equalsIgnoreCase("BRL") ? 1 : bcbClientService.getConversionRate(targetCurrency);
            return new CurrencyResponse(sourceCurrency, targetCurrency, sourceRefBrl/targetRefBrl, environment + " (currencies in BCB)");
        } catch (RestClientException | NoSuchElementException exception) {
            logger.warn("BCB indisponível para {} -> {}; tentando a cotação local", sourceCurrency, targetCurrency);
            var currency =  repository.findBySourceCurrencyAndTargetCurrency(sourceCurrency.toUpperCase(), targetCurrency.toUpperCase())
                    .orElseThrow(() -> new EntityNotFoundException("Cotação não encontrada"));

            return new CurrencyResponse(currency.getSourceCurrency(), currency.getTargetCurrency(), currency.getConversionRate(), environment + " (currencies em Local DB)");
        }
    }
}
