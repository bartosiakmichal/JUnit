package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;

	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		String msg = "Test: getName";
		assertEquals(msg, "SEK", SEK.getName());
		assertEquals(msg, "DKK", DKK.getName());
		assertEquals(msg, "EUR", EUR.getName());
	}

	@Test
	public void testGetRate() {
		String msg = "Test: getRate";
		assertEquals(msg, new Double(0.15), SEK.getRate());
		assertEquals(msg, new Double(0.20), DKK.getRate());
		assertEquals(msg, new Double(1.50), EUR.getRate());
	}

	@Test
	public void testSetRate() {
		String msg = "Test: setRate";

		SEK.setRate(0.17);
		DKK.setRate(0.22);
		EUR.setRate(1.52);
		assertEquals(msg, new Double(0.17), SEK.getRate());
		assertEquals(msg, new Double(0.22), DKK.getRate());
		assertEquals(msg, new Double(1.52), EUR.getRate());
		SEK.setRate(0.15);
		DKK.setRate(0.20);
		EUR.setRate(1.50);

	}

	@Test
	public void testGlobalValue() {
		String msg = "Test: universalValue";
		assertEquals(msg, new Integer(1500), SEK.universalValue(10000));
		assertEquals(msg, new Integer(2000), DKK.universalValue(10000));
		assertEquals(msg, new Integer(15000),EUR.universalValue(10000));
		assertEquals(msg, new Integer(756),  SEK.universalValue(5043));
		assertEquals(msg, new Integer(757),  SEK.universalValue(5045));

	}

	@Test
	public void testValueInThisCurrency() {
		String msg = "Test: valueInThisCurrency";
		assertEquals(msg, new Integer(1050), SEK.valueInThisCurrency(105, EUR));
		assertEquals(msg, new Integer(79),   DKK.valueInThisCurrency(105, SEK));
		assertEquals(msg, new Integer(14),   EUR.valueInThisCurrency(105, DKK));
	}

}
