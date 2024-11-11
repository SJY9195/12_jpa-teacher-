package com.ohgiraffers.chap05springdata.menu.repository;

import com.ohgiraffers.chap05springdata.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    Menu findByMenuName(String menuName);
}
