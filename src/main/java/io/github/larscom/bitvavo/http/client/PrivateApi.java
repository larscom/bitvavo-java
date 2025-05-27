package io.github.larscom.bitvavo.http.client;

import io.github.larscom.bitvavo.http.account.TransactionHistory;
import io.github.larscom.bitvavo.http.account.TransactionHistoryParams;
import io.reactivex.rxjava3.core.Single;

public interface PrivateApi extends PublicApi {
    /// Returns all past transactions for your account.
    ///
    /// Rate limit weight points: 1
    Single<TransactionHistory> getTransactionHistory();

    /// Returns all past transactions for your account.
    ///
    /// Rate limit weight points: 1
    Single<TransactionHistory> getTransactionHistory(TransactionHistoryParams transactionHistoryParams);
}
