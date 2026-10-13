package br.edu.atitus.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BcbQuote(

        @JsonProperty("cotacaoCompra")
        Double buyRate,

        @JsonProperty("cotacaoVenda")
        Double sellRate,

        @JsonProperty("dataHoraCotacao")
        String quoteDateTime,

        @JsonProperty("tipoBoletim")
        String bulletinType

) {
}