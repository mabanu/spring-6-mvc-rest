package mvcrest.spring6mvcrest.service;

import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> mapCustomers;

    public CustomerServiceImpl() {
        this.mapCustomers = new HashMap<>();

        var customer1 = Customer.builder()
                .id(UUID.fromString("0d1cb520-2ec1-4ddd-a675-aecfaabde6e3"))
                .customerName("Customer1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var customer2 = Customer.builder()
                .id(UUID.fromString("69516f42-91eb-4ec4-a7d7-869c0d8a5e75"))
                .customerName("Customer2")
                .version(2)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var customer3 = Customer.builder()
                .id(UUID.fromString("69516f42-91eb-4ec4-a7d7-869c0d8a5e76"))
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
    public Optional<Customer> getCustomerById(UUID customerID) {

        log.debug("customer dy id service call");

        return Optional.of(mapCustomers.get(customerID));
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

    @Override
    public void updateCustomer(UUID id, Customer customer) {

        Customer updateCustomer = Customer.builder()
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        mapCustomers.put(updateCustomer.getId(), updateCustomer);
    }

    @Override
    public void patchCustomer(UUID id, Customer customer) {

        var customerPatch = mapCustomers.get(id);

        if (StringUtils.hasText(customer.getCustomerName())) {
            customerPatch.setCustomerName(customer.getCustomerName());
        }

        if (customer.getVersion() != null) {
            customerPatch.setVersion(customer.getVersion());
        }

        customerPatch.setLastModifiedDate(LocalDateTime.now());

        mapCustomers.put(customerPatch.getId(), customerPatch);
    }

    @Override
    public void deleteCustomer(UUID id) {

        mapCustomers.remove(id);
    }
}
