package edu.pja.s14616.sri.sri4soapws;

import edu.pja.s14616.sri.sri4soapws.model.Customer;
import edu.pja.s14616.sri.sri4soapws.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    private final CustomerRepository customerRepository;

    public void initData(){
        Customer e1 = Customer.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .job("Manager")
                .purchases(2L)
                .companyName("Staples")
                .joinDate(LocalDate.of(2012, 05,01))
                .build();
        Customer e2 = Customer.builder()
                .firstName("Jan")
                .lastName("Nowak")
                .job("Accountant")
                .purchases(10L)
                .companyName("Amazon")
                .joinDate(LocalDate.of(2015, 01,01))
                .build();
        Customer e3 = Customer.builder()
                .firstName("Michał")
                .lastName("Hyla")
                .job("java lover")
                .purchases(2L)
                .companyName("jk")
                .joinDate(LocalDate.of(2010, 10,11))
                .build();
        customerRepository.saveAll(Arrays.asList(e1,e2,e3));
        LOG.info("Data initialized");
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {initData();}

}
