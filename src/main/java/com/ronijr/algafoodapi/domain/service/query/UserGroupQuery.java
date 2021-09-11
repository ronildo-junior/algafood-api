package com.ronijr.algafoodapi.domain.service.query;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Permission;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import com.ronijr.algafoodapi.domain.repository.UserGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserGroupQuery {
    private final UserGroupRepository userGroupRepository;
    private final AppMessageSource messageSource;

    public List<UserGroup> findAll() {
        return userGroupRepository.findAll();
    }

    public Optional<UserGroup> findById(Long id) {
        return userGroupRepository.findById(id);
    }

    public UserGroup findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return userGroupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("user.group.not.found", id)));
    }

    public Permission getPermission(Long userGroupId, Long permissionId) {
        UserGroup userGroup = findByIdOrElseThrow(userGroupId);
        return userGroup.getPermission(permissionId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("permission.not.granted", permissionId)));
    }

    public Set<Permission> listPermissions(Long id) {
        UserGroup userGroup = findByIdOrElseThrow(id);
        return userGroup.getPermissions();
    }
}