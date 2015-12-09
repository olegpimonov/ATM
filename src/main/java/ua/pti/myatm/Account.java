package ua.pti.myatm;

public interface Account {

    //Возвращает баланс на счете
    public double getBalanceInTheCard();

    //Снимает указанную сумму со счета
    //Возвращает сумму, которая была снята
    public double withdrow(double amount);
    
}
