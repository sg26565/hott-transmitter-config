package de.treichels.lzma

import SevenZip.Compression.LZMA.Decoder
import SevenZip.Compression.LZMA.Encoder
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

fun canCompress(fileName: String) = when (fileName.substringAfterLast('.')) {
    "pdf", "jar", "zip", "7z", "mp3", "exe" -> false
    else -> true
}

fun canCompress(file: File) = file.isFile && canCompress(file.name)

fun compress(inputStream: InputStream, outputStream: OutputStream) {
    inputStream.use { instream ->
        outputStream.use { outstream ->
            Encoder().apply {
                SetEndMarkerMode(true)
                WriteCoderProperties(outstream)
                Code(instream, outstream, -1, -1, null)
            }
        }
    }
}

fun compress(inputFile: File, outputFile: File) {
    compress(inputFile.inputStream(), outputFile.outputStream())
}

fun uncompress(inputStream: InputStream, outputStream: OutputStream) {
    inputStream.use { instream ->
        outputStream.use { outstream ->
            Decoder().apply {
                val properties = ByteArray(5)
                if (instream.read(properties) != properties.size) throw IOException("input .lzma file is too short")
                if (!SetDecoderProperties(properties)) throw IOException("Incorrect stream properties")
                if (!Code(instream, outstream, -1)) throw  IOException("Error in data stream")
            }
        }
    }
}

fun uncompress(inputFile: File, outputFile: File) {
    uncompress(inputFile.inputStream(), outputFile.outputStream())
}
