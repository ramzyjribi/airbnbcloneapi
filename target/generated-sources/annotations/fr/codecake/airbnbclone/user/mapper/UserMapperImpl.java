package fr.codecake.airbnbclone.user.mapper;

import fr.codecake.airbnbclone.user.application.dto.ReadUserDTO;
import fr.codecake.airbnbclone.user.domain.Authority;
import fr.codecake.airbnbclone.user.domain.User;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-04T14:34:51+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public ReadUserDTO readUserDTOToUser(User user) {
        if ( user == null ) {
            return null;
        }

        UUID publicId = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String imageUrl = null;
        Set<String> authorities = null;

        publicId = user.getPublicId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        imageUrl = user.getImageUrl();
        authorities = authoritySetToStringSet( user.getAuthorities() );

        ReadUserDTO readUserDTO = new ReadUserDTO( publicId, firstName, lastName, email, imageUrl, authorities );

        return readUserDTO;
    }

    protected Set<String> authoritySetToStringSet(Set<Authority> set) {
        if ( set == null ) {
            return null;
        }

        Set<String> set1 = new LinkedHashSet<String>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Authority authority : set ) {
            set1.add( mapAuthoritiesToString( authority ) );
        }

        return set1;
    }
}
