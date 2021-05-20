package edu.pja.s14616.sri.sri4soapws.endpoint;

import edu.pja.s14616.sri.sri4soapws.config.SoapWSConfig;
import edu.pja.s14616.sri.sri4soapws.employees.*;
import edu.pja.s14616.sri.sri4soapws.model.Employee;
import edu.pja.s14616.sri.sri4soapws.repo.EmployeeRepository;
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
public class EmployeeEndpoint {

    private final EmployeeRepository employeeRepository;

            @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "getEmployeesRequest")
            @ResponsePayload
            public GetEmployeesResponse getEmployees(@RequestPayload GetEmployeesRequest req) {
                List<Employee> employeeList = employeeRepository.findAll();
                List<EmployeeDto> dtoList = employeeList.stream()
                        .map( this::convertToDto )
                        .collect(Collectors.toList());
                GetEmployeesResponse res = new GetEmployeesResponse();
                res.getEmployees().addAll(dtoList);
                return res;
            }
            @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "getEmployeeByIdRequest")
            @ResponsePayload
            public GetEmployeeByIdResponse getEmployeeById(@RequestPayload GetEmployeeByIdRequest req) {
                Long employeeId = req.getEmployeeId().longValue();
                Optional<Employee> emp = employeeRepository.findById(employeeId);
                GetEmployeeByIdResponse res = new GetEmployeeByIdResponse();
                res.setEmployee(convertToDto(emp.orElse(null)));
                return res;
            }
            @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "addEmployeeRequest")
            @ResponsePayload
            public AddEmployeeResponse addEmployee(@RequestPayload AddEmployeeRequest req){
                EmployeeDto empDto = req.getEmployee();
                Employee emp = convertToEntity(empDto);
                employeeRepository.save(emp);
                AddEmployeeResponse response = new AddEmployeeResponse();
                response.setEmployeeId(new BigDecimal(emp.getId()));
                return response;
            }
            private EmployeeDto convertToDto(Employee e){
                if(e == null){
                    return null;
                }
                try {
                    EmployeeDto dto = new EmployeeDto();
                    dto.setId(new BigDecimal(e.getId()));
                    dto.setFirstname(e.getFirstName());
                    dto.setLastname(e.getLastName());
                    XMLGregorianCalendar birthDate = null;
                    birthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(e.getBirthDate().toString());
                    dto.setBirthDate(birthDate);
                    dto.setJob(e.getJob());

                    return dto;
                } catch (DatatypeConfigurationException datatypeConfigurationException) {
                    datatypeConfigurationException.printStackTrace();
                    return null;
                }
            }

            private Employee convertToEntity(EmployeeDto dto) {
                return Employee.builder()
                        .id(dto.getId() != null ? dto.getId().longValue() : null)
                        .firstName(dto.getFirstname())
                        .lastName(dto.getLastname())
                        .job(dto.getJob())
                        .build();
            }


}
