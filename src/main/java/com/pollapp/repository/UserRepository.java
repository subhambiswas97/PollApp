package com.pollapp.repository;

import com.pollapp.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long > {
    public User findByEmail(String email);
    public User findByEmailAndPassword(String email, String password);
    //public User findByTokenToken(String token);
    //public String findPasswordByEmail(String email);

    //@Query(value = "SELECT firstname FROM user WHERE email=?1")
    //public String getFirstnameByEmail(String email);

    @Modifying
    @Query(value = "UPDATE users set embedded_poll=?1 where id=?2",nativeQuery = true)
    public void setEmbeddedPoll(String pollId, Long userId);
}
