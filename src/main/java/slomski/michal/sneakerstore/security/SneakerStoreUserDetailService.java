package slomski.michal.sneakerstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import slomski.michal.sneakerstore.model.User;
import slomski.michal.sneakerstore.model.UserPrincipal;
import slomski.michal.sneakerstore.repositories.UserRepository;

@Service
public class SneakerStoreUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        User user =userRepository.findUserByEmailAddress(emailAddress);

        return UserPrincipal.create(user);
    }

}
