package backend.bookNote.auth.service;

import backend.bookNote.auth.dto.AuthTokens;
import backend.bookNote.auth.dto.LoginDto;

public interface AuthService {

    LoginDto tokenRefresh(String refreshToken);

    void logOut(String refreshToken);
}
