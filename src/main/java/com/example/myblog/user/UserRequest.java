package com.example.myblog.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class LoginDTO {
        private String username;
        private String password;

        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자 이름을 입력하세요");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("패스워드를 입력하세요");
            }
        }
    }

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자 이름은 필수입니다");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("패스워드는 필수입니다");
            }

            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("이메일은 필수입니다");
            }
        }

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }
    }

}
