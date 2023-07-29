package team6.sobun.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team6.sobun.domain.user.dto.SignupRequestDto;
import team6.sobun.domain.user.entity.User;
import team6.sobun.domain.user.entity.UserRoleEnum;
import team6.sobun.domain.user.repository.UserRepository;
import team6.sobun.global.exception.InvalidConditionException;
import team6.sobun.global.responseDto.ApiResponse;
import team6.sobun.global.security.repository.RefreshTokenRedisRepository;
import team6.sobun.global.stringCode.ErrorCodeEnum;
import team6.sobun.global.stringCode.SuccessCodeEnum;
import team6.sobun.global.utils.ResponseUtils;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository redisRepository;

    public ApiResponse<?> signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String nickname = signupRequestDto.getNickname();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        checkDuplicatedEmail(email);
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(email, nickname, password, role);
        userRepository.save(user);

        log.info("'{}' 이메일을 가진 사용자가 가입했습니다.", email);

        return ApiResponse.okWithMessage(SuccessCodeEnum.USER_SIGNUP_SUCCESS);
    }

    public ApiResponse<?> withdraw(User user) {
        userRepository.delete(user);
        redisRepository.deleteById(user.getEmail());

        return ApiResponse.okWithMessage(SuccessCodeEnum.USER_WITHRAW_SUCCESS);
    }

    public ApiResponse<?> logout(User user) {
        redisRepository.deleteById(user.getEmail());

        return ApiResponse.okWithMessage(SuccessCodeEnum.USER_LOGOUT_SUCCESS);
    }



    private void checkDuplicatedEmail(String email) {
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new InvalidConditionException(ErrorCodeEnum.DUPLICATE_USERNAME_EXIST);
        }
    }
}
