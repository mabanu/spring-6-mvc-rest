package mvcrest.spring6mvcrest.controller;

import mvcrest.spring6mvcrest.service.BeerService;
import mvcrest.spring6mvcrest.service.CustomerService;
import mvcrest.spring6mvcrest.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @Test
    void getListCustomers() {

    }

    @Test
    void getCustomerById() throws Exception {

        var customerTest = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(customerTest.getId())).willReturn(customerTest);

        mockMvc.perform(get("/api/v1/customers/" + customerTest.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is (customerTest.getId().toString())))
                .andExpect(jsonPath("$.customerName", is (customerTest.getCustomerName())));
    }

    @Test
    void handlePostCustomer() {

    }

    @Test
    void handlerPutCustomer() {

    }

    @Test
    void handlerPatchCustomer() {
    }

    @Test
    void handlerDeleteCustomer() {

    }
}