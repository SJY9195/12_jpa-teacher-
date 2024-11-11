package com.ohgiraffers.chap05springdata.menu.service;

import com.ohgiraffers.chap05springdata.menu.dto.MenuDTO;
import com.ohgiraffers.chap05springdata.menu.entity.Menu;
import com.ohgiraffers.chap05springdata.menu.infra.CategoryFind;
import com.ohgiraffers.chap05springdata.menu.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired // 느슨한 결합구조를 위해 service 직접 넣지 않는다.
    private CategoryFind categoryFind;
    public List<Menu> selectAllMenu() {

        List<Menu> menuList = menuRepository.findAll();
        if(menuList.isEmpty()){
            return null;
        }
        return menuList;
    }

    public Object insertMenu(MenuDTO menuDTO) {

        // 메뉴 이름 중복 체크
        Menu menu = menuRepository.findByMenuName(menuDTO.getMenuName());

        if(!Objects.isNull(menu)){
            return menuDTO.getMenuName() + "메뉴가 존재함.";
        }

        // 가격 유효성 검사
        if(menuDTO.getMenuPrice() <= 0){
            return menuDTO.getMenuPrice() + " 가격이 잘못 입력됨";
        }

        // 카테고리 코드 검사
        Integer categoryCode = categoryFind.getCategory(menuDTO.getCategoryCode());

        if(Objects.isNull(categoryCode)){
            return menuDTO.getCategoryCode()+"는 존재하지 않습니다.";
        }

        Menu newMenu = new Menu();
        newMenu.setMenuName(menuDTO.getMenuName());
        newMenu.setMenuPrice(menuDTO.getMenuPrice());
        newMenu.setCategoryCode(menuDTO.getCategoryCode());
        newMenu.setOrderableStatus(menuDTO.getOrderableStatus());

        Menu result = menuRepository.save(newMenu);

        return result;
    }
}
