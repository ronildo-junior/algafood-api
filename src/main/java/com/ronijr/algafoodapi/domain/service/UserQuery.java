package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.User;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import com.ronijr.algafoodapi.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserQuery {
    private final UserRepository userRepository;
    private final AppMessageSource messageSource;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("user.not.found", id)));
    }

    public Set<UserGroup> getUserGroupList(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);
        return user.getUserGroups();
    }

    public UserGroup getUserGroup(Long userId, Long userGroupId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return user.getUserGroup(userId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("user.group.user.not.found", userGroupId, userId)));
    }
}