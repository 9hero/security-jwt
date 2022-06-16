package com.example.security.jwt.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    UsernameOrPasswordNotFoundException (400, "아디 비번 틀림", HttpStatus.BAD_REQUEST),
    ForbiddenException(403, "해당 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZEDException (401, "로그인 후 이용가능합니다.", HttpStatus.UNAUTHORIZED),
    ExpiredJwtException(444, "기존 토큰이 만료되었습니다. 해당 토큰을 가지고 get-newtoken링크로 이동해주세요.", HttpStatus.UNAUTHORIZED),
    ReLogin(445, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.UNAUTHORIZED),
    ;

    private int code;
    private String msg;
    private HttpStatus status;

    ErrorCode(int code, String msg, HttpStatus status) {
        this.code = code;
        this.msg = msg;
        this.status = status;
    }
}
