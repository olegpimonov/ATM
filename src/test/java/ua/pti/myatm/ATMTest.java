/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * @author andrii
 */
public class ATMTest {
 // проверка не правильного аргумента
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCashATM()
    {
        ATM atm = new ATM(-1);
    }
// проверка указаной суммы в банкомате
    @Test
    public void testGetATMCash()
    {
        ATM atm = new ATM(1000.0);
        Assert.assertEquals(atm.getCashInATM(), 1000.0);
    }
// проверка карты
    @Test(expected = NullPointerException.class)
    public void testPlasticCardValidationNullPointerException()
    {
        ATM atm = new ATM(1000);
        atm.validatePlasticCard(null, 1234);
    }
// проверка блокировки карты
    @Test
    public void testPlasticCardValidationBlockedPlasticCard()
    {
        ATM atm = new ATM(1000);
        Card plasticCard = mock(Card.class);
        when(plasticCard.isBlocked()).thenReturn(true);
        boolean result = atm.validatePlasticCard(plasticCard, 1234);
        Assert.assertFalse(result);
    }
//проверка пароля и блокировки карты
    @Test
    public void testPlasticCardAcceptation()
    {
        ATM atm = new ATM(1000);
        int pin = 1234;
        Card plasticCard = mock(Card.class);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);

        boolean result = atm.validatePlasticCard(plasticCard, pin);
        Assert.assertTrue(result);
    }
//проверка баланса без карты
    @Test(expected = NoPlasticCardException.class)
    public void testCheckBalanceNoCard() throws NoPlasticCardException, BlockedCardExeption {
        ATM atm = new ATM(1000);
        atm.checkBalanceInThePlasticCard();
    }
//проверка баланса
    @Test
    public void testCheckBalanceInThePlasticCard() throws NoPlasticCardException, BlockedCardExeption {
        ATM atm = new ATM(1000);
        int pin = 1234;
        double balance = 1000.0;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(balance);

        Card plasticCard = mock(Card.class);
        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);
        atm.validatePlasticCard(plasticCard, pin);
        atm.checkBalanceInThePlasticCard();

        Assert.assertEquals(atm.checkBalanceInThePlasticCard(), 1000.0);
    }
//попытка снять деньги без карты
    @Test(expected = NoPlasticCardException.class)
    public void testGetCashNoPlasticCard() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException, BlockedCardExeption {
        ATM atm = new ATM(1000);
        Assert.assertNull(atm.getCash(1000));
    }
//
    @Test(expected = NotEnoughCashInAccountException.class)
    public void testGetCashNoEnoughMoney() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException, BlockedCardExeption {
        double amount = 1500;
        ATM atm = new ATM(100);
        double actual = 1000;
        int pin = 1234;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(actual);

        Card plasticCard = mock(Card.class);
        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);

        InOrder inOrder = inOrder(plasticCard, account);

        atm.validatePlasticCard(plasticCard, pin);
        atm.getCash(amount);

        inOrder.verify(plasticCard).isBlocked();
        inOrder.verify(plasticCard).checkPin(pin);
        inOrder.verify(plasticCard).getAccount();
        inOrder.verify(account).getBalanceInTheCard();
    }

    @Test(expected = NotEnoughCashInATMException.class)
    public void testGetCashNoEnoughCashInATM() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException, BlockedCardExeption {
        double amount = 1001;
        ATM atm = new ATM(100);
        int pin = 1234;
        double actual = 1005;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(actual);

        Card plasticCard = mock(Card.class);

        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);

        InOrder inOrder = inOrder(plasticCard, account);

        atm.validatePlasticCard(plasticCard, pin);
        atm.getCash(amount);

        inOrder.verify(plasticCard).isBlocked();
        inOrder.verify(plasticCard).checkPin(pin);
        inOrder.verify(plasticCard).getAccount();
        inOrder.verify(account).getBalanceInTheCard();
    }
    @Test
    public void testCheckBalanceOrderGetCash() throws NotEnoughCashInATMException, BlockedCardExeption, NotEnoughCashInAccountException, NoPlasticCardException {
        double amount = 1000;
        ATM atm = new ATM(10000);
        double actual = 10000;
        int pin = 1234;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(actual);

        Card plasticCard = mock(Card.class);
        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);


        atm.setPlasticCard(plasticCard);
        atm.getCash(amount);
        verify(account, times(1)).withdrow(amount);




    }
    @Test
    public void testGetCashBalanceOrderGetBalanceBeforeWithdraw() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException, BlockedCardExeption {
        double amount = 1000;
        ATM atm = new ATM(10000);
        double actual = 10000;
        int pin = 1234;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(actual);
        when(account.withdrow(amount)).thenReturn(amount);

        Card plasticCard = mock(Card.class);
        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);
        atm.validatePlasticCard(plasticCard, pin);
        atm.getCash(amount);


        InOrder order = inOrder(account);
        order.verify(account).getBalanceInTheCard();
        order.verify(account).withdrow(amount);
    }

    @Test
    public void testGetCash() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException, BlockedCardExeption {
        double amount = 1000;
        ATM atm = new ATM(1000);
        int pin = 1234;
        double actual = 1000;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(actual);

        Card plasticCard = mock(Card.class);
        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);

        atm.validatePlasticCard(plasticCard, pin);

    }
/*
    @Test(expected = IllegalArgumentException.class)
    public void testZeroCashATM()
    {
        ATM atm = new ATM(0);
    }

    @Test(expected = BlockedCardExeption.class)
    public  void  testBlockedCardExeption() throws BlockedCardExeption, NoPlasticCardException {
        Card plasticCard = mock(Card.class);
        when(plasticCard.checkPin(1234)).thenReturn(true);
        when(plasticCard.isBlocked()).thenReturn(true);

        ATM atm = new ATM(1000);
        atm.setPlasticCard(plasticCard);
        atm.isInserted();    }
*/
    @Test
    public void testCheckBalanceInTheBlockedPlasticCard() throws BlockedCardExeption, NoPlasticCardException {
        Card plasticCard = mock(Card.class);
        when(plasticCard.checkPin(1234)).thenReturn(true);

        Account account = mock(Account.class);
        when(plasticCard.getAccount()).thenReturn(account);

        ATM atm = new ATM(1000);
        atm.validatePlasticCard(plasticCard, 1234);
        atm.checkBalanceInThePlasticCard();
        verify(plasticCard, times(1)).isBlocked();

    }

    @Test
    public void test1() throws BlockedCardExeption, NoPlasticCardException {
        double amount = 1000;
        ATM atm = new ATM(10000);
        double actual = 10000;
        int pin = 1234;

        Account account = mock(Account.class);
        when(account.getBalanceInTheCard()).thenReturn(actual);

        Card plasticCard = mock(Card.class);
        when(plasticCard.getAccount()).thenReturn(account);
        when(plasticCard.isBlocked()).thenReturn(false);
        when(plasticCard.checkPin(pin)).thenReturn(true);


        atm.setPlasticCard(plasticCard);
        atm.addCash(amount);
        verify(account, times(1)).add(amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() throws BlockedCardExeption, NoPlasticCardException {
        double amount = -1;
        ATM atm = new ATM(1000);
        atm.addCash(amount);

    }

    @Test(expected = IllegalArgumentException.class)
    public void test3() throws BlockedCardExeption, NoPlasticCardException {
        double amount = 0;
        ATM atm = new ATM(1000);
        atm.addCash(amount);

    }
}





