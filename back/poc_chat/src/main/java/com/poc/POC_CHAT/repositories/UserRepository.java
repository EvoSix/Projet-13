package com.poc.POC_CHAT.repositories;

import com.poc.POC_CHAT.models.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<user, Long> {
    Optional<user> findByEmail(String email);




}
