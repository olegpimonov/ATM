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
 //
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCashATM()
    {
        ATM atm = new ATM(-1);
    }

    @Test
    public void testGetATMCash()
    {
        ATM atm = new ATM(1000.0);
        Assert.assertEquals(atm.getCashInATM(), 1000.0);
    }

    @Test(expected = NullPointerException.class)
    public void testPlasticCardValidationNullPointerException()
    {
        ATM atm = new ATM(1000);
        atm.validatePlasticCard(null, 1234);
    }

    @Test
    public void testPlasticCardValidationBlockedPlasticCard()
    {
        ATM atm = new ATM(1000);
        Card plasticCard = mock(Card.class);
        when(plasticCard.isBlocked()).thenReturn(true);
        boolean result = atm.validatePlasticCard(plasticCard, 1234);
        Assert.assertFalse(result);
    }

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

    @Test(expected = NoPlasticCardException.class)
    public void testCheckBalanceNoCard() throws NoPlasticCardException
    {
        ATM atm = new ATM(1000);
        atm.checkBalanceInThePlasticCard();
    }

    @Test
    public void testCheckBalanceInThePlasticCard() throws NoPlasticCardException
    {
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

    @Test(expected = NoPlasticCardException.class)
    public void testGetCashNoPlasticCard() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException
    {
        ATM atm = new ATM(1000);
        Assert.assertNull(atm.getCash(1000));
    }

    @Test(expected = NotEnoughCashInAccountException.class)
    public void testGetCashNoEnoughMoney() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException
    {
        double amount = 1001;
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
    public void testGetCashNoEnoughCashInATM() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException
    {
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
    public void testGetCashBalanceOrderGetBalanceBeforeWithdraw() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException
    {
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
    public void testGetCash() throws NotEnoughCashInAccountException, NoPlasticCardException, NotEnoughCashInATMException
    {
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
        atm.getCash(amount);
    }
}
