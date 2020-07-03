package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 =  new Money(10000,  SEK);
		EUR10 =   new Money(1000,   EUR);
		SEK200 =  new Money(20000,  SEK);
		EUR20 =   new Money(2000,   EUR);
		SEK0 =    new Money(0,      SEK);
		EUR0 =    new Money(0,      EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
        String msg = "Test: getAmount";
        assertEquals(msg, new Integer(10000),  SEK100.getAmount());
        assertEquals(msg, new Integer(1000),   EUR10.getAmount());
        assertEquals(msg, new Integer(20000),  SEK200.getAmount());
        assertEquals(msg, new Integer(2000),   EUR20.getAmount());
        assertEquals(msg, new Integer(0),       SEK0.getAmount());
        assertEquals(msg, new Integer(0),       EUR0.getAmount());
        assertEquals(msg, new Integer(-10000), SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		String msg = "Test: getCurrency";
	    assertEquals(msg, SEK,SEK100.getCurrency());
	    assertEquals(msg, EUR,EUR10.getCurrency());
	    assertEquals(msg, SEK,SEK200.getCurrency());
	    assertEquals(msg, EUR,EUR20.getCurrency());
	    assertEquals(msg, SEK,SEK0.getCurrency());
	    assertEquals(msg, EUR,EUR0.getCurrency());
	    assertEquals(msg, SEK,SEKn100.getCurrency());
	}

	@Test
	public void testToString() {
		String msg = "Test: toString";
        assertEquals(msg, "100.00"  + " " + "SEK", SEK100.toString());
        assertEquals(msg, "10.00"   + " " + "EUR", EUR10.toString());
        assertEquals(msg, "200.00"  + " " + "SEK", SEK200.toString());
        assertEquals(msg, "20.00"   + " " + "EUR", EUR20.toString());
        assertEquals(msg, "0"       + " " + "SEK", SEK0.toString());
        assertEquals(msg, "0"       + " " + "EUR", EUR0.toString());
        assertEquals(msg, "-100.00" + " " + "SEK", SEKn100.toString());

	}

	@Test
	public void testGlobalValue() {
		String msg = "Test: universalValue";
        assertEquals(msg, new Integer((int) Math.round(10000  * 0.15)), SEK100.universalValue());
        assertEquals(msg, new Integer((int) Math.round(1000   * 1.5)),  EUR10.universalValue());
        assertEquals(msg, new Integer((int) Math.round(20000  * 0.15)), SEK200.universalValue());
        assertEquals(msg, new Integer((int) Math.round(2000   * 1.5)),  EUR20.universalValue());
        assertEquals(msg, new Integer((int) Math.round(0      * 0.15)), SEK0.universalValue());
        assertEquals(msg, new Integer((int) Math.round(0      * 1.5)),  EUR0.universalValue());
        assertEquals(msg, new Integer((int) Math.round(-10000 * 0.15)), SEKn100.universalValue());
    }

	@Test
	public void testEqualsMoney() {
		String msg = "Test: equalsMoney";
		assertTrue(msg,  EUR10.equals(SEK100));
        assertTrue(msg,  EUR20.equals(SEK200));
        assertFalse(msg, EUR0.equals(SEK100));
	}

	@Test
	public void testAdd() {
		String msg = "Test: addMoney";
		assertEquals(msg, (new Money(30000,  SEK)).getAmount(), SEK200.add(SEK100)  .getAmount());
		assertEquals(msg, (new Money(3000,   EUR)).getAmount(), EUR20.add(EUR10)    .getAmount());
		assertEquals(msg, (new Money(30000,  SEK)).getAmount(), SEK100.add(SEK200)  .getAmount());
		assertEquals(msg, (new Money(4000,   EUR)).getAmount(), EUR20.add(EUR20)    .getAmount());
		assertEquals(msg, (new Money(10000,  SEK)).getAmount(), SEK100.add(SEK0)    .getAmount());
		assertEquals(msg, (new Money(-20000, SEK)).getAmount(), SEKn100.add(SEKn100).getAmount());
		assertEquals(msg, (new Money(40000,  SEK)).getAmount(), SEK200.add(EUR20)   .getAmount());
	}

	@Test
	public void testSub() {
		String msg = "Test: subMoney";
		assertEquals(msg, (new Money(10000,  SEK)).getAmount(), SEK200 .sub(SEK100).getAmount());
		assertEquals(msg, (new Money(1000,   EUR)).getAmount(), EUR20  .sub(EUR10) .getAmount());
		assertEquals(msg, (new Money(-10000, SEK)).getAmount(), SEK100 .sub(SEK200).getAmount());
		assertEquals(msg, (new Money(0,      EUR)).getAmount(), EUR20  .sub(EUR20) .getAmount());
		assertEquals(msg, (new Money(0,      SEK)).getAmount(), SEK0   .sub(SEK0)  .getAmount());
		assertEquals(msg, (new Money(0,      EUR)).getAmount(), EUR0   .sub(EUR0)  .getAmount());
		assertEquals(msg, (new Money(0,      SEK)).getAmount(), SEK200 .sub(EUR20) .getAmount());
	}

	@Test
	public void testIsZero() {
		String msg = "Test: isZero";
		assertFalse(SEK100.isZero());
		assertFalse(EUR10.isZero());
		assertFalse(SEK200.isZero());
		assertFalse(EUR20.isZero());
		assertFalse(SEKn100.isZero());
		assertTrue(SEK0.isZero());
		assertTrue(EUR0.isZero());
	}

	@Test
	public void testNegate() {
		String msg = "Test: negate";
		assertEquals(msg, new Integer(-10000), SEK100.negate().getAmount());
		assertEquals(msg, new Integer(-1000), EUR10.negate().getAmount());
		assertEquals(msg, new Integer(-20000), SEK200.negate().getAmount());
		assertEquals(msg, new Integer(-2000), EUR20.negate().getAmount());
		assertEquals(msg, new Integer(0), SEK0.negate().getAmount());
		assertEquals(msg, new Integer(0), EUR0.negate().getAmount());
		assertEquals(msg, new Integer(10000), SEKn100.negate().getAmount());
		assertEquals(msg, SEK100.getAmount(), SEKn100.negate().getAmount());
		assertEquals(msg, SEKn100.getAmount(), SEK100.negate().getAmount());

	}

	@Test
	public void testCompareTo() {
		String msg = "Test: compareTo";
		assertEquals(-1, SEKn100.compareTo(EUR20));
		assertEquals(1, EUR20.compareTo(SEK100));
		assertEquals(0, SEK200.compareTo(EUR20));
	}
}
