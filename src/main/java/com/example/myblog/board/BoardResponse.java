package com.example.myblog.board;

import com.example.myblog.util.MyDateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

public class BoardResponse {

    @Data
    // 게시글 목록 응답 DTO
    public static class ListDTO {
        private Integer id;
        private String title;
        private String username;
        private String createdAt;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            if(board.getUser() != null) {
                this.username = board.getUser().getUsername();
            }

            if(board.getCreatedAt() != null) {
                this.createdAt = MyDateUtil.timestampFormat(board.getCreatedAt());
            }
        }
    }

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private String username;
        private Integer userId;

        public DetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();

            if(board.getUser() != null) {
                this.username = board.getUser().getUsername();
                this.userId = board.getUser().getId();
            }
        }
    }
}
