/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.ArticlesFacade;
import com.example.ejb.EmployeesFacade;
import com.example.entities.Articles;
import com.example.entities.Employees;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author paumon64
 */
@Named(value = "employeesController")
@SessionScoped

public class EmployeesController implements Serializable {
    @EJB
    EmployeesFacade employeesFacade;

    @Inject
    EmployeesBean employeesBean;
    
    /**
     * Creates a new instance of EmployeesController
     */
    
    public EmployeesController() {
    }
    
    public List<Employees> getAll() {

        return employeesFacade.findAll();
    }

    public int count() {

        return employeesFacade.count();
    }

    public String delete(Employees x) {

        employeesFacade.remove(x);

        return null;

    }

    public String add() {

        Employees y = new Employees();
        //y.setId(Integer.SIZE);
        y.setFirstName(employeesBean.getFirstName());
        y.setLastName(employeesBean.getLastName());
        y.setPosition(employeesBean.getPosition());

        employeesFacade.create(y);

        return "index";
    }

    public String edit(Employees i) {
        employeesBean.setIdEmployee(i.getIdEmployee());
        employeesBean.setFirstName(i.getFirstName());
        employeesBean.setLastName(i.getLastName());
        employeesBean.setPosition(i.getPosition());

        return "update_employee";
    }

    public String save(){
        
       Employees i = new Employees(employeesBean.getIdEmployee());
       
       i.setFirstName(employeesBean.getFirstName());
       i.setLastName(employeesBean.getLastName());
       i.setPosition(employeesBean.getPosition());

       employeesFacade.edit(i);
       
       return "index";
       

   }
}
