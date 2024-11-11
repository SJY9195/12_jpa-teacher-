package com.ohgiraffers.chap05springdata.menu.controller;

import com.ohgiraffers.chap05springdata.menu.dto.MenuDTO;
import com.ohgiraffers.chap05springdata.menu.entity.Menu;
import com.ohgiraffers.chap05springdata.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/select")
    public ResponseEntity selectAllMenu(){

        List<Menu> menuList = menuService.selectAllMenu();
        if (menuList != null){
            return ResponseEntity.ok(menuList);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }


    @PostMapping("insert")
    public ResponseEntity insertMenu(@RequestBody MenuDTO menuDTO){

        Object result = menuService.insertMenu(menuDTO);

        if(result instanceof Menu){
            Menu response = (Menu) result;
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(404).body(result);

    }



}
