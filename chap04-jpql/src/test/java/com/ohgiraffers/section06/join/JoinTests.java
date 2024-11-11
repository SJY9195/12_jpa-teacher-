package com.ohgiraffers.section06.join;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

public class JoinTests {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory =
                Persistence.createEntityManagerFactory("jpatest");
    }
    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeManager(){
        entityManager.close();
    }
    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }



    /*
    *  조인의 종류
    *  1. 일반 조인 : 일반적인 SQL 조인을 의미
    *  2. 페치 조인 : JPQL 에서 성능 최적화를 위해 제공하는 기능으로 연관 된 엔티티나 컬렉션을
    *  한 번에 조회할 수 있다.
    * */

    @Test
    void 내부조인을_이용한_조회_테스트(){

        String jpql = "SELECT m FROM menu_section06 m JOIN m.category c";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        Assertions.assertNotNull(menuList);
        for (Menu menu:menuList) {
            System.out.println(menu);
        }

    }

    @Test
    void RIGHT_JOIN_테스트(){

        String jpql = "SELECT m.menuName, c.categoryName FROM menu_section06 m " +
                " RIGHT JOIN m.category c ORDER BY m.category.categoryCode";

        List<Object[]> menuList = entityManager.createQuery(jpql,Object[].class).getResultList();

        Assertions.assertNotNull(menuList);
        for (Object[] obj:menuList) {
            for (Object obj2:obj) {
                System.out.println(obj2);
            }
        }
    }


    @Test
    void 페치조인을_이용한_조회_테스트(){

        /*
        *  페치 조인을 하면 처음 SQL 실행 후 로딩할 때 조인 결과를 다 조회한 뒤에 사용하는 방식
        *  그래서 쿼리 실행 횟수가 줄어든다.
        *  대부분의 경우 성능이 향상된다.
        * */

        String jpql = "SELECT m FROM menu_section06 m JOIN FETCH m.category c";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        Assertions.assertNotNull(menuList);
        for (Menu menu:menuList) {
            System.out.println(menu);
        }
    }


}
