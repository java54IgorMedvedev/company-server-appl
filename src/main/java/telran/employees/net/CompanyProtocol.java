package telran.employees.net;

import java.util.Arrays;
import java.util.stream.Collectors;

import telran.employees.Company;
import telran.employees.Employee;
import telran.employees.Manager;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol {
    Company company;
    
    public CompanyProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request request) {
        String requestType = request.requestType();
        String requestData = request.requestData();
        Response response = null;
        try {
            response = switch(requestType) {
                case "addEmployee" -> addEmployee(requestData);
                case "getEmployee" -> getEmployee(requestData);
                case "removeEmployee" -> removeEmployee(requestData);
                case "getDepartmentBudget" -> getDepartmentBudget(requestData);
                case "getDepartments" -> getDepartments(requestData);
                case "getManagersWithMostFactor" -> getManagersWithMostFactor(requestData);
                default -> wrongTypeResponse(requestType);
            };
            
        } catch (Exception e) {
            response = wrongDataResponse(e.getMessage());
        }
        return response;
    }

    private Response wrongDataResponse(String message) {
        return new Response(ResponseCode.WRONG_REQUEST_DATA, message);
    }

    private Response wrongTypeResponse(String requestType) {
        return new Response(ResponseCode.WRONG_REQUEST_TYPE, requestType);
    }
    
    private Response getManagersWithMostFactor(String requestData) {
        Manager[] managers = company.getManagersWithMostFactor();
        String managersJSON = managersToJSON(managers);
        return new Response(ResponseCode.OK, managersJSON);
    }

    private String managersToJSON(Manager[] managers) {
        return Arrays.stream(managers)
                .map(Employee::getJSON)
                .collect(Collectors.joining(";"));
    }

    private Response getDepartments(String requestData) {
        String departmentsJSON = null;
        String[] departments = company.getDepartments();
        departmentsJSON = String.join(",", departments);
        return new Response(ResponseCode.OK, departmentsJSON);
    }

    private Response getDepartmentBudget(String requestData) {
        int budget = company.getDepartmentBudget(requestData);
        return new Response(ResponseCode.OK, String.valueOf(budget));
    }

    private Response removeEmployee(String requestData) {
        Employee removedEmployee = null;
        Response response = null;
        long id = Long.parseLong(requestData);
        removedEmployee = company.removeEmployee(id);
        if (removedEmployee == null) {
            response = new Response(ResponseCode.WRONG_REQUEST_DATA, "Employee not found");
        } else {
            response = new Response(ResponseCode.OK, removedEmployee.getJSON());
        }
        return response;
    }

    private Response getEmployee(String requestData) {
        Response response = null;
        long id = Long.parseLong(requestData);
        Employee employee = company.getEmployee(id);
        if (employee == null) {
            response = new Response(ResponseCode.WRONG_REQUEST_DATA, "Employee not found");
        } else {
            response = new Response(ResponseCode.OK, employee.getJSON());
        }
        return response;
    }

    private Response addEmployee(String emplJSON) {
        Employee empl = (Employee) new Employee().setObject(emplJSON);
        company.addEmployee(empl);
        return new Response(ResponseCode.OK, "");
    }
}
