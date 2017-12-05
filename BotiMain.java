package RollCallTelBot;

import iso_project.MyBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class BotiMain {
    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        MyBoti myBot = new MyBoti();
        try {
            botsApi.registerBot(myBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
