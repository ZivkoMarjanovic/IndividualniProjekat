package com.tus.individual.project.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.individual.project.dto.LoginRequest;
import com.tus.individual.project.dto.SignupRequest;
import com.tus.individual.project.model.*;
import com.tus.individual.project.repository.OrderRepository;
import com.tus.individual.project.repository.ProductRepository;
import com.tus.individual.project.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    AuthController authController;

    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    OrderRepository orderRepository;
    @MockBean
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"MANAGER"})
    void registerUserManagerSuccess() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("aleksandra");
//        loginRequest.setPassword("aleksandra");
        SignupRequest signupRequest = getSignupRequest("MANAGER");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        MvcResult result = mockMvc.perform(post("/signup").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"COFFEE_MAKER"})
    void registerUserCoffeeMakerSuccess() throws Exception {
        SignupRequest signupRequest = getSignupRequest("COFFEE_MAKER");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        MvcResult result = mockMvc.perform(post("/signup").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"COFFEE_MAKER"})
    void registerUserNoRoleSuccess() throws Exception {
        SignupRequest signupRequest = getSignupRequest(null);
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        MvcResult result = mockMvc.perform(post("/signup").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"COFFEE_MAKER"})
    void registerUserExistFailure() throws Exception {
        SignupRequest signupRequest = getSignupRequest("COFFEE_MAKER");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);
        MvcResult result = mockMvc.perform(post("/signup").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"COFFEE_MAKER"})
    void registerUserEmailExistFailure() throws Exception {
        SignupRequest signupRequest = getSignupRequest("COFFEE_MAKER");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);
        MvcResult result = mockMvc.perform(post("/signup").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"MANAGER"})
    void updateUserNotFound() throws Exception {
        SignupRequest signupRequest = getSignupRequest("COFFEE_MAKER");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        when(userRepository.findByUsernameAndEmail(any(), any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(put("/user").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username="alex", password = "alex",roles={"MANAGER"})
    void updateUserSuccess() throws Exception {
        SignupRequest signupRequest = getSignupRequest("COFFEE_MAKER");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        when(userRepository.findByUsernameAndEmail(signupRequest.getUsername(), signupRequest.getEmail())).thenReturn(Optional.of(getCoffeeMaker()));
        MvcResult result = mockMvc.perform(put("/user").content(signupRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<User> getUsersList() {
        List<User> users = new ArrayList<>();
        users.add(getCoffeeMaker());
        users.add(getManager());

        return users;
    }

    private User getCoffeeMaker() {
        User user = new User();
        user.setId(1L);
        user.setEmail("alex@gmail.com");
        user.setUsername("alex");
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_COFFEE_MAKER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return user;
    }

    private User getManager() {
        User user = new User();
        user.setId(2L);
        user.setEmail("vidak@gmail.com");
        user.setUsername("vidak");
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_MANAGER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return user;
    }

    private SignupRequest getSignupRequest(String role) {
        SignupRequest user = new SignupRequest();
        user.setEmail("aleksandra@gmail.com");
        user.setUsername("aleksandra");
        user.setPassword("aleksandra");
        Set<String> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        return user;
    }
}
