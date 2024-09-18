package backend.bookNote.user.service;

import backend.bookNote.user.domain.User;
import backend.bookNote.auth.dto.SocialKakaoDto;
import backend.bookNote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(SocialKakaoDto.UserInfo userInfoDto) {
        User user = userInfoDto.createUser();
        return userRepository.save(user);
    }

    public User findByUserUid(String userUid) {
        return userRepository.findByUserUid(userUid);
    }

}