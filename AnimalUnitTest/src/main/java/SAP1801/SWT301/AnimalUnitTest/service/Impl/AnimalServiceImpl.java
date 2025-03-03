package SAP1801.SWT301.AnimalUnitTest.service.Impl;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.exception.AnimalNotFoundException;
import SAP1801.SWT301.AnimalUnitTest.repository.AnimalRepository;
import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import SAP1801.SWT301.AnimalUnitTest.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    @Override
    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id).orElseThrow(() -> new AnimalNotFoundException("Cannot found animal from the id"))  ;
    }

    @Override
    public Animal createAnimal(AnimalDto animalDto) {
        Animal animal = new Animal();
        animal.setName(animalDto.getName());
        animal.setSpecies(animalDto.getSpecies());
        return animalRepository.save(animal);
    }

    @Override
    public void deleteAnimal(Long id) {
        Animal deletedAnimal = animalRepository.findById(id).orElseThrow(() -> new AnimalNotFoundException("Cannot found animal from the id"));
        animalRepository.delete(deletedAnimal);
    }

    @Override
    public List<Animal> getAnimalBySpecies(String species) {
        return animalRepository.findBySpecies(species);
    }

    public Animal updateAnimal(Long id, AnimalDto animalDto) {
        return animalRepository.findById(id).map(animal -> {
            animal.setName(animalDto.getName());
            animal.setSpecies(animalDto.getSpecies());
            return animalRepository.save(animal);
        }).orElseThrow(() -> new RuntimeException("Animal not found"));
    }
}
