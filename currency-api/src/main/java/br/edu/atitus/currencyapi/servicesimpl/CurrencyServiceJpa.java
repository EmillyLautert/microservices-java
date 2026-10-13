package br.edu.atitus.currencyapi.servicesimpl;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import br.edu.atitus.currencyapi.services.CurrencyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceJpa implements CurrencyService {

    private final CurrencyRepository repository;
    private final BcbCurrencyService bcbCurrencyService;

    public CurrencyServiceJpa(
            CurrencyRepository repository,
            BcbCurrencyService bcbCurrencyService) {

        this.repository = repository;
        this.bcbCurrencyService = bcbCurrencyService;
    }

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${currency.bcb.quote-date:10-09-2026}")
    private String quoteDate;

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(
            String sourceCurrency,
            String targetCurrency
    ) throws Exception {

        String source = normalizeCurrency(sourceCurrency);
        String target = normalizeCurrency(targetCurrency);

        String environment =
                "Currency API running in Port: " + serverPort;

        try {
            double sourceRate = getBcbRate(source);
            double targetRate = getBcbRate(target);

            double conversionRate = sourceRate / targetRate;

            if (!Double.isFinite(conversionRate)
                    || conversionRate <= 0) {

                throw new IllegalStateException(
                        "A API do BCB retornou uma taxa inválida"
                );
            }

            return new CurrencyResponse(
                    source,
                    target,
                    conversionRate,
                    environment
                            + " - Cotação obtida no BCB em "
                            + quoteDate
            );

        } catch (Exception ex) {

            var localCurrency = repository
                    .findBySourceCurrencyAndTargetCurrency(
                            source,
                            target
                    )
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Cotação não encontrada no BCB nem no "
                                    + "banco local para "
                                    + source + " -> " + target
                    ));

            return CurrencyResponse.fromEntity(
                    localCurrency,
                    environment
                            + " - FALLBACK: cotação do banco local. "
                            + "Motivo: "
                            + safeMessage(ex)
            );
        }
    }

    private double getBcbRate(String currency) {

        if ("BRL".equals(currency)) {
            return 1.0;
        }

        return bcbCurrencyService.getQuote(
                currency,
                quoteDate
        );
    }

    private String normalizeCurrency(String currency) {

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException(
                    "A moeda deve ser informada"
            );
        }

        return currency.trim().toUpperCase();
    }

    private String safeMessage(Exception ex) {

        String message = ex.getMessage();

        return message == null || message.isBlank()
                ? ex.getClass().getSimpleName()
                : message;
    }

}