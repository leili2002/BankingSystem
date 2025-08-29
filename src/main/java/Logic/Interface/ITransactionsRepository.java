package Logic.Interface;

import Logic.AccountData;
import Logic.TransactionData;

import java.util.List;

public interface ITransactionsRepository {
    int addTransaction(TransactionData transactionData);

    List<TransactionData> printTransactions(int id_account);
}
