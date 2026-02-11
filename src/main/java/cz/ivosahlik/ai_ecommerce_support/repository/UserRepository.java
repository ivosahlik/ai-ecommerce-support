package cz.ivosahlik.ai_ecommerce_support.repository;

import cz.ivosahlik.ai_ecommerce_support.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Customer, Long> {
    Customer findByEmailAddressAndPhoneNumber(String emailAddress, String phoneNumber);
}
