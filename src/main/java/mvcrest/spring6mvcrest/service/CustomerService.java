package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO savedNewCustomer(CustomerDTO customerDTO);

    Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO);

    Optional<CustomerDTO> patchCustomer(UUID id, CustomerDTO customerDTO);

    Boolean deleteCustomer(UUID id);
}
