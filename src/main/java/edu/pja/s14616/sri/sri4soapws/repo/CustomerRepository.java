package edu.pja.s14616.sri.sri4soapws.repo;

import edu.pja.s14616.sri.sri4soapws.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findAll();
    List<Customer> findCustomersByCompanyName(String name);
    List<Customer> findCustomersByPurchasesGreaterThan(Long count);
}
