package bot;

import cosinedist.CosineDistance;
import dbrequest.Request;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import reader.MyFileReader;

import java.util.ArrayList;
import java.util.List;

public class MessageThread {
    private Message message;
    private Request request;
    private Long chatId;

    public MessageThread(Message message, Request request) {
        this.message = message;
        this.request = request;
        this.chatId = message.getChatId();
    }

    public SendMessage answerOnMessage() {
        assert message != null;

        if (message.hasText()) {
            SendMessage result;
            String resourcesPath = "./src/main/resources/";
            // TODO костыль для CallBack'а, надо фиксануть

            try {
                if (message.getReplyMarkup().getKeyboard().get(0).get(0).getCallbackData().equals("/register")) {
                    request.addNewUser(chatId);
                    result = sendMsg(MyFileReader.readFile(resourcesPath + "register-message.txt"));
                    return result;
                }
            } catch (NullPointerException ignored) {

            }

            switch (message.getText()) {
                case "/register":
                    request.addNewUser(chatId);
                    result = sendMsg(MyFileReader.readFile(resourcesPath + "register-message.txt"));
                    return result;
                case "/start":
                    result = sendMsg(MyFileReader.readFile(resourcesPath + "start-message.txt"));
                    sendInlineKeyBoard(result, "Зарегистрироваться", "/register");
                    return result;
                default:
                    int status = request.getUserStatus(chatId);
                    if (status == -1) {
                        result = sendMsg(MyFileReader.readFile(resourcesPath + "unregistered-user-message.txt"));
                        sendInlineKeyBoard(result, "Зарегистрироваться", "/register");
                    } else {
                        if (request.setText(message.getText(), chatId)) {
                            if (status == 0) {
                                result = sendMsg(MyFileReader.readFile(resourcesPath + "first-text-message.txt"));
                            } else {
                                double res = CosineDistance.CosineSimilarityScore(request.getText(chatId, false),
                                        request.getText(chatId, true));
                                res *= 100;
                                request.eraseTexts(chatId);
                                if ((int) res == 0) {
                                    result = sendMsg(MyFileReader.readFile(resourcesPath + "different-texts.txt"));
                                } else {
                                    result = sendMsg(
                                            "Ваши тексты похожи примерно на: " + (int) res + "%\n"
                                                    + MyFileReader.readFile(resourcesPath + "result-message.txt"));
                                }
                            }
                        } else {
                            result = sendMsg(
                                    MyFileReader.readFile(resourcesPath + "undefined-error.txt"));
                        }
                    }
                    return result;
            }
        }
        return null;
    }

    private SendMessage sendMsg(String text) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);

        return sendMessage.setText(text);
    }

    public void sendInlineKeyBoard(SendMessage message, String text, String command) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        InlineKeyboardButton registerButton = new InlineKeyboardButton();
        registerButton.setText(text).setCallbackData(command);

        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(registerButton);

        List<List<InlineKeyboardButton>> rowList = List.of(
                firstRow
        );

        keyboard.setKeyboard(rowList);
        message.setReplyMarkup(keyboard);
    }
}
