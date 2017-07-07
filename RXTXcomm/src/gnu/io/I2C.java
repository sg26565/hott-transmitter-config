/*-------------------------------------------------------------------------
|   RXTX License v 2.1 - LGPL v 2.1 + Linking Over Controlled Interface.
|   RXTX is a native interface to serial ports in java.
|   Copyright 1997-2007 by Trent Jarvi tjarvi@qbang.org and others who
|   actually wrote it.  See individual source files for more information.
|
|   A copy of the LGPL v 2.1 may be found at
|   http://www.gnu.org/licenses/lgpl.txt on March 4th 2007.  A copy is
|   here for your convenience.
|
|   This library is free software; you can redistribute it and/or
|   modify it under the terms of the GNU Lesser General Public
|   License as published by the Free Software Foundation; either
|   version 2.1 of the License, or (at your option) any later version.
|
|   This library is distributed in the hope that it will be useful,
|   but WITHOUT ANY WARRANTY; without even the implied warranty of
|   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
|   Lesser General Public License for more details.
|
|   An executable that contains no derivative of any portion of RXTX, but
|   is designed to work with RXTX by being dynamically linked with it,
|   is considered a "work that uses the Library" subject to the terms and
|   conditions of the GNU Lesser General Public License.
|
|   The following has been added to the RXTX License to remove
|   any confusion about linking to RXTX.   We want to allow in part what
|   section 5, paragraph 2 of the LGPL does not permit in the special
|   case of linking over a controlled interface.  The intent is to add a
|   Java Specification Request or standards body defined interface in the
|   future as another exception but one is not currently available.
|
|   http://www.fsf.org/licenses/gpl-faq.html#LinkingOverControlledInterface
|
|   As a special exception, the copyright holders of RXTX give you
|   permission to link RXTX with independent modules that communicate with
|   RXTX solely through the Sun Microsytems CommAPI interface version 2,
|   regardless of the license terms of these independent modules, and to copy
|   and distribute the resulting combined work under terms of your choice,
|   provided that every copy of the combined work is accompanied by a complete
|   copy of the source code of RXTX (the version of RXTX used to produce the
|   combined work), being distributed under the terms of the GNU Lesser General
|   Public License plus this exception.  An independent module is a
|   module which is not derived from or based on RXTX.
|
|   Note that people who make modified versions of RXTX are not obligated
|   to grant this special exception for their modified versions; it is
|   their choice whether to do so.  The GNU Lesser General Public License
|   gives permission to release a modified version without this exception; this
|   exception also makes it possible to release a modified version which
|   carries forward this exception.
|
|   You should have received a copy of the GNU Lesser General Public
|   License along with this library; if not, write to the Free
|   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
|   All trademarks belong to their respective owners.
--------------------------------------------------------------------------*/
package gnu.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * @author Trent Jarvi @version,
 * @since JDK1.0
 */

/**
 * I2C
 */
final class I2C extends I2CPort {

    /** Inner class for I2CInputStream */
    class I2CInputStream extends InputStream {
        @Override
        public int available() throws IOException {
            return nativeavailable();
        }

        @Override
        public int read() throws IOException {
            dataAvailable = 0;
            return readByte();
        }

        @Override
        public int read(final byte b[]) throws IOException {
            return read(b, 0, b.length);
        }

        @Override
        public int read(final byte b[], final int off, final int len) throws IOException {
            dataAvailable = 0;
            int i = 0, Minimum = 0;
            final int intArray[] = { b.length, InputBuffer, len };
            /*
             * find the lowest nonzero value timeout and threshold are handled on the native side see NativeEnableReceiveTimeoutThreshold in I2CImp.c
             */
            while (intArray[i] == 0 && i < intArray.length)
                i++;
            Minimum = intArray[i];
            while (i < intArray.length) {
                if (intArray[i] > 0) Minimum = Math.min(Minimum, intArray[i]);
                i++;
            }
            Minimum = Math.min(Minimum, threshold);
            if (Minimum == 0) Minimum = 1;
            available();
            final int Ret = readArray(b, off, Minimum);
            return Ret;
        }
    }

    /** Inner class for I2COutputStream */
    class I2COutputStream extends OutputStream {
        @Override
        public void flush() throws IOException {
            drain();
        }

        @Override
        public void write(final byte b[]) throws IOException {
            writeArray(b, 0, b.length);
        }

        @Override
        public void write(final byte b[], final int off, final int len) throws IOException {
            writeArray(b, off, len);
        }

        @Override
        public void write(final int b) throws IOException {
            writeByte(b);
        }
    }

    /** Actual I2CPort wrapper class */

    class MonitorThread extends Thread {
        /**
         * Note: these have to be separate boolean flags because the I2CPortEvent constants are NOT bit-flags, they are just defined as integers from 1 to 10
         * -DPL
         */
        private boolean CTS = false;
        private boolean DSR = false;
        private boolean RI = false;
        private boolean CD = false;
        private boolean OE = false;
        private boolean PE = false;
        private boolean FE = false;
        private boolean BI = false;
        private boolean Data = false;
        private boolean Output = false;

        MonitorThread() {}

        @Override
        public void run() {
            eventLoop();
        }
    }

    static {
        System.loadLibrary("rxtxI2C");
        Initialize();
    }

    /** DSR flag **/
    static boolean dsrFlag = false;

    /** Initialize the native library */
    private native static void Initialize();

    /** File descriptor */
    private int fd;
    /** Output stream */
    private final I2COutputStream out = new I2COutputStream();

    /** Input stream */
    private final I2CInputStream in = new I2CInputStream();
    /** Line speed in bits-per-second */
    private int speed = 9600;

    /** Data bits port parameter */
    private int dataBits = DATABITS_8;

    /** Stop bits port parameter */
    private int stopBits = I2CPort.STOPBITS_1;

    /** Parity port parameter */
    private int parity = I2CPort.PARITY_NONE;
    /** Flow control */
    private int flowmode = I2CPort.FLOWCONTROL_NONE;

    /** Receive timeout control */
    private int timeout = 0;
    /** Receive threshold control */

    private int threshold = 0;

    /** Input/output buffers */
    /**
     * FIXME I think this refers to FOPEN(3)/SETBUF(3)/FREAD(3)/FCLOSE(3) taj@www.linux.org.uk
     * 
     * These are native stubs...
     */
    private int InputBuffer = 0;
    private int OutputBuffer = 0;

    /** I2C Port Event listener */
    private I2CPortEventListener SPEventListener;
    /** Thread to monitor data */
    private MonitorThread monThread;

    private int dataAvailable = 0;

    /** Open the named port */
    public I2C(final String name) throws PortInUseException {
        fd = open(name);
    }

    /** Add an event listener */
    @Override
    public void addEventListener(final I2CPortEventListener lsnr) throws TooManyListenersException {
        if (SPEventListener != null) throw new TooManyListenersException();
        SPEventListener = lsnr;
        monThread = new MonitorThread();
        monThread.start();
    }

    @Override
    public void close() {
        setDTR(false);
        setDSR(false);
        nativeClose();
        super.close();
        fd = 0;
    }

    @Override
    public void disableReceiveFraming() {}

    @Override
    public void disableReceiveThreshold() {
        enableReceiveThreshold(0);
    }

    @Override
    public void disableReceiveTimeout() {
        enableReceiveTimeout(0);
    }

    private native void drain() throws IOException;

    /*
     * linux/drivers/char/n_hdlc.c? FIXME taj@www.linux.org.uk
     */
    /**
     * Receive framing control
     */
    @Override
    public void enableReceiveFraming(final int f) throws UnsupportedCommOperationException {
        throw new UnsupportedCommOperationException("Not supported");
    }

    @Override
    public void enableReceiveThreshold(final int thresh) {
        if (thresh >= 0) {
            threshold = thresh;
            NativeEnableReceiveTimeoutThreshold(timeout, threshold, InputBuffer);
        } else
            System.out.println("Invalid Threshold");
    }

    @Override
    public void enableReceiveTimeout(final int time) {
        if (time >= 0) {
            timeout = time;
            NativeEnableReceiveTimeoutThreshold(time, threshold, InputBuffer);
        } else
            System.out.println("Invalid timeout");
    }

    /** Process I2CPortEvents */
    native void eventLoop();

    /** Finalize the port */
    @Override
    protected void finalize() {
        if (fd > 0) close();
    }

    @Override
    public int getBaudRate() {
        return speed;
    }

    @Override
    public int getDataBits() {
        return dataBits;
    }

    @Override
    public int getFlowControlMode() {
        return flowmode;
    }

    @Override
    public int getInputBufferSize() {
        return InputBuffer;
    }

    @Override
    public InputStream getInputStream() {
        return in;
    }

    @Override
    public int getOutputBufferSize() {
        return OutputBuffer;
    }

    @Override
    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public int getParity() {
        return parity;
    }

    @Override
    public int getReceiveFramingByte() {
        return 0;
    }

    @Override
    public int getReceiveThreshold() {
        return threshold;
    }

    @Override
    public int getReceiveTimeout() {
        return NativegetReceiveTimeout();
    }

    @Override
    public int getStopBits() {
        return stopBits;
    }

    @Override
    public native boolean isCD();

    @Override
    public native boolean isCTS();

    @Override
    public native boolean isDSR();

    /** Line status methods */
    @Override
    public native boolean isDTR();

    @Override
    public boolean isReceiveFramingEnabled() {
        return false;
    }

    @Override
    public boolean isReceiveThresholdEnabled() {
        return threshold > 0;
    }

    @Override
    public boolean isReceiveTimeoutEnabled() {
        return NativeisReceiveTimeoutEnabled();
    }

    @Override
    public native boolean isRI();

    @Override
    public native boolean isRTS();

    /** I2C read methods */
    private native int nativeavailable() throws IOException;

    /** Close the port */
    private native void nativeClose();

    public native void NativeEnableReceiveTimeoutThreshold(int time, int threshold, int InputBuffer);

    public native int NativegetReceiveTimeout();

    public native boolean NativeisReceiveTimeoutEnabled();

    /** Set the native I2C port parameters */
    private native void nativeSetI2CPortParams(int speed, int dataBits, int stopBits, int parity) throws UnsupportedCommOperationException;

    @Override
    public void notifyOnBreakInterrupt(final boolean enable) {
        monThread.BI = enable;
    }

    @Override
    public void notifyOnCarrierDetect(final boolean enable) {
        monThread.CD = enable;
    }

    @Override
    public void notifyOnCTS(final boolean enable) {
        monThread.CTS = enable;
    }

    @Override
    public void notifyOnDataAvailable(final boolean enable) {
        monThread.Data = enable;
    }

    @Override
    public void notifyOnDSR(final boolean enable) {
        monThread.DSR = enable;
    }

    @Override
    public void notifyOnFramingError(final boolean enable) {
        monThread.FE = enable;
    }

    @Override
    public void notifyOnOutputEmpty(final boolean enable) {
        monThread.Output = enable;
    }

    @Override
    public void notifyOnOverrunError(final boolean enable) {
        monThread.OE = enable;
    }

    @Override
    public void notifyOnParityError(final boolean enable) {
        monThread.PE = enable;
    }

    @Override
    public void notifyOnRingIndicator(final boolean enable) {
        monThread.RI = enable;
    }

    private native int open(String name) throws PortInUseException;

    private native int readArray(byte b[], int off, int len) throws IOException;

    private native int readByte() throws IOException;

    /** Remove the I2C port event listener */
    @Override
    public void removeEventListener() {
        SPEventListener = null;
        if (monThread != null) {
            monThread.interrupt();
            monThread = null;
        }
    }

    /** Write to the port */
    @Override
    public native void sendBreak(int duration);

    public void sendEvent(final int event, final boolean state) {
        switch (event) {
        case I2CPortEvent.DATA_AVAILABLE:
            dataAvailable = 1;
            if (monThread.Data) break;
            return;
        case I2CPortEvent.OUTPUT_BUFFER_EMPTY:
            if (monThread.Output) break;
            return;
        /*
         * if( monThread.DSR ) break; return; if (isDSR()) { if (!dsrFlag) { dsrFlag = true; I2CPortEvent e = new I2CPortEvent(this, I2CPortEvent.DSR, !dsrFlag,
         * dsrFlag ); } } else if (dsrFlag) { dsrFlag = false; I2CPortEvent e = new I2CPortEvent(this, I2CPortEvent.DSR, !dsrFlag, dsrFlag ); }
         */
        case I2CPortEvent.CTS:
            if (monThread.CTS) break;
            return;
        case I2CPortEvent.DSR:
            if (monThread.DSR) break;
            return;
        case I2CPortEvent.RI:
            if (monThread.RI) break;
            return;
        case I2CPortEvent.CD:
            if (monThread.CD) break;
            return;
        case I2CPortEvent.OE:
            if (monThread.OE) break;
            return;
        case I2CPortEvent.PE:
            if (monThread.PE) break;
            return;
        case I2CPortEvent.FE:
            if (monThread.FE) break;
            return;
        case I2CPortEvent.BI:
            if (monThread.BI) break;
            return;
        default:
            System.err.println("unknown event:" + event);
            return;
        }
        final I2CPortEvent e = new I2CPortEvent(this, event, !state, state);
        if (SPEventListener != null) SPEventListener.I2CEvent(e);
    }

    private native void setDSR(boolean state);

    @Override
    public native void setDTR(boolean state);

    native void setflowcontrol(int flowcontrol) throws IOException;

    @Override
    public void setFlowControlMode(final int flowcontrol) {
        try {
            setflowcontrol(flowcontrol);
        } catch (final IOException e) {
            e.printStackTrace();
            return;
        }
        flowmode = flowcontrol;
    }

    /** Set the I2CPort parameters */
    @Override
    public void setI2CPortParams(final int b, final int d, final int s, final int p) throws UnsupportedCommOperationException {
        nativeSetI2CPortParams(b, d, s, p);
        speed = b;
        dataBits = d;
        stopBits = s;
        parity = p;
    }

    @Override
    public void setInputBufferSize(final int size) {
        InputBuffer = size;
    }

    @Override
    public void setOutputBufferSize(final int size) {
        OutputBuffer = size;
    }

    @Override
    public native void setRTS(boolean state);

    private native void writeArray(byte b[], int off, int len) throws IOException;

    private native void writeByte(int b) throws IOException;
}
