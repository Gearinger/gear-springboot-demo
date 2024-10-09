package com.gear.sqlite.service.impl;

import com.gear.sqlite.config.CurrentUserStore;
import com.gear.sqlite.config.SystemConfig;
import com.gear.sqlite.db.TokenEntity;
import com.gear.sqlite.db.UserEntity;
import com.gear.sqlite.dto.UserDTO;
import com.gear.sqlite.dto.UserRegisterDTO;
import com.gear.sqlite.enums.RoleEnum;
import com.gear.sqlite.mapper.UserMapper;
import com.gear.sqlite.service.ITokenService;
import com.gear.sqlite.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    private final SystemConfig systemConfig;

    private final ITokenService tokenService;

    @Override
    public List<UserEntity> list() {
        return userMapper.findAll();
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userMapper.save(user);
    }


    @Override
    public UserDTO getUserByToken(String token) {
        TokenEntity tokenEntity = tokenService.getByToken(token);
        if (tokenEntity == null) {
            return null;
        }
        UserEntity userEntity = userMapper.getById(tokenEntity.getUserId());
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setAvatar(userEntity.getAvatar());
        userDTO.setRole(userEntity.getRole().getName());
        userDTO.setToken(tokenEntity.getToken());
        userDTO.setRefreshToken(tokenEntity.getRefreshToken());
        return userDTO;
    }

    @Override
    public UserDTO login(String username, String password, HttpServletRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        Optional<UserEntity> one = userMapper.findOne(Example.of(userEntity));
        if (one.isEmpty()) {
            throw new RuntimeException("用户名或密码错误");
        }
        userEntity = one.get();
        LocalDateTime now = LocalDateTime.now();
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(userEntity.getUserId());
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setRefreshToken(UUID.randomUUID().toString());
        tokenEntity.setExpireTime(now.plus(systemConfig.getExpireTime(), HOURS));
        tokenEntity.setRefreshExpireTime(now.plus(systemConfig.getRefreshExpireTime(), HOURS));
        tokenEntity.setBrowser(request.getHeader("User-Agent"));
        tokenEntity.setLoginIp(request.getRemoteAddr());
        tokenService.save(tokenEntity);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setAvatar(userEntity.getAvatar());
        userDTO.setRole(userEntity.getRole().getName());
        userDTO.setToken(tokenEntity.getToken());
        userDTO.setRefreshToken(tokenEntity.getRefreshToken());
        return userDTO;
    }

    @Override
    public UserDTO refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new RuntimeException("refreshToken为空");
        }
        LocalDateTime now = LocalDateTime.now();
        UserDTO currentUser = CurrentUserStore.getCurrentUser();
        TokenEntity tokenEntity = tokenService.getByRefreshToken(refreshToken);
        if (tokenEntity == null || tokenEntity.getRefreshExpireTime().isBefore(now)) {
            throw new RuntimeException("refreshToken无效");
        }
        tokenEntity.setUserId(currentUser.getUserId());
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setRefreshToken(UUID.randomUUID().toString());
        tokenEntity.setExpireTime(now.plus(systemConfig.getExpireTime(), HOURS));
        tokenEntity.setRefreshExpireTime(now.plus(systemConfig.getRefreshExpireTime(), HOURS));
        TokenEntity save = tokenService.save(tokenEntity);

        currentUser.setToken(save.getToken());
        currentUser.setRefreshToken(save.getRefreshToken());
        CurrentUserStore.setCurrentUser(currentUser);
        return currentUser;
    }

    @Override
    public void logout() {
        UserDTO currentUser = CurrentUserStore.getCurrentUser();
        tokenService.removeByToken(currentUser.getToken());
    }

    @Override
    public UserEntity register(UserRegisterDTO user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setNickname(user.getNickname());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setAddress(user.getAddress());
        userEntity.setRole(existAdmin() ? RoleEnum.USER : RoleEnum.ADMIN);
        userEntity.setEnable("1");
        return this.save(userEntity);
    }

    @Override
    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    private boolean existAdmin() {
        UserEntity entity = new UserEntity();
        entity.setRole(RoleEnum.ADMIN);
        return userMapper.exists(Example.of(entity));
    }
}
