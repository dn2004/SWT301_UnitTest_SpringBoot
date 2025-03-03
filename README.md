
#  Using JUnit and Mockito to test SpringBoot Project

## Project Description:
A SpringBoot project to CRUD Animal.

## Project tools:
IDE: IntelliJ

SpringBoot framework project

Test tools: JUnit, Mockito

Dependencies needed: H2

## Test Step
Test steps include 3 layer: Repository, Service and Controller

### Repository layer
1. Needed Anotation to test: 
- @DataJpaTest
- @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
2. Name Test Structure

- [Repository Name]\_[Repository Method]\_[Return Data type]

Example: AnimalRepository_SaveAnimal_ReturnAnimal() 

3. Test Steps
- Create sample data to test.
- Call the repository from test case.
- Perform assertions to check data integrity.


Example from project: 

    @Test
    void AnimalRepository_SaveAnimal_ReturnAnimal() {
            //step 1: create data to test
            Animal animal = new Animal();
            animal.setName("Lion");
            animal.setSpecies("Panthera leo");

            //step 2: call the repository method
            Animal savedAnimal = animalRepository.save(animal);
            
            //step 3: Perfrom Assertion
            Assertions.assertNotNull(savedAnimal.getId());
    }


### Service Layer

1. Needed Anotation to Test:

@ExtendWith(MockitoExtension.class)

2. Set Up Data for Each Test:
Example:
@BeforeEach
    void setUp() {
        elephant = new Animal(1L, "Elephant", "Loxodonta");
        wolf = new Animal(2L, "Wolf", "Canis lupus");
        lion = new Animal(3L, "Lion", "Panthera leo");
        tiger = new Animal(4L, "Tiger", "Panthera tigris");
        tiger2 = new Animal(5L, "Tiger2", "Panthera tigris");
    }


3. Name Test Structure
- [Service Name]\_[Service Method]\_[Return Data Type]

Example: AnimalService_CreateAnimal_ReturnAnimal()

4. Test Step
- If the method has argument, create the parameter object. 

    Example: In AnimalService has a method Animal createAnimal(AnimalDto animalDto) => Create an AnimalDto Object.

- Mock repository

- Call the service method being tested

- Validate response

Example:

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

### Controller Layer
1. Needed Anotation to Test

    @WebMvcTest(controllers = AnimalController.class)

    @ExtendWith(MockitoExtension.class)

2. Name Test Structure

- [Controller Name]\_[Controller Method]\_[Return Data Type]

3. Test Step
Step 1: Mock the service method to return expected results.

Step 2: Send an HTTP request (POST, GET, PUT, DELETE).

Step 3: Set the appropriate headers and content type.

Step 4: Validate the HTTP response status

Step 5: Verify the response body

Example: 

    @Test
    public void AnimalController_CreateAnimal_ReturnAnimal() throws Exception {
        // Step 1
        given(animalService.createAnimal(ArgumentMatchers.any())).willAnswer(invocation -> {
            AnimalDto dto = invocation.getArgument(0, AnimalDto.class);
            return new Animal(1L, dto.getName(), dto.getSpecies()); 
        });

        // Step 2
        ResultActions response = mockMvc.perform(post("/animals")
                .contentType(MediaType.APPLICATION_JSON) //Step 3
                .content(objectMapper.writeValueAsString(animalDto)));

        
        response.andExpect(status().isCreated()) // Step 4
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(1))) //Step 5
                .andExpect(jsonPath("$.name").value(CoreMatchers.is(animalDto.getName())))
                .andExpect(jsonPath("$.species").value(CoreMatchers.is(animalDto.getSpecies())));
    }


## Project Test Coverage
[link test coverage](https://www.youtube.com/watch?v=ZmKPRi8vpaA)


## Reference
[Spring Boot Unit Testing Tutorial (W/ Mockito) - Teddy Smith](https://www.youtube.com/watch?v=jqwZthuBmZY&list=PL82C6-O4XrHcg8sNwpoDDhcxUCbFy855E)





