package ua.pti.myatm;

public class ATM {
    private double cash;
    private Card plasticCard;
    private double amount;

    //Можно задавать количество денег в банкомате 
    ATM(double cashInATM)
    {
        if (cashInATM <= 0)
        {
            throw new IllegalArgumentException();
        }
        this.plasticCard = null;
        this.cash = cashInATM;
    }

    // Возвращает каоличестов денег в банкомате
    public double getCashInATM() {
        return cash;
    }

    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false.
    // При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validatePlasticCard(Card plasticCard, int pinCode)
    {
        if (plasticCard == null)
        {
            throw new NullPointerException();
        }
        else if (!plasticCard.checkPin(pinCode) || plasticCard.isBlocked())
            {
                return false;
            }
            else
            {
                this.plasticCard = plasticCard;
                return true;
            }
    }

    //Возвращает сколько денег есть на счету
    public double checkBalanceInThePlasticCard() throws NoPlasticCardException, BlockedCardExeption {
        isInserted();
        return plasticCard.getAccount().getBalanceInTheCard();
    }

    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount 
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM 
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount) throws NoPlasticCardException, NotEnoughCashInAccountException, NotEnoughCashInATMException, BlockedCardExeption {
        isInserted();
        Account account = this.plasticCard.getAccount();
        if (account.getBalanceInTheCard() < amount)
        {
            throw new NotEnoughCashInAccountException();
        }
        else if (this.getCashInATM() < amount)
            {
                throw new NotEnoughCashInATMException();
            } else
            {
                this.cash -= account.withdrow(amount);
                return account.getBalanceInTheCard();
            }
    }

    public void isInserted() throws NoPlasticCardException, BlockedCardExeption {
        if (this.plasticCard == null)
        {
            throw new NoPlasticCardException();
            }
    }

    public void setPlasticCard(Card plasticCard) {
        this.plasticCard = plasticCard;
    }

    public double addCash(double amount) throws BlockedCardExeption, NoPlasticCardException {
        if (amount <= 0)
        {
            throw new IllegalArgumentException();
        }
        isInserted();
        Account account = this.plasticCard.getAccount();
        this.cash += account.add(amount);
        return account.getBalanceInTheCard();
    }
}
