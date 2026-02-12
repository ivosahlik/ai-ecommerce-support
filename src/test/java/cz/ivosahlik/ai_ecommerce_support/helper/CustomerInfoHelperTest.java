package cz.ivosahlik.ai_ecommerce_support.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;

class CustomerInfoHelperTest {

    @Test
    public void test() {
        String regex = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}";
        Pattern pattern = Pattern.compile(regex);
        String phone = "+420 123 456 789";
        System.out.println(pattern.matcher(phone).matches());
        Assertions.assertTrue(pattern.matcher(phone).matches());
    }

}