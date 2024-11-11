package com.ohgiraffers.chap06securityjwt.user.repository;

import com.ohgiraffers.chap06securityjwt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findByUserId(String username);
}
