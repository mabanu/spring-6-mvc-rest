package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO savedNewCustomer(CustomerDTO customerDTO);

    void updateCustomer(UUID id, CustomerDTO customerDTO);

    void patchCustomer(UUID id, CustomerDTO customerDTO);

    void deleteCustomer(UUID id);
}
