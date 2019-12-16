package msadaka.repositories;

import msadaka.models.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Employee findEmployeeByEmpEmail(String empEmail);
    Employee findEmployeesByPhone(String phone);
    Employee findEmployeeByEmpEmailContains(String email);
    List<Employee> findEmployeesByEmpNoIgnoreCaseContainingAndNameIgnoreCaseContaining(String empNo,String name);
    Employee findEmployeeByEmpNo(String empNo);
    List<Employee> findEmployeesByEmpNo(String empNo);
    Employee findEmployeeById(Integer Id);


}