package com.example.MediBook.repository;

import com.example.MediBook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;



@Repository 
public interface UserRepository extends JpaRepository<User, UUID> {
    //JpaRepository là interface trong JPA nó giúp bạn làm việc với databse mà ko cần viết SQL
    //optional là 1 class trong java dùng để tránh lỗi null
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    

}
