package exploratory.mapstruct;

import static org.assertj.core.api.Assertions.assertThat;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.domain.entity.Event;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class MapStructTest {

    private ExploratoryMapper exploratoryMapper;

    @Before
    public void setup() {
        exploratoryMapper = Mappers.getMapper(ExploratoryMapper.class);
    }

    @Test
    public void testEventDomainToApi() {
        Event domainEvent = DomainEntityTestUtils.fullEvent();
        EventApiEntity expected = ApiEntityTestUtils.fullEvent();

        EventApiEntity actual = exploratoryMapper.eventToApiEntity(domainEvent);

        assertThat(actual).isEqualTo(expected);
    }
}
