package id.my.hendisantika.testcontainers.repository;

import id.my.hendisantika.testcontainers.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : testcontainers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 01/11/24
 * Time: 09.20
 * To change this template use File | Settings | File Templates.
 */
@RepositoryRestResource(collectionResourceRel = "person", path = "person")
public interface PersonRepository extends PagingAndSortingRepository<Person, Integer>, CrudRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);
}
