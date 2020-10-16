package sample;

import Dictionary.DictionaryManagement;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller extends Application implements Initializable {
    public static DictionaryManagement manage = new DictionaryManagement();
    private static final DictionaryManagement markupList = new DictionaryManagement();

    public static boolean isChanged = false;
    @FXML
    private ListView<String> listView;
    private final AddController addWindow = new AddController();
    private final DeleteController deleteWindow = new DeleteController();
    private final ModifyController modifyWindow = new ModifyController();
    private final AuthorController AuthorWindow = new AuthorController();

    @FXML
    private TextField input;
    @FXML
    private Text define;
    @FXML
    private Label engWord;
    @FXML
    private TextArea list_Word;
    @FXML
    private Button EnToVn;
    @FXML
    private Button VnToEn;
    @FXML
    private TextArea history;
    @FXML
    private TextArea mark;
    @FXML
    private Button starMarkup;

    private String TargetLanguage = "VI";

    private ObservableList<String> list;

    public Controller() throws EngineException {
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public Synthesizer synthesizer
            = Central.createSynthesizer(
            new SynthesizerModeDesc(Locale.US));

    public void initialize() {
        mark.setText(markupList.showAllWords());
    }

    //@Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        manage.insertFromFile();
        markupList.insertFromFile("markUp");
        System.setProperty(
                "freetts.voices",
                "com.sun.speech.freetts.en.us"
                        + ".cmu_us_kal.KevinVoiceDirectory");
        Central.registerEngineCentral(
                "com.sun.speech.freetts"
                        + ".jsapi.FreeTTSEngineCentral");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(e -> {
            if (isChanged) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Thoát");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có muốn lưu lại những thay đổi trước khi thoát?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                    manage.dictionaryExportToFile();
                    manage.saveFile();
                    markupList.saveFile("markUp");
                }
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Thoát");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có chắc chắn đồng ý muốn thoát?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                    primaryStage.close();
                }
                e.consume();
            }
        });
        primaryStage.setTitle("Bachh's DICTIONARY");
        primaryStage.getIcons().add(new Image("/images/icon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public void mark() {
        if (markupList.dictionaryLookup(engWord.getText()) >= 0) {
            starMarkup.setStyle("-fx-background-image: url('/images/star1.png');" +
                    " -fx-background-position: center;\n" +
                    "-fx-background-size: cover;\n" +
                    "-fx-border-radius: 100px;\n" +
                    "-fx-background-radius: 100px;");
            markupList.deleteWord(engWord.getText(), define.getText());
            mark.setText(markupList.showAllWords());
        } else {
            starMarkup.setStyle("-fx-background-image: url('/images/star2.png');" +
                    " -fx-background-position: center;\n" +
                    "-fx-background-size: cover;\n" +
                    "-fx-border-radius: 100px;\n" +
                    "-fx-background-radius: 100px;");
            markupList.addWord(engWord.getText(), define.getText());
            mark.appendText(engWord.getText() + " : " + define.getText());
        }
    }

    public void inputChanged(KeyEvent event) {

        mark.setText(markupList.showAllWords());
        if (event.getCode() == KeyCode.DOWN) {
            listView.requestFocus();
        } else {
            String prefix = input.getText().toLowerCase();
            list_Word.setText(manage.showSomeWords(prefix));
            if (prefix.length() == 0) {
                if (prefix.length() == 0) {
                    list = FXCollections.observableArrayList();
                    listView.getItems().clear();
                    listView.setItems(list);
                    listView.getSelectionModel().select(0);
                } else {
                    List<String> words = manage.prefixSeach(prefix);
                    list = FXCollections.observableArrayList(words);
                    listView.getItems().clear();
                    listView.setItems(list);
                    listView.getSelectionModel().select(0);
                }
            }
        }
    }

    public static boolean contains(String key) {
        return manage.dictionaryLookup(key) >= 0;
    }

    public static void add(String text, String text1) {
        manage.getDic().addWord(text, text1);
    }

    public static void remove(String text, String text1) {
        manage.getDic().removeWord(text, text1);
    }

    public static void modify(String text, String text1) {
        manage.getDic().modifyWord(text, text1);
    }


    public void seachClicked() throws IOException, GeneralSecurityException, InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        String word = input.getText();
        if (checkInternet()) {
            engWord.setText(word);
            define.setText(translateText(word, TargetLanguage));
            history.appendText("(" + dtf.format(now) + ") " + word + " : " + translateText(word, TargetLanguage) + '\n');

            if (markupList.dictionaryLookup(engWord.getText()) >= 0) {
                starMarkup.setStyle("-fx-background-image: url('/images/star2.png');" +
                        " -fx-background-position: center;\n" +
                        "-fx-background-size: cover;\n" +
                        "-fx-border-radius: 100px;\n" +
                        "-fx-background-radius: 100px;");
            } else {
                starMarkup.setStyle("-fx-background-image: url('/images/star1.png');" +
                        " -fx-background-position: center;\n" +
                        "-fx-background-size: cover;\n" +
                        "-fx-border-radius: 100px;\n" +
                        "-fx-background-radius: 100px;");
            }
        } else {
            if (manage.dictionaryLookup(word) >= 0) {
                String result = manage.getDic().getWord(manage.dictionaryLookup(word)).getWord_explain();
                define.setText(result);
                engWord.setText(word);
                history.appendText("(" + dtf.format(now) + ") - " + word + " : " + result + '\n');

                if (markupList.dictionaryLookup(engWord.getText()) >= 0) {
                    starMarkup.setStyle("-fx-background-image: url('/images/star1.png');" +
                            " -fx-background-position: center;\n" +
                            "-fx-background-size: cover;\n" +
                            "-fx-border-radius: 100px;\n" +
                            "-fx-background-radius: 100px;");
                } else {
                    starMarkup.setStyle("-fx-background-image: url('/images/star2.png');" +
                            " -fx-background-position: center;\n" +
                            "-fx-background-size: cover;\n" +
                            "-fx-border-radius: 100px;\n" +
                            "-fx-background-radius: 100px;");
                }

            } else {
                define.setText("Từ \"" + word + "\" không có trong dữ liệu từ điển!");
                engWord.setText("");
            }
        }
    }


    public String translateText(String word, String TargetLanguage) throws IOException, GeneralSecurityException {
        Translate t = new Translate.Builder(
                GoogleNetHttpTransport.newTrustedTransport()
                , GsonFactory.getDefaultInstance(), null)
                .setApplicationName("Translate")
                .build();
        Translate.Translations.List list = t.new Translations().list(
                Arrays.asList(
                        word),
                TargetLanguage);

        list.setKey("AIzaSyBfrjADyNCTDqDeoHQ2ZR_k67dQVxTrmi0");
        TranslationsListResponse response = list.execute();
        String result = "";
        for (TranslationsResource translationsResource : response.getTranslations()) {
            result += translationsResource.getTranslatedText();
        }
        return result;
    }

    public void clickedListView() {
        define.setText(manage.get(listView.getSelectionModel().getSelectedItem()));
        engWord.setText(listView.getSelectionModel().getSelectedItem());
    }

    public void voiceSearch() {

    }

    public void saveItemClicked() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Lưu dữ liệu thành công!");
        alert.showAndWait();
        manage.saveFile();
        markupList.saveFile("markUp");
    }

    public void exportItemClicked() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Xuất dữ liệu thành công!");
        alert.showAndWait();
        manage.dictionaryExportToFile();
        markupList.saveFile("markUp");
    }

    public static boolean checkInternet() throws InterruptedException, IOException {
        try {
            URL url = new URL("https://www.google.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void speakNoInternet(String text) {
        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");
        voice.allocate();
        voice.speak(text);
    }

    public void speechTarget() throws IOException, InterruptedException {
        if (checkInternet()) {
            if (TargetLanguage == "VI") {
                tts convert = new tts("en-US");
                convert.textToSpeech(engWord.getText());
            } else {
                tts convert = new tts("vi-VN");
                convert.textToSpeech(engWord.getText());
            }
        } else {
            speakNoInternet(engWord.getText());
        }
    }


    public void speechExplain() throws IOException, InterruptedException {
        if (checkInternet()) {
            if (TargetLanguage.equals("VI")) {
                tts convert = new tts("vi-VN");
                convert.textToSpeech(define.getText());
            } else {
                tts convert = new tts("en-US");
                convert.textToSpeech(define.getText());
            }
        } else {
            speakNoInternet(define.getText());
        }
    }

    public void swapEnToVi() throws IOException, InterruptedException {
        if (checkInternet()){
            TargetLanguage = "VI";
            EnToVn.setStyle("-fx-background-color: #1e2956;-fx-text-fill: #fec400;");
            VnToEn.setStyle("-fx-background-color: #fec400;-fx-text-fill: #1e2956;");
        }
        else {
            manage.insertFromFile();
            EnToVn.setStyle("-fx-background-color: #1e2956;-fx-text-fill: #fec400;");
            VnToEn.setStyle("-fx-background-color: #fec400;-fx-text-fill: #1e2956;");
        }
    }

    public void swapViToEn() throws IOException, InterruptedException {
        if (checkInternet()){
            TargetLanguage = "EN";
            EnToVn.setStyle("-fx-background-color: #fec400;-fx-text-fill: #1e2956;");
            VnToEn.setStyle("-fx-background-color: #1e2956;-fx-text-fill: #fec400;");
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Warning !!!");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng kiểm tra kết nối internet");
            alert.showAndWait();
//            manage.insertFromFile("ViToEn");
            EnToVn.setStyle("-fx-background-color: #fec400;-fx-text-fill: #1e2956;");
            VnToEn.setStyle("-fx-background-color: #1e2956;-fx-text-fill: #fec400;");
        }
    }

    public void openAddWindow() {
        addWindow.run();
    }

    public void openModifyWindow() {
        modifyWindow.run();
    }

    public void openDeleteWindow() {
        deleteWindow.run();
    }

    public void openAuthorWindow() {
        AuthorWindow.run();
    }
}