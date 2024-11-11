package com.ohgiraffers.section02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCRUDTests {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.
                createEntityManagerFactory("jpatest");
    }
    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
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
    public void 메뉴코드로_메뉴조회_테스트(){

        int menuCode = 1;

        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        System.out.println("foundMenu = " + foundMenu);
        Assertions.assertNotNull(foundMenu);
    }

    @Test
    public void 새로운_메뉴_추가_테스트(){

        Menu menu = new Menu();
        menu.setMenuName("jpa 테스트 메뉴");
        menu.setMenuPrice(5000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");

        // 데이터베이스의 상태 변화를 하나의 단위로 묶어주는 기능을 할 객체
        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin(); // 트랜잭션 활성화

        try {
            // 엔티티 매니저를 사용해 영속성 컨텍스트에 추가
            entityManager.persist(menu); // 메모리 단계의 저장

            entityTransaction.commit(); // db 에 명령을 넣음
        }catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }
        // 데이터가 영속성 컨텍스트에 포함되어 있는지 확인
        Assertions.assertTrue(entityManager.contains(menu));
        
        
    }

    @Test
    public void 메뉴_이름_수정_테스트(){
        Menu menu = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menu);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        
        try {
            menu.setMenuName("쿠우쿠우골드");
            entityTransaction.commit();
        }catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Assertions.assertEquals("쿠우쿠우골드",
                entityManager.find(Menu.class,2).getMenuName());
    }


    @Test
    public void 메뉴_삭제하기_테스트(){

        Menu menuToRemove = entityManager.find(Menu.class, 41);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.remove(menuToRemove);
            entityTransaction.commit();
        }catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }
        Menu removeMenu = entityManager.find(Menu.class, 41);
        Assertions.assertNull(removeMenu);
    }

}
