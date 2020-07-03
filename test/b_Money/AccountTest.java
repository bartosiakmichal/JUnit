package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		//błąd w metodzie openAccount
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		//błąd w metodzie deposit
		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	@Test
	public void testAddRemoveTimedPayment() {

		testAccount.addTimedPayment("T01", 1, 1, new Money(10050, SEK), SweBank, "Alice");
		testAccount.addTimedPayment("T02", 2, 2, new Money(10033, SEK), SweBank, "Alice");
		assertTrue( testAccount.timedPaymentExists("T01") );
		assertTrue( testAccount.timedPaymentExists("T02") );

		testAccount.removeTimedPayment("T01");
		testAccount.removeTimedPayment("T02");
		assertFalse( testAccount.timedPaymentExists("T01") );
		assertFalse( testAccount.timedPaymentExists("T02") );

	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {


		testAccount.addTimedPayment("T01", 1, 1, new Money(10050, SEK), SweBank, "Alice");
		testAccount.tick();

		int amountAlice = SweBank.getBalance("Alice");
		int amountHans = testAccount.getBalance().getAmount();

		assertEquals(amountHans, testAccount.getBalance().getAmount().intValue() );
		assertEquals(amountAlice, SweBank.getBalance("Alice").intValue() );

		testAccount.tick();

		assertEquals(amountHans - 10050, testAccount.getBalance().getAmount().intValue() );
		assertEquals(amountAlice + 10050, SweBank.getBalance("Alice").intValue() );

		testAccount.tick();
		testAccount.tick();

		assertEquals(amountHans - 10050 - 10050, testAccount.getBalance().getAmount().intValue() );
		assertEquals(amountAlice + 10050 + 10050, SweBank.getBalance("Alice").intValue() );

		testAccount.removeTimedPayment("T01");

		testAccount.tick();

		assertEquals(amountHans - 10050 - 10050, testAccount.getBalance().getAmount().intValue() );
		assertEquals(amountAlice + 10050 + 10050, SweBank.getBalance("Alice").intValue() );

	}

	@Test
	public void testAddWithdraw() {
		int amountTestAccount = testAccount.getBalance().getAmount();

		testAccount.withdraw(new Money(500, SEK));
		assertEquals(amountTestAccount - 500, (int) testAccount.getBalance().getAmount() );
		testAccount.deposit(new Money(500, SEK));
		assertEquals(amountTestAccount, (int) testAccount.getBalance().getAmount() );
	}

	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(10000000, (int) testAccount.getBalance().getAmount());
		assertEquals(1000000, (int) SweBank.getBalance("Alice"));
	}
}
