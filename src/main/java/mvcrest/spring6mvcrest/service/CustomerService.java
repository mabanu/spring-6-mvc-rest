package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    public List<Customer> listCustomers();
    public Customer getCustomerById(UUID customerID);

    Customer savedNewCustomer(Customer customer);
}
