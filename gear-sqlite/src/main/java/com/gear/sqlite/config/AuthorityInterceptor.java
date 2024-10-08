package com.gear.sqlite.config;

import com.gear.sqlite.db.TokenEntity;
import com.gear.sqlite.dto.UserDTO;
import com.gear.sqlite.service.ITokenService;
import com.gear.sqlite.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorityInterceptor implements HandlerInterceptor {

    private final IUserService userService;

    private final ITokenService tokenService;

    private final SystemConfig systemConfig;

    /**
     * 请求执行前执行的，将用户信息放入ThreadLocal
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!systemConfig.getEnableAuth()) {
            return true;
        }
        String token = extractTokenFromRequest(request);
        UserDTO userDTO = userService.getUserByToken(token);
        if (userDTO == null) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(701);
            response.getOutputStream().write("Token已失效，请重新登录".getBytes());
            return false;
        }
        if (!userDTO.getEnable()) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(702);
            response.getOutputStream().write("用户已禁用，请联系管理员".getBytes());
            return false;
        }
        TokenEntity tokenEntity = tokenService.getByToken(token);
        if (tokenEntity == null || tokenEntity.getExpireTime() == null || tokenEntity.getExpireTime().isBefore(LocalDateTime.now())) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(704);
            response.getOutputStream().write("Token已失效，请重新登录".getBytes());
            return false;
        }
        CurrentUserStore.setCurrentUser(userDTO);
        return true;
    }

    /**
     * 从请求中提取令牌
     *
     * @param request 请求
     * @return {@link String}
     */
    private static String extractTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization == null ? request.getParameter("token") : authorization;
    }

    /**
     * 接口访问结束后，从ThreadLocal中删除用户信息
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrentUserStore.remove();
    }
}
