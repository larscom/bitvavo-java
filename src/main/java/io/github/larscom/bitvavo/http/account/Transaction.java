package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.UUID;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTransaction.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Transaction {
    UUID getTransactionId();

    Instant getExecutedAt();

//    {
//        "transactionId": "5f5e7b3b-4f5b-4b2d-8b2f-4f2b5b3f5e5f",
//        "executedAt": "2021-01-01T00:00:00.000Z",
//        "type": "sell",
//        "priceCurrency": "EUR",
//        "priceAmount": "1000.00",
//        "sentCurrency": "EUR",
//        "sentAmount": "0.1",
//        "receivedCurrency": "BTC",
//        "receivedAmount": "0.0001",
//        "feesCurrency": "EUR",
//        "feesAmount": "0.01",
//        "address": "string"
//    }


}
