package sample;


import com.darkprograms.speech.synthesiser.Synthesiser;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.IOException;
import java.io.InputStream;

public class tts {

    Synthesiser synthesizer = new Synthesiser("AIzaSyBfrjADyNCTDqDeoHQ2ZR_k67dQVxTrmi0");

    private String targetLanguage = "en-US";

    public InputStream input = null;

    public tts() {
        synthesizer.setLanguage(targetLanguage);
    }

    public tts(String targetLanguage) {
        this.targetLanguage = targetLanguage;
        synthesizer.setLanguage(targetLanguage);
    }

    public InputStream getInput() {
        return input;
    }

    public void textToSpeech(String text) {
        Thread thread = new Thread(() -> {
            try {
                input = synthesizer.getMP3Data(text);
                Player player = new Player(input);
                player.play();

            } catch (IOException | JavaLayerException ioException) {
                ioException.printStackTrace();
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
}
