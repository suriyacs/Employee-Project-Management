package controller;

import java.io.IOException;
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import databaseexception.DatabaseException;
import service.EmployeeService;
import model.Employee;
import util.StringUtil;

public class EmployeeController extends HttpServlet {
    EmployeeService employeeService = new EmployeeService();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        int projectId = 0;

        try {
           if (employeeService.addEmployeeDetails(request.getParameter("employeename"), Integer.parseInt(request.getParameter("employeeid")), 
                  Integer.parseInt(request.getParameter("employeesalary")), Integer.parseInt(request.getParameter("employeeage")), 
                  projectId,request.getParameter("employeerole"),request.getParameter("password"))) {
                      request.setAttribute("InsertMessage","Addeded Successfully");
           } else {
               request.setAttribute("InsertMessage","Can not Add!!Please check the input and try again!!");
           }
        } catch(DatabaseException e) {
             request.setAttribute("InsertMessage",e);
        } catch(NumberFormatException e) {
             request.setAttribute("InsertMessage",e);
        }     
        finally {
             request.getRequestDispatcher("insertemployee.jsp").forward(request,response);
        }     
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("deleteEmployee") != null) {
            deleteEmployee(request,response);
         }
        if (request.getParameter("searchEmployee") != null) {
            retrieveEmployee(request,response);
        }
    }
    
    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        try {                  
            if (employeeService.checkIfEmployeeExist(Integer.parseInt(request.getParameter("delete")))) {
                employeeService.deleteEmployee(Integer.parseInt(request.getParameter("delete")));
                request.setAttribute("DeleteMessage","Deleted Successfully");
            } 
            request.getRequestDispatcher("employeeoperations.jsp").forward(request,response);      
         } catch(DatabaseException e) {
                request.setAttribute("DeleteMessage",e);
                request.getRequestDispatcher("employeeoperations.jsp").forward(request,response); 
         }  
     }
    
    private void retrieveEmployee(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        try { 
            if (request.getParameter("search") != null) { 
                if (request.getParameter("search").equals("all")) {               
                    List <Employee> employees = employeeService.getAllEmployees();  
                    request.setAttribute("employeesList",employees); 
                } else {
                    Employee employee = employeeService.getEmployeeById(Integer.parseInt(request.getParameter("search")));
                    request.setAttribute("employee",employee);
                }
                request.getRequestDispatcher("employeeoperations.jsp").forward(request,response);
            }
            if (request.getParameter("view") != null) {
                Employee employee = employeeService.getEmployeeById(Integer.parseInt(request.getParameter("view")));
                request.setAttribute("employee",employee);
                request.getRequestDispatcher("viewemployee.jsp").forward(request,response);
            }
        } catch(DatabaseException e) {
            request.setAttribute("employee",e);
            request.getRequestDispatcher("employeeoperations.jsp").forward(request,response);
        }  
    }                     
                     

}
