package ua.pti.myatm;

public class MyATM {

    public static void main(String[] args) throws NoPlasticCardException, NotEnoughCashInAccountException, NotEnoughCashInATMException {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = null;
        atm.validatePlasticCard(card, 1234);
        atm.checkBalanceInThePlasticCard();
        atm.getCash(999.99);        
    }
}
