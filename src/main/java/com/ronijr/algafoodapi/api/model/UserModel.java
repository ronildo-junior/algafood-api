package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public final class UserModel {
    private interface Id { @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface UserEmail { @NotBlank @Email String getEmail(); }
    private interface Password { @NotBlank String getPassword(); }
    private interface NewPassword { @NotBlank String getNewPassword(); }

    @Value
    public static class Identifier implements Id  {
        Long id;
    }

    @Value
    public static class Create implements Name, UserEmail, Password {
        String name;
        String email;
        String password;
    }

    @Value
    public static class Update implements Name, UserEmail {
        String name;
        String email;
    }

    public static class UpdatePassword implements Password, NewPassword {
        final String currentPassword;
        final String newPassword;

        public UpdatePassword(String currentPassword, String newPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }

        @Override
        public String getPassword() {
            return this.currentPassword;
        }

        @Override
        public String getNewPassword() {
            return this.newPassword;
        }
    }

   @Value
    public static class Output implements Id, Name, UserEmail {
        Long id;
        String name;
        String email;
    }
}