package cz.ivosahlik.ai_ecommerce_support.repository;

import cz.ivosahlik.ai_ecommerce_support.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmailAddress(String email);
}
