package com.easy.constant.enums.user.model;

public class UserEnums {
    public enum UserStatus {
        normal(1),
        lock(5),
        delete(10);

        UserStatus(int code) {
            this.code = code;
        }

        private int code;

        public int getCode() {
            return code;
        }
    }

    public enum RealNameStatus {
        no(0),
        yes(1);

        RealNameStatus(int code) {
            this.code = code;
        }

        private int code;

        public int getCode() {
            return code;
        }

    }
}
