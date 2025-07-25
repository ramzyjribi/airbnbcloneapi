package fr.codecake.airbnbclone.listing.mapper;

import fr.codecake.airbnbclone.listing.application.dto.sub.PictureDTO;
import fr.codecake.airbnbclone.listing.domain.ListingPicture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T00:35:19+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class ListingPictureMapperImpl implements ListingPictureMapper {

    @Override
    public Set<ListingPicture> pictureDTOsToListingPictures(List<PictureDTO> pictureDTOs) {
        if ( pictureDTOs == null ) {
            return null;
        }

        Set<ListingPicture> set = new LinkedHashSet<ListingPicture>( Math.max( (int) ( pictureDTOs.size() / .75f ) + 1, 16 ) );
        for ( PictureDTO pictureDTO : pictureDTOs ) {
            set.add( pictureDTOToListingPicture( pictureDTO ) );
        }

        return set;
    }

    @Override
    public ListingPicture pictureDTOToListingPicture(PictureDTO pictureDTO) {
        if ( pictureDTO == null ) {
            return null;
        }

        ListingPicture listingPicture = new ListingPicture();

        listingPicture.setCover( pictureDTO.isCover() );
        byte[] file = pictureDTO.file();
        if ( file != null ) {
            listingPicture.setFile( Arrays.copyOf( file, file.length ) );
        }
        listingPicture.setFileContentType( pictureDTO.fileContentType() );

        return listingPicture;
    }

    @Override
    public List<PictureDTO> listingPictureToPictureDTO(List<ListingPicture> listingPictures) {
        if ( listingPictures == null ) {
            return null;
        }

        List<PictureDTO> list = new ArrayList<PictureDTO>( listingPictures.size() );
        for ( ListingPicture listingPicture : listingPictures ) {
            list.add( convertToPictureDTO( listingPicture ) );
        }

        return list;
    }

    @Override
    public PictureDTO convertToPictureDTO(ListingPicture listingPicture) {
        if ( listingPicture == null ) {
            return null;
        }

        boolean isCover = false;
        byte[] file = null;
        String fileContentType = null;

        isCover = listingPicture.isCover();
        byte[] file1 = listingPicture.getFile();
        if ( file1 != null ) {
            file = Arrays.copyOf( file1, file1.length );
        }
        fileContentType = listingPicture.getFileContentType();

        PictureDTO pictureDTO = new PictureDTO( file, fileContentType, isCover );

        return pictureDTO;
    }
}
