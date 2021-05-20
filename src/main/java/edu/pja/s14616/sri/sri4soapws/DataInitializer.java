package edu.pja.s14616.sri.sri4soapws;

import edu.pja.s14616.sri.sri4soapws.model.Employee;
import edu.pja.s14616.sri.sri4soapws.repo.EmployeeRepository;
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

    private final EmployeeRepository employeeRepository;

    public void initData(){
        Employee e1 = Employee.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .job("Clerk")
                .birthDate(LocalDate.of(1994, 01,01))
                .build();
        Employee e2 = Employee.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .job("Clerk")
                .birthDate(LocalDate.of(1994, 01,01))
                .build();
        Employee e3 = Employee.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .job("Clerk")
                .birthDate(LocalDate.of(1994, 01,01))
                .build();
        employeeRepository.saveAll(Arrays.asList(e1,e2,e3));
        LOG.info("Data initialized");
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {initData();}

}
