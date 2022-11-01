package kr.ac.chungbuk.ShareSquare.security;

import kr.ac.chungbuk.ShareSquare.entity.User;
import kr.ac.chungbuk.ShareSquare.repository.UserRepository;
import kr.ac.chungbuk.ShareSquare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    UserRepository userRepository;

    @Autowired
    public JwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && !jwt.equals("") && JwtTokenProvider.validateToken(jwt)) {
                String username = JwtTokenProvider.getUsernameFromJwt(jwt);

                try {
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not present"));

                    UserAuthentication authentication = new UserAuthentication(username, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                catch (Exception e) {
                    request.setAttribute("unauthorization", "401 인증 중 오류");
                }
            } else {
                if (jwt != null && !jwt.equals("")) {
                    request.setAttribute("unauthorization", "401 인증키 없음.");
                }

                if (!JwtTokenProvider.validateToken(jwt)) {
                    request.setAttribute("unauthorization", "401-001 인증키 만료.");
                }
            }
        } catch (Exception e) {

        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && !bearerToken.equals("") && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
