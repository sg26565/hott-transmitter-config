package gde.model.voice;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.UUID;

import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;
import java.util.List;

public class VoiceRSS implements Text2Speech {

    private final static String VOICE_RSS_API_KEY = "1def8e9c6ebf4a2eb02fc7b510b04387";

    @Override
    public File convert(String text, String language) throws Exception {

        VoiceProvider tts = new VoiceProvider(VOICE_RSS_API_KEY);

        // Give file a fancy name
        int len = 10;
        if (text.length() < 10)
            len = text.length();

        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        String filename = text.substring(0, len) + ".wav";
        File file = new File(tmpdir, filename);

        while (file.exists()) {
            file = new File(tmpdir, text.substring(0, len) + "_" + UUID.randomUUID().toString().substring(0, 3) + ".wav");
        }

        VoiceParameters params = new VoiceParameters(text, language);
        params.setCodec(AudioCodec.WAV);
        params.setFormat(AudioFormat.Format_11KHZ.AF_11khz_16bit_mono);
        params.setBase64(false);
        params.setSSML(false);
        params.setRate(0);

        byte[] voice = tts.speech(params);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(voice, 0, voice.length);
        fos.flush();
        fos.close();

        return file;
    }

    public static List<String> getLanguages () {
        return Arrays.asList(
                Languages.German,
                Languages.English_GreatBritain,
                Languages.English_UnitedStates,
                Languages.Norwegian,
                Languages.Catalan,
                Languages.Chinese_China,
                Languages.Chinese_HongKong,
                Languages.Chinese_Taiwan,
                Languages.Danish,
                Languages.Dutch,
                Languages.English_Australia,
                Languages.English_Canada,
                Languages.English_India,
                Languages.Finnish,
                Languages.French_Canada,
                Languages.French_France,
                Languages.Italian,
                Languages.Japanese,
                Languages.Korean,
                Languages.Polish,
                Languages.Portuguese_Brazil,
                Languages.Portuguese_Portugal,
                Languages.Russian,
                Languages.Spanish_Mexico,
                Languages.Spanish_Spain
        );
    }
}