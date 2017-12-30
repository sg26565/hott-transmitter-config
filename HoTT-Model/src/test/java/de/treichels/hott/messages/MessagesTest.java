package de.treichels.hott.messages;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessagesTest {
    @Test
    public void testDotName() {
        assertEquals(Messages.getString(".Invalid"), "!.Invalid!");
    }

    @Test
    public void testMissing() {
        assertEquals(Messages.getString("xxx"), "!xxx!");
    }

    @Test
    public void testNameDot() {
        assertEquals(Messages.getString("Invalid."), "!Invalid.!");
    }

    @Test
    public void testNull() {
        assertNull(Messages.getString(null));
    }

    @Test
    public void testSimple() {
        assertEquals(Messages.getString("Ok"), "Ok");
    }
}
