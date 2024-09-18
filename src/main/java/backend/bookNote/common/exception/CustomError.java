package backend.bookNote.common.exception;

import org.springframework.http.HttpStatus;

public enum CustomError {

    //인증
    ACCESS_TOKEN_INVALID(1000,"유효하지 않은 인증 토큰입니다.", HttpStatus.UNAUTHORIZED.value()),
    ACCESS_TOKEN_EXPIRED(1001,"인증 토큰이 만료되었습니다",HttpStatus.UNAUTHORIZED.value()),
    REFRESH_TOKEN_INVALID(1010,"유효하지 않은 재발급 토큰입니다.",HttpStatus.UNAUTHORIZED.value()),
    REFRESH_TOKEN_EXPIRED(1011,"재발급 토큰이 만료되었습니다",HttpStatus.UNAUTHORIZED.value()),
    AUTH_TOKEN_CREATE_FAIL(1020,"토큰 발급에 실패했습니다.",HttpStatus.BAD_REQUEST.value()),
    LOGOUT_FAILED(1021, "로그아웃에 실패했습니다.", HttpStatus.BAD_REQUEST.value()),

    //회원 등록
    USER_ALREADY_EXISTS(1034, "가입한 이력이 있습니다.", HttpStatus.FORBIDDEN.value()),
    USER_DOES_NOT_EXIST(1035, "해당 사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()),

    // 책 검색
    BOOK_NOT_FOUND(2001, "검색하려는 책의 isbn 정보를 다시 확인해주세요.", HttpStatus.BAD_REQUEST.value()),
    SHARE_TOKEN_EXPIRED(2002, "열람 만료 기한이 지났습니다.", HttpStatus.FORBIDDEN.value()),
    USERBOOK_NOT_FOUND(2003, "사용자와 책 정보를 다시 확인해주세요.", HttpStatus.NOT_FOUND.value()),
    NOTE_NOT_FOUND(2004, "해당 노트는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()),
    CANNOT_DELETE_ACTIVE_NOTE(2005, "해당 노트는 삭제할 수 없습니다.", HttpStatus.FORBIDDEN.value()),

    //공통
    SERVER_ERROR(3000,"알수 없는 문제가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR.value());

    private int errorCode;
    private String message;
    private int statusCode;


    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }


    CustomError(int errorCode, String message, int statusCode) {
        this.errorCode = errorCode;
        this.message = message;
        this.statusCode = statusCode;
    }

}
