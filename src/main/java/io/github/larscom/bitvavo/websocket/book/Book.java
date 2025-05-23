package io.github.larscom.bitvavo.websocket.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.message.MessageIn;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableBook.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Book extends MessageIn {
    /// The market for this book (e.g. "ETH-EUR").
    String getMarket();

    /// Integer which is increased by one for every update to the book. Useful for synchronizing.
    /// Resets to zero after restarting the matching engine.
    int getNonce();

    /// The bids in this book.
    List<BookPage> getBids();

    /// The asks in this book.
    List<BookPage> getAsks();
}
