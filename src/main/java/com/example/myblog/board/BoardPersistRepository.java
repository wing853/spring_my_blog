package com.example.myblog.board;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardPersistRepository {

    private final EntityManager em;

    @Transactional
    public Board save(Board board) {
        em.persist(board);

        return board;
    }

    public List<Board> findAll(int page, int size) {
        return em.createQuery("select b from Board b order by b.id desc", Board.class)
                .setFirstResult(page * size) // 시작 인덱스
                .setMaxResults(size)         // 가져올 개수
                .getResultList();
    }

    public Long count() {
        return em.createQuery("select count(b) from Board b", Long.class)
                .getSingleResult();
    }

    public Board findById(Integer id) {
        Board board = em.find(Board.class, id);
        return board;
    }

    @Transactional
    public void deleteById(Integer id) {
        Board board = em.find(Board.class,id);

        if(board == null) {
            throw new IllegalArgumentException("삭제할 게시글을 찾을 수 없습니다");
        }

        em.remove(board);
    }

    @Transactional
    public void updateById(Integer id, BoardRequest.UpdateDTO updateDTO) {
        Board boardEntity = em.find(Board.class, id);

        if(boardEntity == null) {
            throw new IllegalArgumentException("수정할 게시물을 찾을 수 없습니다");
        }

        boardEntity.update(updateDTO);
    }
}
