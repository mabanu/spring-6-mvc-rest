package mvcrest.spring6mvcrest.controller;

import jakarta.transaction.Transactional;
import mvcrest.spring6mvcrest.entities.Customer;
import mvcrest.spring6mvcrest.mappers.CustomerMapper;
import mvcrest.spring6mvcrest.model.CustomerDTO;
import mvcrest.spring6mvcrest.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void customerListTest() {

        var customers = customerController.getListCustomers();

        assertThat(customers).hasSize(3);
    }

    @Rollback
    @Transactional
    @Test
    void emptyCustomerListTest() {
        customerRepository.deleteAll();

        var customers = customerController.getListCustomers();

        assertThat(customers).isEmpty();
    }

    @Test
    void getByIdTest() {
        var customer = customerRepository.findAll().get(0);
        var dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void notFoundIdTest() {
        assertThrows(NotFoundException.class, () ->
                customerController.getCustomerById(UUID.randomUUID())
        );
    }

    @Rollback
    @Transactional
    @Test
    void savedCustomerTest() {
        CustomerDTO customerDto = CustomerDTO.builder()
                .customerName("new test")
                .build();

        ResponseEntity<CustomerDTO> response = customerController.handlePostCustomer(customerDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] location = response.getHeaders().getLocation().getPath().split("/");
        UUID customerCreatedId = UUID.fromString(location[4]);

        Boolean createdResponse = customerRepository.existsById(customerCreatedId);

        assertThat(createdResponse).isTrue();
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomerTest() {
        CustomerDTO customerDtoUpdate = customerMapper.customerToCustomerDto(customerRepository.findAll().get(0));
        final String nameUpdate = "NAME UPDATE TEST";
        customerDtoUpdate.setCustomerName(nameUpdate);

        ResponseEntity<CustomerDTO> response = customerController.handlerPutCustomer(customerDtoUpdate.getId(), customerDtoUpdate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        customerRepository.findById(customerDtoUpdate.getId())
                .ifPresent(customer -> assertThat(customer.getCustomerName()).isEqualTo(nameUpdate));
    }

    @Test
    void updateCustomerNotFoundTest() {

        assertThrows(NotFoundException.class, () -> customerController.handlerPutCustomer(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteCustomerTest() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity<CustomerDTO> response = customerController.handlerDeleteCustomer(customer.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void deleteCustomerNotFoundTest() {
        assertThrows(NotFoundException.class, () -> customerController.handlerDeleteCustomer(UUID.randomUUID()));
    }
}