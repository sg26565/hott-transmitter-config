package de.treichels.hott.tts.win10

import com.fasterxml.jackson.databind.ObjectMapper
import de.treichels.hott.tts.Quality
import de.treichels.hott.tts.Text2SpeechProvider
import de.treichels.hott.tts.Voice
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class Win10Voice : Voice() {
    // culture is an alias for locale
    @Suppress("unused")
    var culture: String
        get() = locale.toLanguageTag()
        set(culture) {
            locale = Locale.forLanguageTag(culture)
        }
}

class Win10TTSProvider : Text2SpeechProvider() {
    companion object {
        /*
        val cmd = """
            Add-Type -AssemblyName System.Speech
            ${'$'}SpeechSynthesizer = New-Object System.Speech.Synthesis.SpeechSynthesizer
            ${'$'}voiceList = ${'$'}SpeechSynthesizer.GetInstalledVoices()

            Write-Host "["
            [bool] ${'$'}first = ${'$'}true

            foreach(${'$'}voice in ${'$'}voiceList) {
                if (${'$'}first) {
                    Write-Host "{"
                    ${'$'}first = ${'$'}false
                } else {
                    Write-Host ",{"
                }

                Write-Host ""${'"'}enabled"" : ""${'$'}(${'$'}voice.Enabled)"","
                ${'$'}info = ${'$'}voice.VoiceInfo
                Write-Host ""${'"'}age"" : ""${'$'}(${'$'}info.Age)"","
                Write-Host ""${'"'}culture"" : ""${'$'}(${'$'}info.Culture)"","
                Write-Host ""${'"'}description"" : ""${'$'}(${'$'}info.Description)"","
                Write-Host ""${'"'}gender"" : ""${'$'}(${'$'}info.Gender)"","
                Write-Host ""${'"'}id"" : ""${'$'}(${'$'}info.Id)"","
                Write-Host ""${'"'}name"" : ""${'$'}(${'$'}info.Name)""${'"'}
                Write-Host "}"
            }

            Write-Host "]"
            ${'$'}SpeechSynthesizer.Dispose()
            exit
        """
        val installedVoices = String(Base64.getEncoder().encode(cmd.toByteArray(Charsets.UTF_16LE)))
        */

        // base64 UTF-16LE encoded version of the above script
        const val installedVoices = "CgBBAGQAZAAtAFQAeQBwAGUAIAAtAEEAcwBzAGUAbQBiAGwAeQBOAGEAbQBlACAAUwB5AHMAdABlAG0ALgBTAHAAZQBlAGMAaAAKACQAUwBwAGUAZQBjAGgAUwB5AG4AdABoAGUAcwBpAHoAZQByACAAPQAgAE4AZQB3AC0ATwBiAGoAZQBjAHQAIABTAHkAcwB0AGUAbQAuAFMAcABlAGUAYwBoAC4AUwB5AG4AdABoAGUAcwBpAHMALgBTAHAAZQBlAGMAaABTAHkAbgB0AGgAZQBzAGkAegBlAHIACgAkAHYAbwBpAGMAZQBMAGkAcwB0ACAAPQAgACQAUwBwAGUAZQBjAGgAUwB5AG4AdABoAGUAcwBpAHoAZQByAC4ARwBlAHQASQBuAHMAdABhAGwAbABlAGQAVgBvAGkAYwBlAHMAKAApAAoACgBXAHIAaQB0AGUALQBIAG8AcwB0ACAAIgBbACIACgBbAGIAbwBvAGwAXQAgACQAZgBpAHIAcwB0ACAAPQAgACQAdAByAHUAZQAKAAoAZgBvAHIAZQBhAGMAaAAoACQAdgBvAGkAYwBlACAAaQBuACAAJAB2AG8AaQBjAGUATABpAHMAdAApACAAewAKACAAIAAgACAAaQBmACAAKAAkAGYAaQByAHMAdAApACAAewAKACAAIAAgACAAIAAgACAAIABXAHIAaQB0AGUALQBIAG8AcwB0ACAAIgB7ACIACgAgACAAIAAgACAAIAAgACAAJABmAGkAcgBzAHQAIAA9ACAAJABmAGEAbABzAGUACgAgACAAIAAgAH0AIABlAGwAcwBlACAAewAKACAAIAAgACAAIAAgACAAIABXAHIAaQB0AGUALQBIAG8AcwB0ACAAIgAsAHsAIgAKACAAIAAgACAAfQAKAAoAIAAgACAAIABXAHIAaQB0AGUALQBIAG8AcwB0ACAAIgAiACIAZQBuAGEAYgBsAGUAZAAiACIAIAA6ACAAIgAiACQAKAAkAHYAbwBpAGMAZQAuAEUAbgBhAGIAbABlAGQAKQAiACIALAAiAAoAIAAgACAAIAAkAGkAbgBmAG8AIAA9ACAAJAB2AG8AaQBjAGUALgBWAG8AaQBjAGUASQBuAGYAbwAKACAAIAAgACAAVwByAGkAdABlAC0ASABvAHMAdAAgACIAIgAiAGEAZwBlACIAIgAgADoAIAAiACIAJAAoACQAaQBuAGYAbwAuAEEAZwBlACkAIgAiACwAIgAKACAAIAAgACAAVwByAGkAdABlAC0ASABvAHMAdAAgACIAIgAiAGMAdQBsAHQAdQByAGUAIgAiACAAOgAgACIAIgAkACgAJABpAG4AZgBvAC4AQwB1AGwAdAB1AHIAZQApACIAIgAsACIACgAgACAAIAAgAFcAcgBpAHQAZQAtAEgAbwBzAHQAIAAiACIAIgBkAGUAcwBjAHIAaQBwAHQAaQBvAG4AIgAiACAAOgAgACIAIgAkACgAJABpAG4AZgBvAC4ARABlAHMAYwByAGkAcAB0AGkAbwBuACkAIgAiACwAIgAKACAAIAAgACAAVwByAGkAdABlAC0ASABvAHMAdAAgACIAIgAiAGcAZQBuAGQAZQByACIAIgAgADoAIAAiACIAJAAoACQAaQBuAGYAbwAuAEcAZQBuAGQAZQByACkAIgAiACwAIgAKACAAIAAgACAAVwByAGkAdABlAC0ASABvAHMAdAAgACIAIgAiAGkAZAAiACIAIAA6ACAAIgAiACQAKAAkAGkAbgBmAG8ALgBJAGQAKQAiACIALAAiAAoAIAAgACAAIABXAHIAaQB0AGUALQBIAG8AcwB0ACAAIgAiACIAbgBhAG0AZQAiACIAIAA6ACAAIgAiACQAKAAkAGkAbgBmAG8ALgBOAGEAbQBlACkAIgAiACIACgAgACAAIAAgAFcAcgBpAHQAZQAtAEgAbwBzAHQAIAAiAH0AIgAKAH0ACgAKAFcAcgBpAHQAZQAtAEgAbwBzAHQAIAAiAF0AIgAKACQAUwBwAGUAZQBjAGgAUwB5AG4AdABoAGUAcwBpAHoAZQByAC4ARABpAHMAcABvAHMAZQAoACkACgBlAHgAaQB0AAoA"
    }

    override val enabled = System.getProperty("os.name") == "Windows 10" // This provider is only available on Windows 10
    override val name = "Microsoft Windows 10"
    override val qualities: List<Quality> = listOf(Quality(11025, 1), Quality(11025, 2), Quality(22050, 1), Quality(22050, 2), Quality(24000, 1), Quality(24000, 2))

    override fun installedVoices(): List<Voice> {
        // start PowerShell runtime
        val process = Runtime.getRuntime().exec(arrayOf("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe", "-EncodedCommand", "$installedVoices"))
        process.waitFor(1, TimeUnit.MILLISECONDS)

        // ignore errors
        process.errorStream.reader().forEachLine { }

        // parse JSON result
        val mapper = ObjectMapper()
        val type = mapper.typeFactory.constructCollectionType(List::class.java, Win10Voice::class.java)
        return mapper.readValue(process.inputStream.reader(), type)
    }

    override fun speak(text: String, voice: Voice, speed: Int, volume: Int, quality: Quality, ssml: Boolean): AudioInputStream {
        val tempFile = File.createTempFile("wave", ".wav")
        lateinit var process: Process

        try {
            // filter unsafe characters (double single quotes and replace newlines with blank)
            val filtered = text.replace("'", "''").replace('\n', ' ').replace('\r', ' ')

            // PowerShell script
            val cmd = """
Add-Type -AssemblyName System.Speech
${'$'}SpeechSynthesizer = New-Object System.Speech.Synthesis.SpeechSynthesizer
${'$'}SpeechSynthesizer.Rate = $speed
${'$'}SpeechSynthesizer.Volume = ${Math.min(volume, 100)}
${'$'}format = New-Object System.Speech.AudioFormat.SpeechAudioFormatInfo(${quality.sampleRate}, ${quality.sampleSize}, ${quality.channels})
${'$'}SpeechSynthesizer.SetOutputToWaveFile('${tempFile.absolutePath}', ${'$'}format)
${'$'}SpeechSynthesizer.SelectVoice('${voice.name}')
${'$'}SpeechSynthesizer.${if (ssml) "SpeakSsml" else "Speak"}('$filtered')
${'$'}SpeechSynthesizer.Dispose()
exit
"""
            // Base64 encode with UTF-16LE
            val encoded = String(Base64.getEncoder().encode(cmd.toByteArray(Charsets.UTF_16LE)))

            // start PowerShell runtime
            process = Runtime.getRuntime().exec(arrayOf("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe", "-EncodedCommand", "$encoded"))

            // wait for completion
            process.waitFor(10, TimeUnit.SECONDS)

            // read file bytes into memory
            val bytes = tempFile.readBytes()

            // convert to AudioInputStream
            return AudioSystem.getAudioInputStream(bytes.inputStream())
        } finally {
            if (process.isAlive) process.destroy()
            tempFile.delete()
        }
    }
}
