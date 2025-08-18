package Logic;
import java.time.LocalDateTime;


public  final class Transaction {
    private int amount;
    private final LocalDateTime date ;

    public Transaction(int amountt, LocalDateTime date) {
        this.amount = amountt;
        this.date = date;
    }


    void setAmount (int amount){
        this.amount=amount;
    }
   public int getAmount(){
        return amount;
    }

    public  LocalDateTime getdate(){
        return date;
    }

    void print(int amount, LocalDateTime date) {
        System.out.println("amount=" + amount + "  date=" + date);
    }

}

