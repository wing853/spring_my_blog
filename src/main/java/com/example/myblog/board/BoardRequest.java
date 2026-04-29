package com.example.myblog.board;

import com.example.myblog.user.User;
import lombok.Builder;
import lombok.Data;

public class BoardRequest {

    @Data
    @Builder
    public static class SaveDTO {
        private String title;
        private String content;
        public Board toEntity(User sessionUser) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(sessionUser)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String username;
        private String title;
        private String content;

        public void validate() {
            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("작성자 이름은 필수입니다.");
            }

            if(title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 필수입니다.");
            }

            if(content == null || content.length() < 3) {
                throw new IllegalArgumentException("본문은 3글자 이상 작성하여야 합니다.");
            }
        }
    }
}
