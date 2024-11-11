package com.ohgiraffers.section01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerLifeCycleTests {

    /*
    *  엔티티 매니저 팩토리 (Entity Manager Factory)
    *  엔티티 매니저를 생성할 수 있는 기능을 제공하는 팩토리 클래스이다.
    *  요청 스코프마다 생성하기에는 (시간, 메모리) 부담이 크므로
    *  Application 당 싱글톤으로 관리하는 것이 효율적이다.
    *  따라서 데이터베이스를 사용하는 어플리케이션 당 한 개의 팩토리를 생성한다.
    *  ( database 와 커넥션을 맺은 객체)
    *
    *  엔티티 매니저 (Entity Manager)
    *  엔티티 매니저는 엔티티를 저장하는 메모리상의 데이터베이스를 관리하는 인스턴스이다.
    *  엔티티를 저장하고 수정, 삭제, 조회 하는 등의 엔티티와 관련된 모든 일을 한다.
    *  일반적으로 request scope 와 일치시킨다.
    *  ( database 에 명령을 내리기 위한 인스턴스)
    *
    *  영속성 컨텍스트 (persistence context)
    *  엔티티 매니저를 통해 엔티티를 저장하거나 조회하면 엔티티 매니저는 영속성
    *  컨텍스트에 엔티티를 보관하고 관리한다.
    *  영속성 컨텍스트는 엔티티 매니저를 생성할 때 만들어진다.
    *  그리고 엔티티 매니저를 통해서 영속성 컨텍스트에 접근할 수 있고, 관리할 수 있다.
    *  (최신화된 저장소..)
    *
    *
    *
    * */


    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll // 모든 테스트가 실행되기 전 딱 한번만 호출
    public static void initFactory(){
        entityManagerFactory = Persistence.
                createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    @AfterAll // 모든 테스트가 다 실행된후 딱 한번 호출
    public static void closeFactory(){
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager(){
        entityManager.close();
    }

    @Test
    public void 엔티티_매니저_팩토리와_엔티티_매니저_생명주기_확인1(){
        System.out.println("entityManagerFactory.hashCode = "
                + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode() = " + entityManager.hashCode());
    }
    @Test
    public void 엔티티_매니저_팩토리와_엔티티_매니저_생명주기_확인2(){
        System.out.println("entityManagerFactory.hashCode = "
                + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode() = " + entityManager.hashCode());
    }

}
