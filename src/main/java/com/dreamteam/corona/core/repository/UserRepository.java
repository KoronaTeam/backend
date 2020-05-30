package com.dreamteam.corona.core.repository;

import com.dreamteam.corona.core.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username) throws DataAccessException;

    List<User> findAll() throws DataAccessException;

    Optional<User> findById(Long id) throws DataAccessException;

    Optional<User> findByEmail(String email) throws DataAccessException;

    User save(User user) throws DataAccessException;

    void delete(User user) throws DataAccessException;


}
