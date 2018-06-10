package de.treichels.lzma

import SevenZip.Compression.LZMA.Decoder
import SevenZip.Compression.LZMA.Encoder
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class LZMA {
    companion object {

        fun canCompress(fileName: String) = when (fileName.substringAfterLast('.')) {
            "zip", "7z", "mp3", "exe" -> false
            else -> true
        }

        fun canCompress(file: File) = canCompress(file.name)

        fun compress(inputStream: InputStream, outputStream: OutputStream) {
            inputStream.use {
                outputStream.use {
                    Encoder().apply {
                        SetEndMarkerMode(true)
                        WriteCoderProperties(outputStream)
                        Code(inputStream, outputStream, -1, -1, null)
                    }
                }
            }
        }

        fun uncompress(inputStream: InputStream, outputStream: OutputStream) {
            inputStream.use {
                outputStream.use {
                    Decoder().apply {
                        val properties = ByteArray(5)
                        if (inputStream.read(properties) != properties.size) throw IOException("input .lzma file is too short")
                        if (!SetDecoderProperties(properties)) throw IOException("Incorrect stream properties")
                        if (!Code(inputStream, outputStream, -1)) throw  IOException("Error in data stream")
                    }
                }
            }
        }
    }
}
