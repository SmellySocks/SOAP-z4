package edu.pja.s14616.sri.sri4soapws.endpoint;

import edu.pja.s14616.sri.sri4soapws.config.SoapWSConfig;
import edu.pja.s14616.sri.sri4soapws.customers.*;
import edu.pja.s14616.sri.sri4soapws.model.Customer;
import edu.pja.s14616.sri.sri4soapws.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Endpoint
@RequiredArgsConstructor
public class CustomerEndpoint {

    private final CustomerRepository customerRepository;

            @PayloadRoot(namespace = SoapWSConfig.CUSTOMER_NAMESPACE, localPart = "getCustomersRequest")
            @ResponsePayload
            public GetCustomersResponse getCustomers(@RequestPayload GetCustomersRequest req) {
                List<Customer> customerList = customerRepository.findAll();
                List<CustomerDto> dtoList = customerList.stream()
                        .map( this::convertToDto )
                        .collect(Collectors.toList());
                GetCustomersResponse res = new GetCustomersResponse();
                res.getCustomers().addAll(dtoList);
                return res;
            }
    @PayloadRoot(namespace = SoapWSConfig.CUSTOMER_NAMESPACE, localPart = "getCustomersByCompanyNameRequest")
    @ResponsePayload
    public GetCustomersByCompanyNameResponse getCustomersByCompanyName(@RequestPayload GetCustomersByCompanyNameRequest req) {
                String companyName = req.getCustomerCompanyName().toString();
        List<Customer> customerList = customerRepository.findCustomersByCompanyName(companyName);
        List<CustomerDto> dtoList = customerList.stream()
                .map( this::convertToDto )
                .collect(Collectors.toList());
        GetCustomersByCompanyNameResponse res = new GetCustomersByCompanyNameResponse();
        res.getCustomer().addAll(dtoList);
        return res;
    }
            @PayloadRoot(namespace = SoapWSConfig.CUSTOMER_NAMESPACE, localPart = "getCustomerByIdRequest")
            @ResponsePayload
            public GetCustomerByIdResponse getCustomerById(@RequestPayload GetCustomerByIdRequest req) {
                Long customerId = req.getCustomerId().longValue();
                Optional<Customer> emp = customerRepository.findById(customerId);
                GetCustomerByIdResponse res = new GetCustomerByIdResponse();
                res.setCustomer(convertToDto(emp.orElse(null)));
                return res;
            }
            @PayloadRoot(namespace = SoapWSConfig.CUSTOMER_NAMESPACE, localPart = "addCustomerRequest")
            @ResponsePayload
            public AddCustomerResponse addCustomer(@RequestPayload AddCustomerRequest req){
                CustomerDto empDto = req.getCustomer();
                Customer emp = convertToEntity(empDto);
                customerRepository.save(emp);
                AddCustomerResponse response = new AddCustomerResponse();
                response.setCustomerId(new BigDecimal(emp.getId()));
                return response;
            }
            private CustomerDto convertToDto(Customer e){
                if(e == null){
                    return null;
                }
                try {
                    CustomerDto dto = new CustomerDto();
                    dto.setId(new BigDecimal(e.getId()));
                    dto.setFirstname(e.getFirstName());
                    dto.setLastname(e.getLastName());
                    dto.setCompanyName(e.getCompanyName());
                    XMLGregorianCalendar birthDate = null;
                    birthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(e.getJoinDate().toString());
                    dto.setBirthDate(birthDate);
                    dto.setPurchases(e.getPurchases());
                    dto.setJob(e.getJob());

                    return dto;
                } catch (DatatypeConfigurationException datatypeConfigurationException) {
                    datatypeConfigurationException.printStackTrace();
                    return null;
                }
            }

            private Customer convertToEntity(CustomerDto dto) {
                return Customer.builder()
                        .id(dto.getId() != null ? dto.getId().longValue() : null)
                        .firstName(dto.getFirstname())
                        .lastName(dto.getLastname())
                        .companyName(dto.getCompanyName())
                        .purchases(dto.getPurchases())
                        .job(dto.getJob())
                        .build();
            }


}
