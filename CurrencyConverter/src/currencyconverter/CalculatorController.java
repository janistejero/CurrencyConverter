package currencyconverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    private CurrencyAPIParser currencyParser;

    private double convertedAmount = 0.00;

    private final ObservableList<String> options
            = FXCollections.observableArrayList(
                    "AED", "ARS", "AUD", "BGN", "BRL", "BSD",
                    "CAD", "CHF", "CLP", "CNY", "COP", "CZK",
                    "DKK", "DOP", "EGP", "EUR", "FJD", "GBP",
                    "GTQ", "HKD", "HRK", "HUF", "IDR", "ILS",
                    "INR", "ISK", "JPY", "KRW", "KZT", "MXN",
                    "MYR", "NOK", "NZD", "PAB", "PEN", "PHP",
                    "PKR", "PLN", "PYG", "RON", "RUB", "SAR",
                    "SEK", "SGD", "THB", "TRY", "TWD", "UAH", 
                    "UYU", "VND", "ZAR"
            );
    @FXML
    private Label amountLbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fromLbl.setText("");
        placeholderLbl.setText("");
        amountLbl.setText("");
        toLbl.setText("");

        fromCurrencyCmb.setItems(options);
        toCurrencyCmb.setItems(options);
    }

    @FXML
    private void handleConvertAction(ActionEvent event) {

        boolean valid = false;
        String from = fromCurrencyCmb.getSelectionModel().getSelectedItem();
        String to = toCurrencyCmb.getSelectionModel().getSelectedItem();
        String amount = amountTxt.getText();

        if (from == null) {
            placeholderLbl.setText("Please choose a currency from");
            valid = false;
        } else if (to == null) {
            placeholderLbl.setText("Please choose a currency to");
            valid = false;
        } else if (amount == null) {
            placeholderLbl.setText("Please enter the amount");
            valid = false;
        } else {
            valid = true;
        }

        if (valid) {
            String exchangeRate = "";

            // Setting URL to get rate from API
            String url_str = "https://api.exchangerate-api.com/v4/latest/" + from;

            try {
                exchangeRate = currencyParser.getExchangeRate(url_str, amount, to);
            } catch (IOException ex) {
                Logger.getLogger(CalculatorController.class.getName()).log(Level.SEVERE, null, ex);
            }

            BigDecimal rate = new BigDecimal(exchangeRate);
            BigDecimal amountBD = new BigDecimal(amount);
            BigDecimal result = amountBD.multiply(rate);

            fromLbl.setText(amountTxt.getText() + " " + from);
            placeholderLbl.setText("converts to");
            amountLbl.setText(result.toString());
            toLbl.setText(toCurrencyCmb.getSelectionModel().getSelectedItem());
        }
    }

}
