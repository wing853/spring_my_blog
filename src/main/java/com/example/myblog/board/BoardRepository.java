package com.example.myblog.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 1. 단건조회(게시글 ID로 조회시 사용자 정보도 함께 가져오기)
    @Query("""
            SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id
            """)
    Optional<Board> findByIdJoinUser(@Param("id") Integer id);

    // 2. 전체 게시글 조회 (작성자 정보도 조회)
    @Query("""
            SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.id DESC
            """)
    List<Board> findAllJoinUser();

    // 3. 데이터 수정은 더티 체킹으로 처리
}
