package org.acme;

import com.example.petstore.Pet;
import com.example.petstore.repo.PetRepo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class PetResourceTest {

    @Inject
    PetRepo petRepo ;

    @Test
    public void testPetEndpoint() {
        given()
                .when().get("/v1/pets")
                .then()
                .statusCode(200)
             .body(hasItem(
 		            allOf(
    		                hasEntry("pet_id", "1"),
    		                hasEntry("pet_type", "Dog"),
    		                hasEntry("pet_name", "Bruno"),
    		                hasEntry("pet_age", "10")
    		            )
    		      )
    		 );
    }

    @Test
    public void testGetByPetName() {

        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("/v1/pets/search/Bru")
                .then()
                .statusCode(200).body("size", notNullValue());
    }

    @Test
    public void testChangePetType() {

        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("/v1/pets/changePetType/1/Bird")
                .then()
                .statusCode(200)
                .body("petType", is("Bird"));
    }

    @Test
    public void testDeletePetByPetType() {
        Pet pet = new Pet();
        pet.setPetName("Maco");
        pet.setPetType("Bird");
        pet.setPetAge(1);
        petRepo.save(pet);

        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("/v1/pets/deletePetByPetType/Bird")
                .then()
                .statusCode(200);
    }

    @Test
    public void testFindPetsByPetType() {

        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("/v1/pets/findPetsByPetType/Dog")
                .then()
                .statusCode(200)
                .body("size", notNullValue());
    }

    @Test
    public void testAddNewPet() {
        Pet pet = new Pet();
        pet.setPetName("Tommy");
        pet.setPetType("Dog");
        pet.setPetAge(6);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(pet)
                .when().post("/v1/pets/addNewPet")
                .then()
                .statusCode(200)
                .body("petName", is("Tommy"));
    }

    @Test
    public void testGetAllPets() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("/v1/pets/getAllPets")
                .then()
                .statusCode(200)
                .body("size", notNullValue());
    }

}