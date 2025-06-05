package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

class AccountFeeDeserializer extends JsonDeserializer<AccountFee> {

    @Override
    public AccountFee deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final ObjectNode node = p.getCodec().readTree(p);

        final JsonNode fees = node.get("fees");
        if (fees == null) {
            throw new JsonMappingException(p, "Expecting JSON node to contain 'fees'");
        }

        final ArrayNode capabilities = Optional.ofNullable(node.get("capabilities"))
            .filter(ArrayNode.class::isInstance)
            .map(ArrayNode.class::cast)
            .orElseThrow(() -> new JsonMappingException(p, "Expecting 'capabilities' to be a JSON array"));

        return ImmutableAccountFee.builder()
            .tier(fees.get("tier").asText())
            .maker(new BigDecimal(fees.get("maker").asText()))
            .taker(new BigDecimal(fees.get("taker").asText()))
            .volume(new BigDecimal(fees.get("volume").asText()))
            .capabilities(capabilities.valueStream().map(JsonNode::asText).map(Capability::deserialize).collect(Collectors.toSet()))
            .build();
    }
}