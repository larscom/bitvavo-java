package io.github.larscom.bitvavo.http.client;

import io.github.larscom.bitvavo.http.account.*;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

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

    /// Returns the current balance for your account (all assets above zero)
    ///
    /// Rate limit weight points: 5
    Single<List<Balance>> getBalance();

    /// Returns the current balance for your account for the specified symbol.
    ///
    /// Rate limit weight points: 5
    Single<List<Balance>> getBalance(String symbol);

    /// Returns the current fees for your account.
    ///
    /// Rate limit weight points: 1
    Single<AccountFee> getAccountFee();

    /// Returns all past withdrawals for your account.
    ///
    /// Rate limit weight points: 5
    Single<List<WithdrawalHistory>> getWithdrawalHistory();
}
