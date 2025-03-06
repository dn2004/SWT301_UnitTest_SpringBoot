package SAP1801.SWT301.AnimalUnitTest.controller;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.exception.AnimalNotFoundException;
import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import SAP1801.SWT301.AnimalUnitTest.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Autowired
    private ObjectMapper objectMapper;

    private Animal elephant;
    private Animal lion;
    private Animal lioness;
    private Animal tiger;
    private AnimalDto animalDto;
    private AnimalDto updatedAnimalDto;

    @BeforeEach
    public void setUp() {
        elephant = new Animal(1L, "Elephant", "Loxodonta");
        lion = new Animal(2L, "Lion", "Panthera leo");
        lioness = new Animal(3L, "Lioness", "Panthera leo");
        tiger = new Animal(4L, "Tiger", "Panthera tigris");

        animalDto = new AnimalDto("Elephant", "Loxodonta");
        updatedAnimalDto = new AnimalDto("Updated Lion", "Updated Panthera leo");
    }


    @Test
    public void AnimalController_CreateAnimal_ReturnAnimal() throws Exception {
        // Arrange
        given(animalService.createAnimal(ArgumentMatchers.any())).willAnswer(invocation -> {
            AnimalDto dto = invocation.getArgument(0, AnimalDto.class);
            return new Animal(1L, dto.getName(), dto.getSpecies());
        });

        // Act
        ResultActions response = mockMvc.perform(post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalDto)));

        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name").value(CoreMatchers.is(animalDto.getName())))
                .andExpect(jsonPath("$.species").value(CoreMatchers.is(animalDto.getSpecies())));
        verify(animalService, times(1)).createAnimal(ArgumentMatchers.any());
    }



    @Test
    public void AnimalController_GetAllAnimals_ReturnAnimalList() throws Exception {
        //Arrange
        when(animalService.getAllAnimals()).thenReturn(List.of(elephant, lion, tiger));

        //Act
        ResultActions response = mockMvc.perform(get("/animals"));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(elephant.getName()))
                .andExpect(jsonPath("$[0].species").value(elephant.getSpecies()))
                .andExpect(jsonPath("$[1].name").value(lion.getName()))
                .andExpect(jsonPath("$[1].species").value(lion.getSpecies()))
                .andExpect(jsonPath("$[2].name").value(tiger.getName()))
                .andExpect(jsonPath("$[2].species").value(tiger.getSpecies()));
        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    public void AnimalController_GetAnimalById_ReturnAnimalList() throws Exception {
        //Arrange
        Long animalId = 4L;
        when(animalService.getAnimalById(animalId)).thenReturn(tiger);

        //Act
        ResultActions response = mockMvc.perform(get("/animals/4"));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tiger.getName()))
                .andExpect(jsonPath("$.species").value(tiger.getSpecies()));
        verify(animalService, times(1)).getAnimalById(animalId);
    }

    @Test
    public void AnimalController_DeleteAnimal_ReturnString() throws Exception {
        //Arrange
        Long animalId = 1L;
        doNothing().when(animalService).deleteAnimal(animalId);

        //Act
        ResultActions response = mockMvc.perform(delete("/animals/1"));

        //Assert
        response.andExpect(status().isOk());
        verify(animalService).deleteAnimal(animalId);
    }

    @Test
    public void AnimalController_DeleteAnimal_ReturnNotFound() throws Exception {
        // Arrange
        Long animalId = 100L;
        doThrow(new AnimalNotFoundException("Animal not found with ID: " + animalId))
                .when(animalService).deleteAnimal(animalId);

        // Act
        ResultActions response = mockMvc.perform(delete("/animals/100"));

        // Assert
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Animal not found with ID: " + animalId));

        verify(animalService).deleteAnimal(animalId);
    }




    @Test
    public void AnimalController_GetAnimalBySpecies_ReturnAnimalList() throws Exception {
        //Arrange
        String species = "Panthera leo";

        when(animalService.getAnimalBySpecies(species)).thenReturn(List.of(lion, lioness));

        //Act
        ResultActions response = mockMvc.perform(get("/animals/species/"+species));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(lion.getName()))
                .andExpect(jsonPath("$[0].species").value(lion.getSpecies()))
                .andExpect(jsonPath("$[1].name").value(lioness.getName()))
                .andExpect(jsonPath("$[1].species").value(lioness.getSpecies()));
        verify(animalService, times(1)).getAnimalBySpecies(species);
    }

    @Test
    public void AnimalController_UpdateAnimal_ReturnAnimal() throws Exception {
        //Arrange
        Animal updatedAnimal = new Animal(2L, "Updated Lion", "Updated Panthera leo");

        when(animalService.updateAnimal(eq(2L), any(AnimalDto.class))).thenReturn(updatedAnimal);

        //Act
        ResultActions response = mockMvc.perform(put("/animals/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedAnimalDto)));
        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(2)))
                .andExpect(jsonPath("$.name").value(updatedAnimalDto.getName()))
                .andExpect(jsonPath("$.species").value(updatedAnimalDto.getSpecies()));
        verify(animalService, times(1)).updateAnimal(eq(2L), any(AnimalDto.class));
    }
}
