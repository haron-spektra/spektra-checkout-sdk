
import java.math.BigDecimal;

public class Checkout {
    private String successURL, cancelURL, currency, description, spektraAccountName;
    private BigDecimal amount;
    private CustomerData customerData;

    public Checkout() {
    }

    public String getSuccessURL(){return successURL;}
    public void setSuccessURL(String successURL) {this.successURL = successURL;}

    public String getCancelURL(){return cancelURL;}
    public void setCancelURL(String cancelURL) {this.cancelURL = cancelURL;}

    public String getCurrency(){return currency;}
    public void setCurrency(String currency) {this.currency = currency;}

    public String getDescription(){return description;}
    public void setDescription(String description) {this.description = description;}

    public String getSpektraAccountName(){return spektraAccountName;}
    public void setSpektraAccountName(String spektraAccountName) {this.spektraAccountName = spektraAccountName;}

    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}

    public CustomerData getCustomerData(){return customerData;}
    public void setCustomerData(CustomerData customerData) {this.customerData = customerData;}
}
