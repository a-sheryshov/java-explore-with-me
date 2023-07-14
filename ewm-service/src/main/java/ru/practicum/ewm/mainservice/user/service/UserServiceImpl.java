package ru.practicum.ewm.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.user.mapper.UserMapper;
import ru.practicum.ewm.mainservice.user.dto.UserDto;
import ru.practicum.ewm.mainservice.user.dto.UserRequestDto;
import ru.practicum.ewm.mainservice.user.model.User;
import ru.practicum.ewm.mainservice.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserRequestDto dto) {
        User user = userRepository.save(UserMapper.toUser(dto));
        log.info("User id = {} created", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> get(List<Long> ids, int from, int size) {
        List<User> users;

        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size),
                size,
                Sort.by(Sort.Direction.ASC, "id")
        );

        if (ids == null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable).toList();
        }

        return UserMapper.toUserDto(users);
    }

    @Override
    @Transactional
    public void delete(long userId) {
        userRepository.deleteById(userId);
        log.info("User id = {} deleted", userId);
    }

}
