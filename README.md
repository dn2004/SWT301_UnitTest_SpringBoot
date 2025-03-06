
#  Using JUnit and Mockito to test SpringBoot Project

## Project Description:
A SpringBoot project to CRUD Animal.

## Project tools:

IDE: IntelliJ

SpringBoot framework project

Test tools: JUnit, Mockito (from the spring-boot-starter-test in SpringBoot framework)

Dependencies needed: H2, ObjectMapper, JPA, Lombok

## Test Step
Test steps include 3 layer: Repository, Service and Controller

### Repository layer
1. Needed Anotation to test: 

@DataJpaTest: used to test JPA repositories in Spring Boot applications. This annotation sets up an in-memory H2 database and configure Spring Data JPA for us.


2. Name Test Structure

- [Repository Name]\_[Repository Method]\_[Return Data type]

Example: AnimalRepository_SaveAnimal_ReturnAnimal() 

3. Test Steps

Step 1: Create sample data to test. (Arrange)

Step 2: Call the repository from test case. (Act)

Step 3: Perform assertions to check data integrity. (Assert)


Example from project: 

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



### Service Layer

1. Needed Anotation to Test:

@ExtendWith(MockitoExtension.class): Enables Mockito for JUnit

@Mock: create a mock object (fake repository)

@InjectMocks: inject the fake repository into the service


2. Set Up Data for Each Test:

Example:

    @BeforeEach //run before each test case 
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

Step 1: If the method has argument, create the parameter object. 

- Example: In AnimalService has a method Animal createAnimal(AnimalDto animalDto) => Create an AnimalDto Object.

Step 2: Mock repository

Step 3: Call the service method being tested

Step 4: Validate response

Example:


    @Test
    void AnimalService_CreateAnimal_ReturnAnimal() {
        //step 1
        AnimalDto animalDto = new AnimalDto("Elephant", "Loxodonta");

        //step 2
        when(animalRepository.save(Mockito.any(Animal.class))).thenReturn(elephant);

        //step 3
        Animal savedAnimal = animalService.createAnimal(animalDto);

        //step 4
        Assertions.assertNotNull(savedAnimal);
        Assertions.assertNotNull(savedAnimal.getId());
        Assertions.assertEquals("Elephant", savedAnimal.getName());
        Assertions.assertEquals("Loxodonta", savedAnimal.getSpecies());
    }

### Controller Layer
1. Needed Anotation to Test

    @WebMvcTest(controllers = AnimalController.class): Loads only the animal controller being tested	
    
    @ExtendWith(MockitoExtension.class): Enables Mockito for JUnit

    @MockBean: replace actual service layer

2. Name Test Structure

- [Controller Name]\_[Controller Method]\_[Return Data Type]

3. Test Step

Step 1: Mock the service method to return expected results.(Arrange)

Step 2: Send an HTTP request (POST, GET, PUT, DELETE). (Act)

Step 3: Set the appropriate headers and content type. (Act)

Step 4: Validate the HTTP response status (Assert)

Step 5: Verify the response body (Assert)

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


## Coverage Information: 

| Component/Hook | Coverage by   | Coverage Percent |
|----------------|---------------|:----------------:|
| TodoForm       | TodoForm.test |        100       |
| TodoList       | TodoList.test |        100       |
| TodoItem       | TodoList.Test |        100       |
| useTodo        | useTodo.Test  |        100       |

## Project Test Video
[Link youtube](https://youtu.be/qakT4vKB2NU)

## Reference
[Spring Boot Unit Testing Tutorial (W/ Mockito) - Teddy Smith](https://www.youtube.com/watch?v=jqwZthuBmZY&list=PL82C6-O4XrHcg8sNwpoDDhcxUCbFy855E)





