package SAP1801.SWT301.AnimalUnitTest.repository;

import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AnimalRepositoryTest {
    @Autowired
    private AnimalRepository animalRepository;

    @Test
    void AnimalRepository_SaveAnimal_ReturnAnimal() {
        Animal animal = new Animal();
        animal.setName("Lion");
        animal.setSpecies("Panthera leo");

        Animal savedAnimal = animalRepository.save(animal);

        Assertions.assertNotNull(savedAnimal.getId());
    }

    @Test
    void AnimalRepository_FindById_ReturnAnimal() {

        Animal animal = new Animal();
        animal.setName("Elephant");
        animal.setSpecies("Loxodonta");
        Animal savedAnimal = animalRepository.save(animal);


        Optional<Animal> foundAnimal = animalRepository.findById(savedAnimal.getId());


        Assertions.assertTrue(foundAnimal.isPresent());
        Assertions.assertEquals("Elephant", foundAnimal.get().getName());
    }



    @Test
    void AnimalRepository_FindAnimal_ReturnAnimalList() {
        Animal animal = new Animal();
        animal.setName("Tiger");
        animal.setSpecies("Panthera tigris");
        animalRepository.save(animal);
        Animal animal2 = new Animal();
        animal2.setName("Lion");
        animal2.setSpecies("Panthera tigris");
        animalRepository.save(animal2);

        List<Animal> animals = animalRepository.findBySpecies("Panthera tigris");

        Assertions.assertFalse(animals.isEmpty());
        Assertions.assertTrue(animals.size() > 1);
    }
}

