package bot;

import dbrequest.Request;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "username";
    private static final String BOT_TOKEN = "token";

    private static Request request = new Request();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        request.connect();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            MessageThread message = new MessageThread(update.getMessage(), request);
            SendMessage sendMessage = message.answerOnMessage();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            MessageThread message = new MessageThread(update.getCallbackQuery().getMessage(), request);
            SendMessage sendMessage = message.answerOnMessage();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return BOT_USERNAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }
}
