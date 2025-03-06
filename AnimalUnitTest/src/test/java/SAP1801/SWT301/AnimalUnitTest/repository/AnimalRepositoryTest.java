package SAP1801.SWT301.AnimalUnitTest.repository;

import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;


@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AnimalRepositoryTest {
    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalRepositoryTest(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Test
    void AnimalRepository_SaveAnimal_ReturnAnimal() {
        //Arrange
        Animal animal = new Animal();
        animal.setName("Lion");
        animal.setSpecies("Panthera leo");

        //Act
        Animal savedAnimal = animalRepository.save(animal);

        //Assert
        Assertions.assertNotNull(savedAnimal.getId());
    }

    @Test
    void AnimalRepository_SaveAnimal_ThrowException() {
        //Arrange
        Animal animal = new Animal();
        animal.setName(null);
        animal.setSpecies(null);

        //Act & Assert
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> animalRepository.save(animal));
    }

    @Test
    void AnimalRepository_FindById_ReturnAnimal() {
        //Arrange
        Animal animal = new Animal();
        animal.setName("Elephant");
        animal.setSpecies("Loxodonta");
        Animal savedAnimal = animalRepository.save(animal);

        //Act
        Optional<Animal> foundAnimal = animalRepository.findById(savedAnimal.getId());

        //Assert
        Assertions.assertTrue(foundAnimal.isPresent());
        Assertions.assertEquals("Elephant", foundAnimal.get().getName());
    }



    @Test
    void AnimalRepository_FindBySpecies_ReturnAnimalList() {
        //Arrange
        Animal animal = new Animal();
        animal.setName("Tiger");
        animal.setSpecies("Panthera tigris");
        animalRepository.save(animal);
        Animal animal2 = new Animal();
        animal2.setName("Lion");
        animal2.setSpecies("Panthera tigris");
        animalRepository.save(animal2);

        //Act
        List<Animal> animals = animalRepository.findBySpecies("Panthera tigris");

        //Assert
        Assertions.assertFalse(animals.isEmpty());
        Assertions.assertTrue(animals.size() > 1);
    }
}

