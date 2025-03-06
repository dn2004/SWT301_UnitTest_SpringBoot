package SAP1801.SWT301.AnimalUnitTest.service;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.exception.AnimalNotFoundException;
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
    private Animal lion;
    private Animal tiger;
    private Animal tiger2;

    @BeforeEach
    void setUp() {
        elephant = new Animal(1L, "Elephant", "Loxodonta");
        lion = new Animal(3L, "Lion", "Panthera leo");
        tiger = new Animal(4L, "Tiger", "Panthera tigris");
        tiger2 = new Animal(5L, "Tiger2", "Panthera tigris");
    }

    @Test
    void AnimalService_CreateAnimal_ReturnAnimal() {
        //Arrange
        AnimalDto animalDto = new AnimalDto("Elephant", "Loxodonta");

        when(animalRepository.save(Mockito.any(Animal.class))).thenReturn(elephant);

        //Act
        Animal savedAnimal = animalService.createAnimal(animalDto);

        //Assert
        Assertions.assertNotNull(savedAnimal);
        Assertions.assertNotNull(savedAnimal.getId());
        Assertions.assertEquals(animalDto.getName(), savedAnimal.getName());
        Assertions.assertEquals(animalDto.getSpecies(), savedAnimal.getSpecies());
        verify(animalRepository, times(1)).save(Mockito.any(Animal.class));
    }

    @Test
    void AnimalService_GetAnimalBySpecies_ReturnAnimalList() {
        //Arrange
        String species = "Panthera tigris";
        when(animalRepository.findBySpecies(species)).thenReturn(List.of(tiger, tiger2));

        //Act
        List<Animal> animals = animalService.getAnimalBySpecies("Panthera tigris");

        //Assert
        Assertions.assertEquals(2, animals.size());
        Assertions.assertEquals("Tiger", animals.getFirst().getName());
        Assertions.assertEquals(species, animals.getFirst().getSpecies());
        Assertions.assertEquals("Tiger2", animals.get(1).getName());
        Assertions.assertEquals(species, animals.get(1).getSpecies());
        verify(animalRepository, times(1)).findBySpecies(species);
    }

    @Test
    void AnimalService_GetAllAnimals_ReturnAnimalList() {
        //Arrange
        List<Animal> expectedList = List.of(lion, tiger);
        when(animalRepository.findAll()).thenReturn(expectedList);

        //Act
        List<Animal> actualList = animalService.getAllAnimals();

        //Assert
        Assertions.assertEquals(expectedList, actualList);
        Assertions.assertEquals(2, actualList.size());
        verify(animalRepository, times(1)).findAll();
    }

    @Test
    void AnimalService_GetAnimalById_ThrowAnimalNotFoundException() {
        //Arrange
        Long notFoundAnimalId = 4L;
        when(animalRepository.findById(notFoundAnimalId)).thenReturn(Optional.empty());

        //Act & Assert
        AnimalNotFoundException exception = Assertions.assertThrows(
            AnimalNotFoundException.class, () -> animalService.getAnimalById(notFoundAnimalId)
        );

        Assertions.assertEquals("Cannot found animal from the id", exception.getMessage());
        verify(animalRepository, times(1)).findById(notFoundAnimalId);
    }

    @Test
    public void AnimalService_GetAnimalBy_Id_ReturnAnimal() {
        //Arrange
        Long animalId = 4L;
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(elephant));

        //Act
        Animal animal = animalService.getAnimalById(animalId);
        //Assert
        Assertions.assertEquals(elephant, animal);
        verify(animalRepository, times(1)).findById(animalId);
    }

    @Test
    void AnimalService_DeleteAnimal_ReturnVoid() {
        //Arrange
        Long animalId = 4L;
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(tiger));

        //Act
        Assertions.assertDoesNotThrow(() -> animalService.deleteAnimal(animalId));

        //Assert
        verify(animalRepository, times(1)).findById(animalId);
        verify(animalRepository, times(1)).delete(tiger);
    }

    @Test
    void AnimalService_UpdateAnimal_ReturnAnimal() {

        //Arrange
        Long animalId = 3L;
        AnimalDto animalDto = new AnimalDto("Updated Lion", "Updated Panthera leo");

        Animal updatedLion = new Animal(animalId, animalDto.getName(), animalDto.getSpecies());

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(lion));
        when(animalRepository.save(any(Animal.class))).thenReturn(updatedLion);

        //Act
        Animal result = animalService.updateAnimal(animalId, animalDto);

        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(animalId, result.getId());
        Assertions.assertEquals(animalDto.getName(), result.getName());
        Assertions.assertEquals(animalDto.getSpecies(), result.getSpecies());
        verify(animalRepository, times(1)).findById(animalId);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }
}
