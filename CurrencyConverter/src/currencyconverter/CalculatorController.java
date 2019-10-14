package currencyconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Janis Tejero
 */
public class CalculatorController implements Initializable {

    @FXML
    private ComboBox<String> toCurrencyCmb;
    @FXML
    private ComboBox<String> fromCurrencyCmb;
    @FXML
    private Button convertBtn;
    @FXML
    private Label fromLbl;
    @FXML
    private Label placeholderLbl;
    @FXML
    private Label toLbl;
    @FXML
    private TextField amountTxt;

    private double convertedAmount = 0.00;

    private final ObservableList<String> options
            = FXCollections.observableArrayList(
                    "AED", "ARS", "AUD",
                    "BGN", "BRL", "BSD",
                    "CAD", "CHF", "CLP", "CNY", "COP", "CZK",
                    "DKK", "DOP" ,
                    "EGP", "EUR",
                    "FJD",
                    "GBP",
                    "GTQ",
                    "HKD", "HRK", "HUF",
                    "IDR", "ILS", "INR", "ISK",
                    "JPY",
                    "KRW",
                    "KZT",
                    "MXN", "MYR",
                    "NOK", "NZD",
                    "PAB", "PEN", "PHP", "PKR", "PLN", "PYG",
                    "RON", "RUB",
                    "SAR", "SEK", "SGD",
                    "THB", "TRY", "TWD",
                    "UAH", "USD", "UYU", 
                    "VND",
                    "ZAR"
            );
    @FXML
    private Pane resultPane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fromLbl.setText("");
        placeholderLbl.setText("");
        toLbl.setText("");

        fromCurrencyCmb.getItems().addAll(options);
        toCurrencyCmb.getItems().addAll(options);

        fromCurrencyCmb.setOnAction(new  EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fromLbl.setText(fromCurrencyCmb.getSelectionModel().getSelectedItem());
            }
        });

        toCurrencyCmb.setOnAction(new  EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toLbl.setText("in " + toCurrencyCmb.getSelectionModel().getSelectedItem());
            }
        });
        
        amountTxt.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fromLbl.setText(newValue + " " + fromCurrencyCmb.getSelectionModel().getSelectedItem());
           
            }
            
        });
    }

    @FXML
    private void handleConvertAction(ActionEvent event) {

        boolean valid = false;
        String from = fromCurrencyCmb.getSelectionModel().getSelectedItem();
        String to = toCurrencyCmb.getSelectionModel().getSelectedItem();
        String amount = amountTxt.getText();

        fromCurrencyCmb.setPromptText(from);
        toCurrencyCmb.setPromptText(to);

        // simple validation
        if (amount == null) {
            placeholderLbl.setText("Please enter the amount");
            valid = false;
        }

        if (from == null) {
            placeholderLbl.setText("Please choose a currency from");
            valid = false;
        } else if (to == null) {
            placeholderLbl.setText("Please choose a currency to");
            valid = false;
        } else {
            valid = true;
        }

        if (valid) {
            String exchangeRate = "";

            // Setting URL to get rate from API
            String url_str = "https://api.exchangerate-api.com/v4/latest/" + from;

            exchangeRate = getExchangeRate(url_str, to);

            BigDecimal rate = new BigDecimal(exchangeRate);
            BigDecimal amountBD = new BigDecimal(amount);
            BigDecimal result = amountBD.multiply(rate);
            MathContext m = new MathContext(3);
            result = result.round(m);
            
            
            resultPane.getStyleClass().add("pane-border");
            fromLbl.setText(amountTxt.getText() + " " + from);
            placeholderLbl.setText("converts to");
            toLbl.setText(result.toString() + " " + toCurrencyCmb.getSelectionModel().getSelectedItem());
        }
    }

    public String getExchangeRate(String url_str, String to) {
        String exchangeRate = "";
        try {
            // Making Request
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            // Accessing object (com.google.gson.JsonObject)
            JsonObject rates = jsonobj.getAsJsonObject("rates");

            exchangeRate = rates.get(to).getAsString();
        } catch (IOException ex) {
            System.out.println("IOException");
            placeholderLbl.setText("Error in converting occured");
        }
        return exchangeRate;
    }

}
