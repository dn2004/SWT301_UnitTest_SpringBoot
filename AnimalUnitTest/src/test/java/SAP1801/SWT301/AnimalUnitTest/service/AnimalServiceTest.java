package SAP1801.SWT301.AnimalUnitTest.service;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.repository.AnimalRepository;
import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import SAP1801.SWT301.AnimalUnitTest.service.Impl.AnimalServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalServiceImpl animalService;

    private Animal elephant;
    private Animal wolf;
    private Animal lion;
    private Animal tiger;
    private Animal tiger2;

    @BeforeEach
    void setUp() {
        elephant = new Animal(1L, "Elephant", "Loxodonta");
        wolf = new Animal(2L, "Wolf", "Canis lupus");
        lion = new Animal(3L, "Lion", "Panthera leo");
        tiger = new Animal(4L, "Tiger", "Panthera tigris");
        tiger2 = new Animal(5L, "Tiger2", "Panthera tigris");
    }

    @Test
    void AnimalService_CreateAnimal_ReturnAnimal() {
        AnimalDto animalDto = new AnimalDto("Elephant", "Loxodonta");

        when(animalRepository.save(Mockito.any(Animal.class))).thenReturn(elephant);

        Animal savedAnimal = animalService.createAnimal(animalDto);

        Assertions.assertNotNull(savedAnimal);
        Assertions.assertNotNull(savedAnimal.getId());
        Assertions.assertEquals("Elephant", savedAnimal.getName());
        Assertions.assertEquals("Loxodonta", savedAnimal.getSpecies());
    }

    @Test
    void AnimalService_GetAnimalBySpecies_ReturnAnimalList() {
        when(animalRepository.findBySpecies("Panthera tigris")).thenReturn(List.of(tiger, tiger2));
        List<Animal> animals = animalService.getAnimalBySpecies("Panthera tigris");

        Assertions.assertFalse(animals.isEmpty());
        Assertions.assertEquals("Tiger", animals.getFirst().getName());
        Assertions.assertEquals("Panthera tigris", animals.getFirst().getSpecies());
        Assertions.assertEquals("Tiger2", animals.get(1).getName());
        Assertions.assertEquals("Panthera tigris", animals.get(1).getSpecies());
    }

    @Test
    void AnimalService_GetAllAnimals_ReturnAnimalList() {
        when(animalRepository.findAll()).thenReturn(List.of(lion));

        List<Animal> animals = animalService.getAllAnimals();

        Assertions.assertFalse(animals.isEmpty());
        Assertions.assertEquals("Lion", animals.get(0).getName());
        Assertions.assertEquals("Panthera leo", animals.get(0).getSpecies());
    }

    @Test
    void AnimalService_GetAnimalById_ReturnAnimal() {
        Long animalId = 4L;

        when(animalRepository.findById(animalId)).thenReturn(Optional.ofNullable(tiger));
        Optional<Animal> animal = Optional.ofNullable(animalService.getAnimalById(animalId));

        Assertions.assertTrue(animal.isPresent());
        Assertions.assertEquals("Tiger", animal.get().getName());
        Assertions.assertEquals("Panthera tigris", animal.get().getSpecies());
    }

    @Test
    void AnimalService_DeleteAnimal_ReturnVoid() {
        Long animalId = 4L;

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(tiger));
        doNothing().when(animalRepository).delete(tiger);

        Assertions.assertAll(() -> animalService.deleteAnimal(animalId));
    }

    @Test
    void AnimalService_UpdateAnimal_ReturnAnimal() {
        Long animalId = 3L;
        AnimalDto animalDto = new AnimalDto("Updated Lion", "Updated Panthera leo");
        Animal updatedLion = new Animal(animalId, "Updated Lion", "Updated Panthera leo");

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(lion));
        when(animalRepository.save(any(Animal.class))).thenReturn(updatedLion);

        Animal result = animalService.updateAnimal(animalId, animalDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Lion", result.getName());
        Assertions.assertEquals("Updated Panthera leo", result.getSpecies());
    }
}
