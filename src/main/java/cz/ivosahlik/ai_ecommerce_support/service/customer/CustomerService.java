package cz.ivosahlik.ai_ecommerce_support.service.customer;

import cz.ivosahlik.ai_ecommerce_support.model.Customer;
import cz.ivosahlik.ai_ecommerce_support.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmailAddress(email)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id)
                .map(customer -> getCustomer(updatedCustomer, customer))
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    private @NonNull Customer getCustomer(Customer updatedCustomer, Customer customer) {
        customer.setFullName(updatedCustomer.getFullName());
        customer.setEmailAddress(updatedCustomer.getEmailAddress());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
