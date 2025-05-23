# Bitvavo Java

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.larscom/bitvavo-java)
[![workflow](https://github.com/larscom/bitvavo-java/actions/workflows/workflow.yml/badge.svg)](https://github.com/larscom/bitvavo-java/actions/workflows/workflow.yml)
[![License MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Java library to interact with the Bitvavo platform using the Bitvavo v2 API (see: https://docs.bitvavo.com)

> [!NOTE]
> This library is in development and breaking changes may be made up until a 1.0 release.

## ✨ Features

- **Reactive WebSocket Client**: Listen to all events occurring on the Bitvavo platform (tickers, tickers24h, candles,
  books, trades, orders, fills)
    - **Automatic Reconnect**: The `ReactiveWebSocketClient` keeps you connected at all times.
    - **Read Only**: The `ReactiveWebSocketClient` is `read-only`, meaning you can only receive data via the websocket.
    - **Reactive**: The `ReactiveWebSocketClient` is `reactive` and `non-blocking`, it
      uses [RxJava](https://github.com/ReactiveX/RxJava) under the hood.

- **HTTP Client** (Coming soon):  You can do things like placing orders or withdraw assets from your account.
    - **Read / Write**: The HTTP client allows you to receive data and send data.

## 📦 Installation

> **Note**: Minimum version of `Java 21` is required.

Add `bitvavo-java` to your `pom.xml`:

```xml

<dependency>
    <groupId>io.github.larscom</groupId>
    <artifactId>bitvavo-java</artifactId>
    <version>0.0.7</version>
</dependency>
```

## 🔧 Usage

Here's a quick example to get you started:

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.websocket.Channel;
import io.github.larscom.bitvavo.websocket.ChannelName;
import io.github.larscom.bitvavo.websocket.client.ReactiveWebSocketClient;

import java.util.Set;

class Main {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final ReactiveWebSocketClient client = new ReactiveWebSocketClient();

        final Channel channel = Channel.builder()
            .name(ChannelName.TICKER)
            .markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR"))
            .build();

        // you can call this function mutliple times to subscribe to more markets at a later moment.
        client.subscribe(Set.of(channel));

        // receive errors, mostly for debug purposes
        client.errors().subscribe(System.out::println);

        // receive data
        client.tickers().subscribe(System.out::println);

        // keep this thread alive
        Thread.currentThread().join();
    }
}
```

## 📚 Examples

### Authentication

Subscribing to `orders` and `fills` requires authentication (`api key` / `api secret`) which you can setup
in [Bitvavo](https://account.bitvavo.com/user/api)

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.websocket.Channel;
import io.github.larscom.bitvavo.websocket.ChannelName;
import io.github.larscom.bitvavo.websocket.account.Credentials;
import io.github.larscom.bitvavo.websocket.client.ReactiveWebSocketClient;

import java.util.Set;

class Main {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final Credentials credentials = new Credentials("MY_API_KEY", "MY_API_SECRET");

        // pass the credentials
        final ReactiveWebSocketClient client = new ReactiveWebSocketClient(credentials);

        final Channel channel = Channel.builder()
            .name(ChannelName.ACCOUNT)
            .markets(Set.of("ETH-EUR", "BTC-EUR"))
            .build();

        client.subscribe(Set.of(channel));

        // receive errors, mostly for debug purposes
        client.errors().subscribe(System.out::println);

        // receive data
        client.orders().subscribe(System.out::println);

        Thread.currentThread().join();
    }
}
```

### Proxy

If you need a proxy you can simply pass a `java.net.Proxy` object to the `ReactiveWebSocketClient` constructor.

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.websocket.Channel;
import io.github.larscom.bitvavo.websocket.ChannelName;
import io.github.larscom.bitvavo.websocket.client.ReactiveWebSocketClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Set;

class Main {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.example.com", 8080));

        // pass the proxy
        final ReactiveWebSocketClient client = new ReactiveWebSocketClient(proxy);

        final Channel channel = Channel.builder()
            .name(ChannelName.TICKER)
            .markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR"))
            .build();

        client.subscribe(Set.of(channel));

        // receive errors, mostly for debug purposes
        client.errors().subscribe(System.out::println);

        // receive data
        client.tickers().subscribe(System.out::println);

        // keep this thread alive
        Thread.currentThread().join();
    }
}
```

### Single Stream

If you want to handle multiple events in a single stream you can use `instanceof`

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.websocket.Channel;
import io.github.larscom.bitvavo.websocket.ChannelName;
import io.github.larscom.bitvavo.websocket.book.Book;
import io.github.larscom.bitvavo.websocket.client.ReactiveWebSocketClient;
import io.github.larscom.bitvavo.websocket.ticker.Ticker;

import java.util.Set;

class Main {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final ReactiveWebSocketClient client = new ReactiveWebSocketClient();

        final Set<Channel> channels = Set.of(
            Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR")).build(),
            Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR")).build()
        );

        client.subscribe(channels);

        // single stream to handle all message types.
        client.messages().subscribe(messageIn -> {
            if (messageIn instanceof final Ticker ticker) {
                System.out.println("Ticker: " + ticker);
            }

            if (messageIn instanceof final Book book) {
                System.out.println("Book: " + book);
            }
        });

        // keep this thread alive
        Thread.currentThread().join();
    }
}
```

### Running The Example

There is an `Example.java` file in this repository which you can run using the following command:

```sh
mvn exec:java
```

The example subscribes to all available channels (tickers, tickers24h, candles,
books, trades, orders, fills)

## License

Copyright (c) 2025 Lars Kniep

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---
