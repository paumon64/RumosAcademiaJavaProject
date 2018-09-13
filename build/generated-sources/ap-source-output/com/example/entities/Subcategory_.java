package com.example.entities;

import com.example.entities.Articles;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-13T20:54:07")
@StaticMetamodel(Subcategory.class)
public class Subcategory_ { 

    public static volatile SingularAttribute<Subcategory, String> subCategoryDescript;
    public static volatile CollectionAttribute<Subcategory, Articles> articlesCollection;
    public static volatile SingularAttribute<Subcategory, Integer> idSubCategory;
    public static volatile SingularAttribute<Subcategory, Integer> category;

}