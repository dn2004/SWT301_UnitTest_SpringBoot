package SAP1801.SWT301.AnimalUnitTest.repository;

import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findBySpecies(String species);
}
