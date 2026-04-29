package com.example.myblog.user;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    // 로그인
    public User login(String username, String password) {

        String jpql = """
                SELECT u 
                FROM User u 
                Where u.username = :username 
                AND u.password = :password 
                """;

        try {
            return em.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // 중복 확인
    public User findByUsername(String username) {
        String jpql = """
                SELECT u FROM User u WHERE u.username = :username
                """;
        try {
            return em.createQuery(jpql, User.class)
                    .setParameter("username",username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // 회원 가입
    @Transactional
    public User join(User user){
        em.persist(user);

        return user;
    }
}
