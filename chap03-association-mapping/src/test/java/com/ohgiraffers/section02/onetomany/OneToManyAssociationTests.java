package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class OneToManyAssociationTests {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager(){
        entityManager  = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager(){
        entityManager.close();
    }

    @Test
    void 일대다_연관관계_객체_조회_테스트(){

        int categoryCode = 10;

        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);


        Assertions.assertNotNull(categoryAndMenu);

        System.out.println(categoryAndMenu);
    }

    @Test
    void 일대다_연관관계_객체_삽입_테스트(){

        CategoryAndMenu categoryAndMenu = new CategoryAndMenu();
        categoryAndMenu.setCategoryName("일대다 카테고리 추가 테스트");
        categoryAndMenu.setRefCategoryCode(null);

        Menu menu = new Menu();
        menu.setMenuName("방어회");
        menu.setMenuPrice(50000);
        menu.setOrderableStatus("N");
        menu.setCategoryCode(categoryAndMenu);

        categoryAndMenu.getMenuList().add(menu);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(categoryAndMenu);
        entityTransaction.commit();

        CategoryAndMenu foundMenu = entityManager.find(CategoryAndMenu.class, categoryAndMenu.getCategoryCode());
        System.out.println(foundMenu);

    }
}
