package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
    Currency SEK, DKK;
    Bank SweBank, Nordea, DanskeBank;

    @Before
    public void setUp() throws Exception {
        DKK = new Currency("DKK", 0.20);
        SEK = new Currency("SEK", 0.15);
        SweBank = new Bank("SweBank", SEK);
        Nordea = new Bank("Nordea", SEK);
        DanskeBank = new Bank("DanskeBank", DKK);
        SweBank.openAccount("Ulrika");
        SweBank.openAccount("Bob");
        Nordea.openAccount("Bob");
        DanskeBank.openAccount("Gertrud");
    }

    @Test
    public void testGetName() {
        assertEquals("Nordea", Nordea.getName());
    }

    @Test
    public void testGetCurrency() {
        assertSame(SEK, SweBank.getCurrency());
        assertSame(SEK, Nordea.getCurrency());
        assertSame(DKK, DanskeBank.getCurrency());
        assertNotSame(SEK, DanskeBank.getCurrency());
    }

    @Test
    public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
        SweBank.openAccount("testAccount");
        SweBank.deposit("testAccount", new Money(100, SEK));
    }

    @Test
    public void testDeposit() throws AccountDoesNotExistException {
        Nordea.deposit("Bob", new Money(100, SEK));
        SweBank.deposit("Ulrika", new Money(200, SEK));
        assertEquals(100, (int) Nordea.getBalance("Bob"));
        assertEquals(200, (int) SweBank.getBalance("Ulrika"));

        Money moneyDKK = new Money(200, DKK);
        Money moneySEK = new Money(100, SEK);

        DanskeBank.deposit("Gertrud", moneyDKK);
        DanskeBank.deposit("Gertrud", moneySEK);
        assertEquals((int) moneyDKK.add(moneySEK).getAmount(), (int) DanskeBank.getBalance("Gertrud"));
    }

    @Test
    public void testWithdraw() throws AccountDoesNotExistException {
        Nordea.deposit("Bob", new Money(400, SEK));
        Nordea.withdraw("Bob", new Money(100, SEK));
        assertEquals(300, (int) Nordea.getBalance("Bob"));

        Money moneyDKK = new Money(200, DKK);
        Money moneySEK = new Money(100, SEK);

        DanskeBank.deposit("Gertrud", moneyDKK);
        DanskeBank.withdraw("Gertrud", moneySEK);

        assertEquals((int) moneyDKK.sub(moneySEK).getAmount(), (int) DanskeBank.getBalance("Gertrud"));
    }

    @Test
    public void testGetBalance() throws AccountDoesNotExistException {
        SweBank.deposit("Ulrika", new Money(100, SEK));
        SweBank.deposit("Bob", new Money(100, SEK));
        Nordea.deposit("Bob", new Money(100, SEK));
        DanskeBank.deposit("Gertrud", new Money(100, DKK));

        assertEquals(100, (int) SweBank.getBalance("Ulrika"));
        assertEquals(100, (int) SweBank.getBalance("Bob"));
        assertEquals(100, (int) Nordea.getBalance("Bob"));
        assertEquals(100, (int) DanskeBank.getBalance("Gertrud"));
    }

    @Test
    public void testTransfer() throws AccountDoesNotExistException {
        SweBank.deposit("Ulrika", new Money(500, SEK));
        SweBank.deposit("Bob", new Money(100, SEK));

        int amountUlrika = SweBank.getBalance("Ulrika");
        int amountBob = SweBank.getBalance("Bob");

        SweBank.transfer("Ulrika", "Bob", new Money(150, SEK));

        assertEquals(amountUlrika - 150, (int) SweBank.getBalance("Ulrika"));
        assertEquals(amountBob + 150, (int) SweBank.getBalance("Bob"));

    }

    @Test
    public void testTimedPayment() throws AccountDoesNotExistException {
        SweBank.addTimedPayment("Ulrika", "T01", 1, 1, new Money(150, SEK), Nordea, "Bob");
        SweBank.tick();

        SweBank.deposit("Ulrika", new Money(500, SEK));
        Nordea.deposit("Bob", new Money(100, SEK));

        int amountUlrikaSweBank = SweBank.getBalance("Ulrika");
        int amountBobNordea = Nordea.getBalance("Bob");

        assertEquals(amountUlrikaSweBank, (int) SweBank.getBalance("Ulrika"));
        assertEquals(amountBobNordea, (int) Nordea.getBalance("Bob"));

        SweBank.tick();

        assertEquals(amountUlrikaSweBank - 150, (int) SweBank.getBalance("Ulrika"));
        assertEquals(amountBobNordea + 150, (int) Nordea.getBalance("Bob"));

        SweBank.tick();
        SweBank.tick();

        assertEquals(amountUlrikaSweBank - 150 - 150, (int) SweBank.getBalance("Ulrika"));
        assertEquals(amountBobNordea + 150 + 150, (int) Nordea.getBalance("Bob"));

        SweBank.removeTimedPayment("Ulrika", "T01");

        SweBank.tick();

        assertEquals(amountUlrikaSweBank - 150 - 150, (int) SweBank.getBalance("Ulrika"));
        assertEquals(amountBobNordea + 150 + 150, (int) Nordea.getBalance("Bob"));
    }
}
