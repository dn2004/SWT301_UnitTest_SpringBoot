package SAP1801.SWT301.AnimalUnitTest.controller;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import SAP1801.SWT301.AnimalUnitTest.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalDto animalDto) {
        return new ResponseEntity<>(animalService.createAnimal(animalDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return new ResponseEntity<>("Animal deleted", HttpStatus.OK);
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<Animal>> getAnimalBySpecies(@PathVariable String species) {
        return ResponseEntity.ok(animalService.getAnimalBySpecies(species));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Long id, @RequestBody AnimalDto animalDto) {
        return ResponseEntity.ok(animalService.updateAnimal(id, animalDto));
    }
}
