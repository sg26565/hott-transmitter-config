package de.treichels.hott.model.voice;

/**
 * A rolling buffer with a fixed capacity.
 *
 * As new elements are added to the buffer, previous elements will be removed if the capacity is reached.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class Buffer {
    /** all elements in this buffer */
    private final int[] elements;

    /** the pointer to the next index where new values are inserted */
    private int pointer = 0;

    /**
     * @param capacity
     *            The maximum number of elements that this Buffer can hold.
     */
    public Buffer(final int capacity) {
        elements = new int[capacity];
    }

    /**
     * Add a new element to the buffer. If the capacity is reached, older values will be automatically removed from the buffer.
     *
     * @param nextElement
     *            The new element.
     */
    public void add(final int nextElement) {
        elements[pointer] = nextElement;
        pointer++;
        if (pointer == elements.length) pointer = 0;
    }

    /**
     * Get the element at the specified position. Where <code>get(0)</code> will return the oldest (first added) element and <code>get(capacity -1)</code> will
     * return the newest (least added) element.
     *
     * @param index
     *            The position were <code>0 <= position < capacity</code>.
     * @return The element at the specified position.
     * @throws IndexOutOfBoundsException
     *             is index is negative or greater or equal to the capacity.
     */
    public int get(final int index) {
        int i = index + pointer;
        if (i >= elements.length) i -= elements.length;
        return elements[i];
    }

    /**
     * Get the current capacity of the Buffer.
     *
     * @return The capacity
     */
    public int getSize() {
        return elements.length;
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();

        b.append("Buffer[");

        for (int i = 0; i < elements.length; i++) {
            if (i > 0) b.append(", ");
            b.append(get(i));
        }

        b.append("]");

        return b.toString();
    }
}
