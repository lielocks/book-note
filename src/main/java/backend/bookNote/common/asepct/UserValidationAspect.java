package backend.bookNote.common.asepct;

import backend.bookNote.auth.model.UserCustom;
import backend.bookNote.common.exception.CustomError;
import backend.bookNote.common.exception.CustomException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * 메서드 실행 전에 사용자 접근 권한을 검증하는 Aspect
 * 해당 Aspect 는 인증된 userId 가 method arguments 로 전달된 DTO 의 userId 와 일치하는지 확인합니다.
 */
@Aspect
@Component
public class UserValidationAspect {

    @Before("@annotation(backend.bookNote.common.annotation.ValidateUserAccess)")
    public void checkUserIdMatch(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // UserCustom 객체에서 userId 추출
        UserCustom userCustom = (UserCustom) authentication.getPrincipal();
        Long authenticatedUserId = userCustom.getId();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            // HasUserId 를 implements 하는 DTO 에서 userId 추출
            if (arg instanceof HasUserId) {
                Long dtoUserId = ((HasUserId) arg).getUserId();
                if (!authenticatedUserId.equals(dtoUserId)) {
                    throw new CustomException(CustomError.ACCESS_TOKEN_INVALID);
                }
            }
        }
    }

}
