package team6.sobun.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team6.sobun.domain.user.dto.location.LocationRquestDto;
import team6.sobun.domain.user.service.util.LocationService;
import team6.sobun.global.jwt.JwtProvider;
import team6.sobun.global.responseDto.ApiResponse;
import team6.sobun.global.security.UserDetailsImpl;
import team6.sobun.global.security.repository.RefreshTokenRedisRepository;
@Slf4j
@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final RefreshTokenRedisRepository redisRepository;
    private final JwtProvider jwtProvider;

    @PutMapping("/auth/location")
    public ApiResponse<?> locationUpdate(@RequestBody LocationRquestDto locationRquestDto,
                                         HttpServletResponse response,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return locationService.locationUpdate(locationRquestDto, userDetails.getUser(),response);
    }

}
