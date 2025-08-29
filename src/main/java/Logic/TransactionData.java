package Logic;
import java.time.LocalDateTime;


public  final class TransactionData {
    private int amount;
    private final LocalDateTime date ;

    public TransactionData(int amount, LocalDateTime date) {
        this.amount = amount;
        this.date = date;
    }


    void setAmount (int amount){
        this.amount=amount;
    }
   public int getAmount(){
        return amount;
    }

    public  LocalDateTime getDate(){
        return date;
    }

    void print(int amount, LocalDateTime date) {
        System.out.println("amount=" + amount + "  date=" + date);
    }

}

