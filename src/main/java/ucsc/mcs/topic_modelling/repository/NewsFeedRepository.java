package ucsc.mcs.topic_modelling.repository;

import org.springframework.data.repository.CrudRepository;
import ucsc.mcs.topic_modelling.entity.NewsFeedMessage;

public interface NewsFeedRepository extends CrudRepository<NewsFeedMessage, Long>
{
}
