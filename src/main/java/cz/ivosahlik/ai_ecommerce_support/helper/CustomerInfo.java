package cz.ivosahlik.ai_ecommerce_support.helper;

public record CustomerInfo(String emailAddress, String phoneNumber, String orderNumber) {

    public CustomerInfo(String emailAddress, String phoneNumber) {
        this(emailAddress, phoneNumber, "");
    }
}
