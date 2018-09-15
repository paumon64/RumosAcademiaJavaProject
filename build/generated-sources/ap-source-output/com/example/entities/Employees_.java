package com.example.entities;

import com.example.entities.Articles;
import com.example.entities.Invoice;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-15T16:19:28")
@StaticMetamodel(Employees.class)
public class Employees_ { 

    public static volatile SingularAttribute<Employees, Integer> idEmployee;
    public static volatile SingularAttribute<Employees, String> firstName;
    public static volatile SingularAttribute<Employees, String> lastName;
    public static volatile CollectionAttribute<Employees, Invoice> invoiceCollection;
    public static volatile CollectionAttribute<Employees, Articles> articlesCollection;
    public static volatile SingularAttribute<Employees, String> position;

}