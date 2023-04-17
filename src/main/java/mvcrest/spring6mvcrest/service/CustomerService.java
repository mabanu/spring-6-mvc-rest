package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Customer getCustomerById(UUID customerID);

    Customer savedNewCustomer(Customer customer);

    void updateCustomer(UUID id, Customer customer);

    void patchCustomer(UUID id, Customer customer);

    void deleteCustomer(UUID id);
}
