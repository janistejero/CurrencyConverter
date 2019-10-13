package currencyconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyAPIParser {

    public String getExchangeRate(String url_str, String amount, String to) 
            throws IOException {

        String exchangeRate = "";
        try {
            // Making Request
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) 
                    request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            // Accessing object (com.google.gson.JsonObject)
            JsonObject rates = jsonobj.getAsJsonObject("rates");

            exchangeRate = rates.get(to).getAsString();
            System.out.println(exchangeRate.getClass().getName());
        } catch (IOException ex) {
            Logger.getLogger(CurrencyAPIParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return exchangeRate;
    }
}
