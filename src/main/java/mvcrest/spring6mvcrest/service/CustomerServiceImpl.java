package mvcrest.spring6mvcrest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.mappers.CustomerMapper;
import mvcrest.spring6mvcrest.model.CustomerDTO;
import mvcrest.spring6mvcrest.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        log.debug("customer list service call");

        var customers = customerRepository.findAll();

        var customerReturn = new ArrayList<CustomerDTO>();
        for (var customer : customers) {
            customerReturn.add(customerMapper.customerToCustomerDto(customer));
        }

        return customerReturn;
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {

        log.debug("customer dy id service call");

        if (customerRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }

        var customer = customerRepository.findById(id).get();

        return Optional.of(customerMapper.customerToCustomerDto(customer));
    }

    @Override
    public CustomerDTO savedNewCustomer(CustomerDTO customerDTO) {
        var customerToSave = customerMapper.customerDtoToCustomer(customerDTO);

        var customerSaved = customerRepository.save(customerToSave);

        return customerMapper.customerToCustomerDto(customerSaved);
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO customerDTO) {

        var customerCheck = customerRepository.findById(id);

        if (customerCheck.isPresent()) {
            var customerUpdate = customerCheck.get();

            customerUpdate.setCustomerName(customerDTO.getCustomerName());
            customerUpdate.setLastModifiedDate(LocalDateTime.now());

            customerRepository.save(customerUpdate);
        }
    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO customerDTO) {

        var customerCheck = customerRepository.findById(id);

        if (customerCheck.isEmpty()) {
            return;
        }

        var customerPatch = customerCheck.get();

        if (StringUtils.hasText(customerDTO.getCustomerName())) {
            customerPatch.setCustomerName(customerDTO.getCustomerName());
        }

        if (customerDTO.getVersion() != null) {
            customerPatch.setVersion(customerDTO.getVersion());
        }

        customerPatch.setLastModifiedDate(LocalDateTime.now());

        customerRepository.save(customerPatch);
    }

    @Override
    public void deleteCustomer(UUID id) {

        customerRepository.deleteById(id);
    }
}
