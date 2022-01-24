package guru.springframework.repositiories;

import guru.springframework.domain.UnitOfMeasure;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    private UnitOfMeasureRepository repository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findByDescription() {
        Optional<UnitOfMeasure> uom = repository.findByDescription("Teaspoon");
        assertThat(uom.get().getDescription(), Matchers.equalTo("Teaspoon"));
    }

    @Test
    public void findByDescriptionCup() {
        Optional<UnitOfMeasure> cup = repository.findByDescription("Cup");
        assertThat(cup.get().getDescription(), Matchers.equalTo("Cup"));
    }
}