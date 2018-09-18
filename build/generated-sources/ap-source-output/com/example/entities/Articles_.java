package com.example.entities;

import com.example.entities.Category;
import com.example.entities.Employees;
import com.example.entities.Subcategory;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-16T19:49:10")
@StaticMetamodel(Articles.class)
public class Articles_ { 

    public static volatile SingularAttribute<Articles, Employees> owner;
    public static volatile SingularAttribute<Articles, Subcategory> subCategory;
    public static volatile SingularAttribute<Articles, Integer> idArticle;
    public static volatile SingularAttribute<Articles, Double> price;
    public static volatile SingularAttribute<Articles, String> description;
    public static volatile SingularAttribute<Articles, Integer> inventory;
    public static volatile SingularAttribute<Articles, Category> category;

}