package com.example.edubox.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.support.web.BaseResponseData;
import com.example.edubox.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private List<String> ignorePath;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        SecurityContextHolder.getContext().getAuthentication();
        if (ignorePath != null && ignorePath.stream().anyMatch(p -> servletPath.contains(p))) {
            chain.doFilter(req, res);
            return;
        }
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC512("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(req, res);
        } else {
            sendError(res, "Access denied");
            return;
        }

    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        BaseResponseData responseData = BaseResponseData.error(
                ErrorCode.ACCESS_DENIED.getValue(),
                message
        );

        String msg = JsonUtil.writeValueAsString(responseData);
        response.setStatus(ErrorCode.ACCESS_DENIED.getValue());
        response.setContentType("application/json");
        response.getOutputStream().write(msg.getBytes());
    }
}
