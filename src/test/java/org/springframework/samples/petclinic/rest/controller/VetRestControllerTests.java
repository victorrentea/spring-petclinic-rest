package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.VetMapper;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link VetRestController}
  */
@SpringBootTest
@ContextConfiguration(classes=ApplicationTestConfig.class)
@WebAppConfiguration
class VetRestControllerTests {

    @Autowired
    private VetRestController vetRestController;

    @Autowired
    private VetMapper vetMapper;

	@MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Vet> vets;

    @BeforeEach
    void initVets(){
    	this.mockMvc = MockMvcBuilders.standaloneSetup(vetRestController)
    			.setControllerAdvice(new ExceptionControllerAdvice())
    			.build();
    	vets = new ArrayList<Vet>();


    	Vet vet = new Vet();
    	vet.setId(1);
    	vet.setFirstName("James");
    	vet.setLastName("Carter");
    	vets.add(vet);

    	vet = new Vet();
    	vet.setId(2);
    	vet.setFirstName("Helen");
    	vet.setLastName("Leary");
    	vets.add(vet);

    	vet = new Vet();
    	vet.setId(3);
    	vet.setFirstName("Linda");
    	vet.setLastName("Douglas");
    	vets.add(vet);
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetVetSuccess() throws Exception {
    	given(clinicService.findVetById(1)).willReturn(vets.get(0));
        mockMvc.perform(get("/api/vets/1")
        	.accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("James"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetVetNotFound() throws Exception {
    	given(clinicService.findVetById(999)).willReturn(null);
        mockMvc.perform(get("/api/vets/999")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllVetsSuccess() throws Exception {
    	given(clinicService.findAllVets()).willReturn(vets);
        mockMvc.perform(get("/api/vets")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].firstName").value("James"))
            .andExpect(jsonPath("$.[1].id").value(2))
            .andExpect(jsonPath("$.[1].firstName").value("Helen"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllVetsNotFound() throws Exception {
    	vets.clear();
    	given(clinicService.findAllVets()).willReturn(vets);
        mockMvc.perform(get("/api/vets")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreateVetSuccess() throws Exception {
    	Vet newVet = vets.get(0);
    	newVet.setId(999);
    	ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    	mockMvc.perform(post("/api/vets")
    		.content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreateVetError() throws Exception {
    	Vet newVet = vets.get(0);
    	newVet.setId(null);
    	newVet.setFirstName(null);
    	ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    	mockMvc.perform(post("/api/vets")
        		.content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdateVetSuccess() throws Exception {
    	given(clinicService.findVetById(1)).willReturn(vets.get(0));
    	Vet newVet = vets.get(0);
    	newVet.setFirstName("James");
    	ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    	mockMvc.perform(put("/api/vets/1")
    		.content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(content().contentType("application/json"))
        	.andExpect(status().isNoContent());

    	mockMvc.perform(get("/api/vets/1")
           	.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("James"));

    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdateVetError() throws Exception {
    	Vet newVet = vets.get(0);
    	newVet.setFirstName(null);
    	ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    	mockMvc.perform(put("/api/vets/1")
    		.content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeleteVetSuccess() throws Exception {
    	Vet newVet = vets.get(0);
    	ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    	given(clinicService.findVetById(1)).willReturn(vets.get(0));
    	mockMvc.perform(delete("/api/vets/1")
    		.content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeleteVetError() throws Exception {
    	Vet newVet = vets.get(0);
    	ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    	given(clinicService.findVetById(999)).willReturn(null);
    	mockMvc.perform(delete("/api/vets/999")
    		.content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNotFound());
    }

}
