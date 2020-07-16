package com.smartbee.crm.auth;

import com.smartbee.crm.auth.repo.CrmPermission;
import com.smartbee.crm.auth.repo.CrmRole;
import com.smartbee.crm.auth.repo.CrmUser;
import com.smartbee.crm.auth.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtUserDetailsService.class);

    private final UserRepository userRepository;

    private String token;

    public JwtUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoginUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        final Optional<CrmUser> crmUserOpt = userRepository.findByName(name);

        if (!crmUserOpt.isPresent()) {
            LOG.debug("User {} not found", name);
            throw new UsernameNotFoundException("Username not found!");
        }

        final CrmUser user = crmUserOpt.get();
        final Set<CrmPermission> permissions =
                Optional.ofNullable(user.getRole()).map(CrmRole::getPermissions).orElse(new HashSet<>());

        return new User(user.getId().toString(), user.getPassword(), permissions);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
