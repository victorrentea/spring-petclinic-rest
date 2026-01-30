package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetTypeApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PetTypeRepository petTypeRepository;

    ObjectMapper mapper = new ObjectMapper();

    int petTypeId;

    @BeforeEach
    final void before() {
        PetType petType = new PetType();
        petType.setName("cat");
        petTypeRepository.save(petType);
        petTypeId = petType.getId();
    }

    private PetTypeDto callGet(int petTypeId) throws Exception {
        String responseJson = mockMvc.perform(get("/api/pettypes/" + petTypeId))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(responseJson, PetTypeDto.class);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetTypeSuccessAsOwnerAdmin() throws Exception {
        PetTypeDto responseDto = callGet(petTypeId);

        assertThat(responseDto.getId()).isEqualTo(petTypeId);
        assertThat(responseDto.getName()).isEqualTo("cat");
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetPetTypeSuccessAsVetAdmin() throws Exception {
        PetTypeDto responseDto = callGet(petTypeId);

        assertThat(responseDto.getId()).isEqualTo(petTypeId);
        assertThat(responseDto.getName()).isEqualTo("cat");
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetTypeNotFound() throws Exception {
        mockMvc.perform(get("/api/pettypes/99999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllPetTypesSuccessAsOwnerAdmin() throws Exception {
        String responseJson = mockMvc.perform(get("/api/pettypes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn()
            .getResponse()
            .getContentAsString();
        PetTypeDto[] petTypes = mapper.readValue(responseJson, PetTypeDto[].class);

        assertThat(petTypes).hasSizeGreaterThanOrEqualTo(1);
        assertThat(petTypes)
            .extracting(PetTypeDto::getId, PetTypeDto::getName)
            .contains(org.assertj.core.api.Assertions.tuple(petTypeId, "cat"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetAllPetTypesSuccessAsVetAdmin() throws Exception {
        String responseJson = mockMvc.perform(get("/api/pettypes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn()
            .getResponse()
            .getContentAsString();
        PetTypeDto[] petTypes = mapper.readValue(responseJson, PetTypeDto[].class);

        assertThat(petTypes).hasSizeGreaterThanOrEqualTo(1);
        assertThat(petTypes)
            .extracting(PetTypeDto::getId, PetTypeDto::getName)
            .contains(org.assertj.core.api.Assertions.tuple(petTypeId, "cat"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testCreatePetTypeSuccess() throws Exception {
        PetTypeDto newPetType = new PetTypeDto();
        newPetType.setName("rabbit");

        String responseJson = mockMvc.perform(post("/api/pettypes")
                .content(mapper.writeValueAsString(newPetType))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PetTypeDto created = mapper.readValue(responseJson, PetTypeDto.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("rabbit");

        // Verify it was saved
        PetTypeDto retrieved = callGet(created.getId());
        assertThat(retrieved.getName()).isEqualTo("rabbit");
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testCreatePetTypeError() throws Exception {
        PetTypeDto newPetType = new PetTypeDto();
        newPetType.setName(""); // invalid - empty name

        mockMvc.perform(post("/api/pettypes")
                .content(mapper.writeValueAsString(newPetType))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdatePetTypeSuccess() throws Exception {
        PetTypeDto existing = callGet(petTypeId);
        existing.setName("cat II");

        mockMvc.perform(put("/api/pettypes/" + petTypeId)
                .content(mapper.writeValueAsString(existing))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());

        // Verify the update took place
        PetTypeDto updated = callGet(petTypeId);
        assertThat(updated.getName()).isEqualTo("cat II");
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdatePetTypeError() throws Exception {
        PetTypeDto existing = callGet(petTypeId);
        existing.setName(""); // invalid - empty name

        mockMvc.perform(put("/api/pettypes/" + petTypeId)
                .content(mapper.writeValueAsString(existing))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testDeletePetTypeSuccess() throws Exception {
        mockMvc.perform(delete("/api/pettypes/" + petTypeId))
            .andExpect(status().isNoContent());

        // Verify it was deleted
        mockMvc.perform(get("/api/pettypes/" + petTypeId))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testDeletePetTypeError() throws Exception {
        mockMvc.perform(delete("/api/pettypes/99999"))
            .andExpect(status().isNotFound());
    }
}
