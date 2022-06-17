package com.example.security.jwt.service;

import com.example.security.jwt.config.ErrorCode;
import com.example.security.jwt.config.JwtProvider;
import com.example.security.jwt.dto.ApiResponse;
import com.example.security.jwt.dto.AuthDTO;
import com.example.security.jwt.dto.ResponseMap;
import com.example.security.jwt.entity.RefreshToken;
import com.example.security.jwt.repo.AccRepository;
import com.example.security.jwt.repo.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final AccRepository accRepository;
    private final RefreshTokenRepository tokenRepository;


    public ApiResponse login(AuthDTO.LoginDTO loginDTO) {
        ResponseMap result = new ResponseMap();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),loginDTO.getPassword()
                    )
            );
            Map createToken = createTokenReturn(loginDTO);
            result.setResponseData("accessToken",createToken.get("accessToken"));
        }catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationException(ErrorCode.UsernameOrPasswordNotFoundException.getMsg());
        }
        return result;
    }

    public ApiResponse newAccessToken(AuthDTO.GetNewAccessTokenDTO getNewAccessTokenDTO, HttpServletRequest request) {
        try {
            ResponseMap result = new ResponseMap();
            var refreshToken = tokenRepository.findById(getNewAccessTokenDTO.getRefreshIdx()).orElseThrow(()->new Exception(""));
            // AccessToken expire , Refresh not yet
            if(jwtProvider.validateJwtToken(request,refreshToken.getRefreshToken())){
                String email = jwtProvider.getUserInfo(refreshToken.getRefreshToken());
                var loginDTO = new AuthDTO.LoginDTO();
                loginDTO.setEmail(email);

                Map createToken = createTokenReturn(loginDTO);
                result.setResponseData("accessToken",createToken.get("accessToken"));
                result.setResponseData("refreshIdx",createToken.get("refreshIdx"));
            }else {
                result.setResponseData("Error",ErrorCode.ReLogin);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String,String> createTokenReturn(AuthDTO.LoginDTO loginDTO){
        Map result = new HashMap<>();
        String accessToken = jwtProvider.createAccessToken(loginDTO);
        String refreshToken = jwtProvider.createRefreshToken(loginDTO).get("refreshToken");
        String refreshTokenExpirationAt = jwtProvider.createRefreshToken(loginDTO).get("refreshTokenExpirationAt");

        RefreshToken insertRefToken = RefreshToken.builder()
                .userEmail(loginDTO.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationAt(refreshTokenExpirationAt)
                .build();
        var rToken =tokenRepository.save(insertRefToken);
        result.put("accessToken",accessToken);
        result.put("refreshIdx",rToken.getIdx());
        return result;
    }
}
