package io.github.larscom.bitvavo.http.client;

import io.github.larscom.bitvavo.http.account.MarketFee;
import io.github.larscom.bitvavo.http.account.MarketFeeParams;
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

    /// Returns your current fees for trading in a specific market.
    ///
    /// If no parameters provided, returns the fees for your current tier in [Category B](https://bitvavo.com/en/fees)
    ///
    /// Rate limit weight points: 1
    Single<MarketFee> getMarketFee();

    /// Returns your current fees for trading in a specific market.
    ///
    /// Rate limit weight points: 1
    Single<MarketFee> getMarketFee(MarketFeeParams marketFeeParams);
}
