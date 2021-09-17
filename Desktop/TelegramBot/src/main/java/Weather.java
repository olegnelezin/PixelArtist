import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=88e3de03af66b0d78f74b46972440612");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        while(scanner.hasNext()) {
            result += scanner.nextLine();
        }
        JSONObject obj = new JSONObject(result);
        model.setName(obj.getString("name"));

        JSONObject main = obj.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONObject wind = obj.getJSONObject("wind");
        model.setSpeed(wind.getDouble("speed"));


        JSONArray getArray = obj.getJSONArray("weather");

        for(int i = 0; i<getArray.length(); i++) {
            JSONObject object = getArray.getJSONObject(i);
            model.setIcon((String) object.get("icon"));
            model.setMain((String) object.get("main"));

        }

        System.out.println(result);
        return "Город: " + model.getName() + "\n" +
                "Температура: " + model.getTemp() + "\n" +
                "Влажность: " + model.getHumidity() + "\n" +
                "Скорость ветра: " + model.getSpeed() + " м/c" + "\n";

    };
}
