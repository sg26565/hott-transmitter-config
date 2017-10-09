package gde.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import gde.mdl.messages.Messages;

public class MessagesTest {
    @Test
    public void testDotName() {
        assertEquals(Messages.getString(".Invalid"), "!.Invalid!");
    }

    @Test
    public void testEnums() {
        assertEquals(Messages.getString("AileronFlapType.OneAil"), "1QR");
    }

    @Test
    public void testFullyQualified() {
        assertEquals(Messages.getString("gde.model.enums.AileronFlapType.OneAil"), "1QR");
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
