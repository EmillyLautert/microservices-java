package br.edu.atitus.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BcbQuoteResponse(

        @JsonProperty("@odata.context")
        String context,

        @JsonProperty("value")
        List<BcbQuote> value

) {
}