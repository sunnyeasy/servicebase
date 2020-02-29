package com.easy.common.enums.model;

public class CommonEnums {
    public enum Result {
        //待处理
        wait(0),

        //成功
        successful(1),

        //失败
        fail(-1);

        Result(int code) {
            this.code = code;
        }

        private int code;

        public int getCode() {
            return code;
        }
    }
}
