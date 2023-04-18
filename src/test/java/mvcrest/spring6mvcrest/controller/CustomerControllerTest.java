package mvcrest.spring6mvcrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mvcrest.spring6mvcrest.model.CustomerDTO;
import mvcrest.spring6mvcrest.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);

    CustomerDTO customerTest;
    ArrayList<CustomerDTO> customers;

    @BeforeEach
    void setUp() {
        customerTest = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("customer1")
                .version(1)
                .build();

        customers = new ArrayList<>();
        customers.add(customerTest);
    }

    @Test
    void getListCustomers() throws Exception {

        var listCustomerTest = customers;

        given(customerService.listCustomers()).willReturn(listCustomerTest);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));

    }

    @Test
    void getCustomerById() throws Exception {

        given(customerService.getCustomerById(customerTest.getId())).willReturn(Optional.of(customerTest));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customerTest.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customerTest.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customerTest.getCustomerName())));
    }

    @Test
    void handlePostCustomer() throws Exception {

        var customerCreated = customerTest;

        given(customerService.savedNewCustomer(any(CustomerDTO.class))).willReturn(customerCreated);

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCreated)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void handlerPutCustomer() throws Exception {

        var putCustomer = customerTest;

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, putCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putCustomer)))
                .andExpect(status().isNoContent());
    }

    @Test
    void handlerPatchCustomer() throws Exception {

        var patchCustomer = customerTest;

        var customerMap = new HashMap<String, Object>();
        customerMap.put("customerName", "new name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, patchCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomer(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(patchCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(customerMap.get("customerName"));
    }

    @Test
    void handlerDeleteCustomer() throws Exception {

        var deleteCustomer = customerTest;

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, deleteCustomer.getId()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(uuidArgumentCaptor.capture());

        assertThat(deleteCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void NotFoundExceptionTest() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}