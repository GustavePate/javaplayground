package play.ground.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import play.ground.mapstruct.dto.FromDTO;
import play.ground.mapstruct.dto.ToDTO;

@Mapper
public interface FromToMapper {

		FromToMapper INSTANCE = Mappers.getMapper(FromToMapper.class );
	
		@Mapping(source = "nom", target = "name")
		@Mapping(source = "poids", target = "weight")
	    ToDTO fromTo(FromDTO from);

	
	
}
