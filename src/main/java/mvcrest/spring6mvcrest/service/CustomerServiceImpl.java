package mvcrest.spring6mvcrest.service;

import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> mapCustomers;

    public CustomerServiceImpl() {
        this.mapCustomers = new HashMap<>();

        var customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Customer1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Customer2")
                .version(2)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Customer3")
                .version(3)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        mapCustomers.put(customer1.getId(), customer1);
        mapCustomers.put(customer2.getId(), customer2);
        mapCustomers.put(customer3.getId(), customer3);
    }


    @Override
    public List<Customer> listCustomers() {
        log.debug("customer list service call");

        return new ArrayList<>(mapCustomers.values());
    }

    @Override
    public Customer getCustomerById(UUID customerID) {

        log.debug("customer dy id service call");

        return mapCustomers.get(customerID);
    }

    @Override
    public Customer savedNewCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        mapCustomers.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }
}
