package slomski.michal.sneakerstore.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import slomski.michal.sneakerstore.model.User;
import slomski.michal.sneakerstore.model.UserPrincipal;

public class AuthenticationSystem  {

    public static Long getLoggedInUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal customUser = (UserPrincipal) authentication.getPrincipal();
        return customUser.getId();
    }
}