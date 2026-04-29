package com.example.myblog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardNativeRepository {

    private final EntityManager em;

    // 게시글 목록 작성
    public List<Board> findAll() {
        String sql = """
                select * from board_tb order by id desc
                """;
        Query query = em.createNativeQuery(sql,Board.class);

        return query.getResultList();
    }

    // 게시글 작성
    @Transactional
    public void saveForm(String title, String content,String username) {
        String sql = """
                insert into board_tb(title, content, username,created_at)
                values (?,?,?,now())
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter(1,title);
        query.setParameter(2,content);
        query.setParameter(3,username);

        query.executeUpdate();
    }

    public Board findById(Integer id) {
        String sql = """
                select * from board_tb where id = ?
                """;
        try{

            Query query = em.createNativeQuery(sql, Board.class);
            query.setParameter(1,id);

            return (Board) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        String sql = """
                delete from board_tb where id = ?
                """;
        Query query = em.createNativeQuery(sql);
        query.setParameter(1,id);
        query.executeUpdate();
    }

    @Transactional
    public void updateById(Integer id, String username, String title, String content) {
        String sql = """
                update board_tb
                set username = ?, title = ?, content = ?
                where id = ?
                """;
        Query query = em.createNativeQuery(sql);
        query.setParameter(1,username);
        query.setParameter(2,title);
        query.setParameter(3,content);
        query.setParameter(4,id);

        query.executeUpdate();
    }
}
