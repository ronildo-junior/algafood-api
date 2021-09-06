package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.Permission;
import com.ronijr.algafoodapi.domain.repository.PermissionRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class PermissionCommand {
    private final PermissionRepository permissionRepository;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public Permission create(Permission permission) throws ValidationException {
        return update(permission);
    }

    public Permission update(Permission permission) throws ValidationException {
        validator.validate(permission);
        return permissionRepository.saveAndFlush(permission);
    }

    public void delete(Long id) throws EntityNotFoundException {
        Permission permission = findById(id);
        permissionRepository.delete(permission);
        permissionRepository.flush();
    }

    private Permission findById(Long id) throws EntityNotFoundException {
        return permissionRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("permission.not.found", id)));
    }
}