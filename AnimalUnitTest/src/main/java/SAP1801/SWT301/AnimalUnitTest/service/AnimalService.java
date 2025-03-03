package SAP1801.SWT301.AnimalUnitTest.service;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.model.Animal;

import java.util.List;

public interface AnimalService {
    List<Animal> getAllAnimals();
    Animal getAnimalById(Long id);
    Animal createAnimal(AnimalDto animalDto);
    void deleteAnimal(Long id);
    List<Animal> getAnimalBySpecies(String species);
    Animal updateAnimal(Long id, AnimalDto animalDto);
}