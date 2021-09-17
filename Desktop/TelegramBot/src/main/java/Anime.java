import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;


public class Anime {
    String title;
    String rate;
    String url;


    public Anime(String title, String rate, String url) {
        this.title = title;
        this.rate = rate;
        this.url = url;
    }

    public static String getAnime() {
        Anime anime;
        Random random = new Random();
        while (true) {
            int randomId = random.nextInt(10000);
            if (getTitle(randomId).equals("404") | getTitle(randomId).equals("18+")) {
                waitRequest();
            } else {
                waitRequest();
                String title = getTitle(randomId);
                String rate = getRate(randomId);
                String url = getURL(randomId);
                anime = new Anime(title, rate, url);
                break;
            }

        }
        return anime.toString();
    }


    private static String getTitle(int id) {
        String title;
        try {
            Document doc = Jsoup.connect("https://shikimori.one/animes/" + id).get();
            title = doc.getElementsByAttributeValue("class", "head").first().child(1).text();
        } catch (IOException e) {
            title = "404";
        } catch (NullPointerException e) {
            title = "18+";
        }

        return title;
    }

    private static String getRate(int id) {
        String rate = "";
        try {
            Document doc = Jsoup.connect("https://shikimori.one/animes/" + id).get();
            rate = doc.getElementsByAttributeValue("class", "scores").first().child(3).child(1).child(0).text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    private static String getURL(int id) {
        return "https://shikimori.one/animes/" + id;
    }

    @Override
    public String toString() {
        return "Название: " + this.title + "\n" +
                "Рейтинг: " + this.rate + "\n" +
                this.url;
    }

    static void waitRequest() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

