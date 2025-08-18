package Logic;

import Logic.Interface.ITransactionsRepository;
import Repository.TransactionsRepository;

import java.util.List;

public class TransactionService {

    private ITransactionsRepository iTransactionsRepository;

    public TransactionService(ITransactionsRepository iTransactionsRepository) {
        this.iTransactionsRepository = iTransactionsRepository;
    }

    public int AddTransaction(TransactionData transactionData) {
        return iTransactionsRepository.addTransaction(transactionData);
    }

    public List<TransactionData> printTransactions(int id_account) {

        return iTransactionsRepository.printTransactions(id_account);
    }
}
