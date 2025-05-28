package io.github.larscom.bitvavo.websocket.client;

import io.github.larscom.bitvavo.websocket.account.Fill;
import io.github.larscom.bitvavo.websocket.account.Order;
import io.reactivex.rxjava3.core.Flowable;

public interface PrivateApi extends PublicApi {
    Flowable<Order> orders();

    Flowable<Fill> fills();
}
