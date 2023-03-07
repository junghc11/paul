package paul.infra;
import paul.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class CafeOrderHateoasProcessor implements RepresentationModelProcessor<EntityModel<CafeOrder>>  {

    @Override
    public EntityModel<CafeOrder> process(EntityModel<CafeOrder> model) {

        
        return model;
    }
    
}
