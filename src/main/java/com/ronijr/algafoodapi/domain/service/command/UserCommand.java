package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.*;
import com.ronijr.algafoodapi.domain.model.User;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import com.ronijr.algafoodapi.domain.repository.UserRepository;
import com.ronijr.algafoodapi.domain.service.query.UserGroupQuery;
import com.ronijr.algafoodapi.domain.service.query.UserQuery;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;
import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@AllArgsConstructor
@Transactional
public class UserCommand {
    private final UserRepository userRepository;
    private final UserQuery userQuery;
    private final UserGroupQuery userGroupQuery;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;
    private final PasswordEncoder passwordEncoder;

    public User create(User user) throws ValidationException {
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(Long id, User user) throws ValidationException {
        User current = findById(id);
        userRepository.detach(current);
        copyProperties(user, current, "id", "password");
        validate(current);
        return userRepository.save(current);
    }

    public User updatePartial(Long id, Map<String, Object> patchMap) throws ValidationException {
        User current = findById(id);
        mergeFieldsMapInObject(patchMap, current);
        validate(current);
        return userRepository.save(current);
    }

    public void updatePassword(Long id, String password, String newPassword) throws BusinessException {
        User user = userQuery.findByIdOrElseThrow(id);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(messageSource.getMessage("password.incorrect"));
        }
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        User user = findById(id);
        userRepository.delete(user);
        userRepository.flush();
    }

    public void associateUserGroup(Long userId, Long userGroupId) {
        User user = findById(userId);
        UserGroup userGroup = userGroupQuery.findByIdOrElseThrow(userGroupId);
        user.linkUserGroup(userGroup);
    }

    public void disassociateUserGroup(Long userId, Long userGroupId) {
        User user = findById(userId);
        UserGroup userGroup = userGroupQuery.findByIdOrElseThrow(userGroupId);
        user.unlinkUserGroup(userGroup);
    }

    private void validate(User user) {
        validator.validate(user);
        if (userRepository.existsByEmailAndIdNotEqual(user.getEmail(), user.getId())) {
            throw new EntityUniqueViolationException(messageSource.getMessage("user.email.unique", user.getEmail()));
        }
    }

    private User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("user.not.found", id)));
    }
}