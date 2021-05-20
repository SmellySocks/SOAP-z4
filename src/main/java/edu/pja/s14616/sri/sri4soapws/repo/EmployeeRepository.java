package edu.pja.s14616.sri.sri4soapws.repo;

import edu.pja.s14616.sri.sri4soapws.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findAll();
}
