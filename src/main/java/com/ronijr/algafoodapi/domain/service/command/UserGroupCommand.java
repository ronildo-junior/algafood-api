package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.Permission;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import com.ronijr.algafoodapi.domain.repository.UserGroupRepository;
import com.ronijr.algafoodapi.domain.service.query.PermissionQuery;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserGroupCommand {
    private final UserGroupRepository userGroupRepository;
    private final PermissionQuery permissionQuery;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public UserGroup create(UserGroup userGroup) throws ValidationException {
        return update(userGroup);
    }

    public UserGroup update(UserGroup userGroup) throws ValidationException {
        validator.validate(userGroup);
        try {
            return userGroupRepository.saveAndFlush(userGroup);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUniqueViolationException(messageSource.getMessage("user.group.name.unique", userGroup.getName()));
        }
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            UserGroup userGroup = findById(id);
            userGroupRepository.delete(userGroup);
            userGroupRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(messageSource.getMessage("user.group.relationship.found", id));
        }
    }

    public void grantPermission(Long userGroupId, Long permissionId) {
        UserGroup userGroup = findById(userGroupId);
        Permission permission = permissionQuery.findByIdOrElseThrow(permissionId);
        userGroup.grantPermission(permission);
    }

    public void revokePermission(Long userGroupId, Long permissionId) {
        UserGroup userGroup = findById(userGroupId);
        Permission permission = permissionQuery.findByIdOrElseThrow(permissionId);
        userGroup.revokePermission(permission);
    }

    private UserGroup findById(Long id) throws EntityNotFoundException {
        return userGroupRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("user.group.not.found", id)));
    }
}