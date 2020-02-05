package com.pollapp.repository;

import com.pollapp.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long > {
    public User findByEmail(String email);
    public User findByEmailAndPassword(String email, String password);
    //public User findByTokenToken(String token);
    //public String findPasswordByEmail(String email);

    //@Query(value = "SELECT firstname FROM user WHERE email=?1")
    //public String getFirstnameByEmail(String email);
}
