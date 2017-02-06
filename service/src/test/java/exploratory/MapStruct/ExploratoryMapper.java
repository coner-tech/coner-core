package exploratory.MapStruct;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.domain.entity.Event;
import org.mapstruct.Mapper;

@Mapper
public interface ExploratoryMapper {

    EventApiEntity eventToApiEntity(Event event);
}
