package SAP1801.SWT301.AnimalUnitTest.controller;

import SAP1801.SWT301.AnimalUnitTest.dto.AnimalDto;
import SAP1801.SWT301.AnimalUnitTest.model.Animal;
import SAP1801.SWT301.AnimalUnitTest.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnimalController.class)
@ExtendWith(MockitoExtension.class)
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
        // Giả lập service với `invocation` để kiểm tra dữ liệu đầu vào
        given(animalService.createAnimal(ArgumentMatchers.any())).willAnswer(invocation -> {
            AnimalDto dto = invocation.getArgument(0, AnimalDto.class); // Lấy tham số đầu vào
            return new Animal(1L, dto.getName(), dto.getSpecies()); // Tạo Animal với ID cố định
        });

        // Gửi request POST với AnimalDto
        ResultActions response = mockMvc.perform(post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalDto)));

        // Kiểm tra API phản hồi đúng
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name").value(CoreMatchers.is(animalDto.getName())))
                .andExpect(jsonPath("$.species").value(CoreMatchers.is(animalDto.getSpecies())));
    }



    @Test
    public void AnimalController_GetAllAnimals_ReturnAnimalList() throws Exception {
        when(animalService.getAllAnimals()).thenReturn(List.of(elephant, lion, tiger));

        ResultActions response = mockMvc.perform(get("/animals"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(elephant.getName()))
                .andExpect(jsonPath("$[0].species").value(elephant.getSpecies()))
                .andExpect(jsonPath("$[1].name").value(lion.getName()))
                .andExpect(jsonPath("$[1].species").value(lion.getSpecies()))
                .andExpect(jsonPath("$[2].name").value(tiger.getName()))
                .andExpect(jsonPath("$[2].species").value(tiger.getSpecies()));
    }

    @Test
    public void AnimalController_GetAnimalById_ReturnAnimalList() throws Exception {
        when(animalService.getAnimalById(4L)).thenReturn(tiger);

        ResultActions response = mockMvc.perform(get("/animals/4"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tiger.getName()))
                .andExpect(jsonPath("$.species").value(tiger.getSpecies()));
    }

    @Test
    public void AnimalController_DeleteAnimal_ReturnString() throws Exception {
        doNothing().when(animalService).deleteAnimal(1L);

        ResultActions response = mockMvc.perform(delete("/animals/1"));

        response.andExpect(status().isOk());
    }

    @Test
    public void AnimalController_GetAnimalBySpecies_ReturnAnimalList() throws Exception {
        when(animalService.getAnimalBySpecies("Panthera leo")).thenReturn(List.of(lion, lioness));

        ResultActions response = mockMvc.perform(get("/animals/species/Panthera leo"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(lion.getName()))
                .andExpect(jsonPath("$[0].species").value(lion.getSpecies()))
                .andExpect(jsonPath("$[1].name").value(lioness.getName()))
                .andExpect(jsonPath("$[1].species").value(lioness.getSpecies()));
    }

    @Test
    public void AnimalController_UpdateAnimal_ReturnAnimal() throws Exception {
        Animal updatedAnimal = new Animal(2L, "Updated Lion", "Updated Panthera leo");

        when(animalService.updateAnimal(eq(2L), any(AnimalDto.class))).thenReturn(updatedAnimal);

        ResultActions response = mockMvc.perform(put("/animals/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedAnimalDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedAnimalDto.getName()))
                .andExpect(jsonPath("$.species").value(updatedAnimalDto.getSpecies()));
    }
}
