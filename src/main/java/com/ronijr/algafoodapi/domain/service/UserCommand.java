package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.*;
import com.ronijr.algafoodapi.domain.model.User;
import com.ronijr.algafoodapi.domain.repository.UserRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserCommand {
    private final UserRepository userRepository;
    private final UserQuery userQuery;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public User create(User user) throws ValidationException {
        validate(user);
        return userRepository.save(user);
    }

    public User update(Long id, User user) throws ValidationException {
        User current = findById(id);
        BeanUtils.copyProperties(user, current, "id", "password");
        validate(current);
        return userRepository.save(current);
    }

    /**
     * Not safe, only tests. */
    public void updatePassword(Long id, String password, String newPassword) throws BusinessException {
        User user = userQuery.findByIdOrElseThrow(id);
        if (!user.passwordEquals(password)) {
            throw new BusinessException(messageSource.getMessage("password.incorrect"));
        }
        user.updatePassword(newPassword);
        userRepository.save(user);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        User user = findById(id);
        userRepository.delete(user);
        userRepository.flush();
    }

    private void validate(User user) {
        validator.validate(user);
        if (userRepository.existsByNameAndIdNotEqual(user.getName(), user.getId())) {
            throw new EntityUniqueViolationException(messageSource.getMessage("user.name.unique", user.getName()));
        }
        if (userRepository.existsByEmailAndIdNotEqual(user.getEmail(), user.getId())) {
            throw new EntityUniqueViolationException(messageSource.getMessage("user.email.unique", user.getEmail()));
        }
    }

    private User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("user.not.found", id)));
    }
}