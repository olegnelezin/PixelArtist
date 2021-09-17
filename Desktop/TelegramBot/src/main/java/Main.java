import com.fasterxml.jackson.annotation.JsonCreator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;



public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws IOException {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Main());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "NelezinBot";
    }

    @Override
    public String getBotToken() {
        return "1880982134:AAG1qafNUJOeW1XwaDE4u9oSmKwN85fit4M";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "/anime":
                    sendMsg(update.getMessage().getChatId().toString(), "Запрос обрабатывается");
                    sendMsg(update.getMessage().getChatId().toString(), Anime.getAnime());
                    break;

                case "/info":
                    String info = """
                            Здравствуйте, я бот Kaрина.
                            Команды:\s
                            /w "Название Города" - показывает погоду в городе
                            /anime - выдает случайное аниме с сайта shikimori.one
                            /about - информация о разработчике""";
                    sendMsg(update.getMessage().getChatId().toString(), info);
                    break;
                case "/about":
                    String about = """
                            Разработчик: Нелезин Олег, г.Воронеж
                            Vk: vk.com/tiredofthisplaceee
                            Gibhub: github.com/olegnelezin
                            """;
                    sendMsg(update.getMessage().getChatId().toString(), about);
                    break;
                default:
                        if (update.getMessage().getText().contains("/w")) {
                            Model model = new Model();
                            String s = update.getMessage().getText();
                            s = s.replace("/w ", "");
                            try {
                                sendMsg(update.getMessage().getChatId().toString(), Weather.getWeather(s, model));
                            } catch (FileNotFoundException e) {
                                sendMsg(update.getMessage().getChatId().toString(), "Неверный формат или неизвестный город");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            sendMsg(update.getMessage().getChatId().toString(), "Такой команды не существует.");
                        }
                    break;
            }
        }
    }

    public void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    }

