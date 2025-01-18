package com.restaurant.gateway.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails  implements UserDetails {

    private final User user; // Esto podría ser tu entidad User, que se extrae de la base de datos.

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve los permisos o roles del usuario
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        // Devuelve la contraseña del usuario
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Devuelve el nombre de usuario (por ejemplo, el correo electrónico)
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Lógica para determinar si la cuenta está expirada
        return true; // O tu propia lógica
    }

    @Override
    public boolean isAccountNonLocked() {
        // Lógica para determinar si la cuenta está bloqueada
        return true; // O tu propia lógica
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Lógica para determinar si las credenciales han expirado
        return true; // O tu propia lógica
    }

    @Override
    public boolean isEnabled() {
        // Lógica para determinar si la cuenta está habilitada
        return user.isEnabled(); // Depende de tu entidad `User`
    }
}
