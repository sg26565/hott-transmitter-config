package de.treichels.hott.model.serial

import de.treichels.hott.util.Util
import jtermios.windows.WinAPI
import jtermios.windows.WinAPI.*
import java.io.IOException
import java.util.logging.Level

class WinAPISerialPort(portName: String) : SerialPortBase(portName) {
    private var port: WinAPI.HANDLE? = null
    private var lpErrors = IntArray(1)
    private var available = 0
    private var pending = 0
    private var flags = 0

    override fun readFromPort() {
        var retries = 25

        clearCommError()

        while (available == 0 && retries-- > 0) {
            logger.finest("waiting for response")
            Thread.sleep(10)
            clearCommError()
        }

        if (retries < 0) {
            throw IOException("read timeout")
        }

        val data = ByteArray(available)
        val bytesRead = IntArray(1)

        if (ReadFile(port, data, available, bytesRead)) {
            val count = bytesRead[0]
            for (i in 0 until count) readQueue.put(data[i])
            logger.fine("$count/$available bytes read\n${Util.dumpData(data)}")
        } else {
            fail(true)
        }
    }

    override fun writeToPort() {
        val bytesToWrite = writeQueue.size

        val data = ByteArray(bytesToWrite) {
            writeQueue.take()
        }

        logger.fine("sending $bytesToWrite bytes\n${Util.dumpData(data)}")

        val bytesWritten = IntArray(1)
        if (WriteFile(port, data, bytesToWrite, bytesWritten))
            logger.finer("${bytesWritten[0]}/$bytesToWrite bytes written")
        else
            fail(false)
    }

    private fun clearCommError() {
        if (port != null) {
            val comStat = COMSTAT()
            val success = WinAPI.ClearCommError(port, lpErrors, comStat)
            if (success) {
                available = comStat.cbInQue
                pending = comStat.cbOutQue
                flags = comStat.fFlags
            }
        }
    }

    override val isOpen: Boolean
        get() = port != null

    override fun close() {
        if (port != null) {
            WinAPI.CloseHandle(port)
            port = null
        }
    }

    override fun open() {
        // open port
        port = WinAPI.CreateFile("\\\\.\\$portName", GENERIC_READ or GENERIC_WRITE, 0, null, OPEN_EXISTING, 0, null)
        if (port == null || port == INVALID_HANDLE_VALUE) fail(true)

        val dcb = DCB()
        dcb.BaudRate = CBR_115200
        dcb.Parity = NOPARITY
        dcb.ByteSize = 8
        dcb.StopBits = ONESTOPBIT
        dcb.fFlags = DCB.fTXContinueOnXoff
        dcb.DCBlength = dcb.size()

        WinAPI.SetCommState(port, dcb)

        // set buffer sizes
        var success: Boolean = WinAPI.SetupComm(port, 2064, 2064)
        if (!success) fail(true)

        // set timeouts
        val timeouts = COMMTIMEOUTS()
        timeouts.ReadIntervalTimeout = 0
        timeouts.ReadTotalTimeoutConstant = 0
        timeouts.ReadTotalTimeoutMultiplier = 0
        timeouts.WriteTotalTimeoutConstant = 0
        timeouts.WriteTotalTimeoutMultiplier = 0

        success = WinAPI.SetCommTimeouts(port, timeouts)
        if (!success) fail(true)
    }

    private fun fail(throwException: Boolean) {
        val lastError = WinAPI.GetLastError()
        clearCommError()
        val message = "port=$portName, error=$lastError, flags=$flags, available=$available, pending=$pending, lpErrors=${lpErrors[0]}"
        val exception = Exception(message)
        logger.log(Level.SEVERE, message, exception)

        if (throwException) throw exception
    }

    override fun reset() {
        if (port != null) {
            WinAPI.PurgeComm(port, PURGE_RXABORT or PURGE_RXCLEAR or PURGE_TXABORT or PURGE_TXCLEAR)
        }

        readQueue.clear()
        writeQueue.clear()
    }
}