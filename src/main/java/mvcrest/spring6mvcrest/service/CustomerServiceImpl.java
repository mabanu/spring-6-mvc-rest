package mvcrest.spring6mvcrest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.mappers.CustomerMapper;
import mvcrest.spring6mvcrest.model.CustomerDTO;
import mvcrest.spring6mvcrest.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        log.debug("customer list service call");

        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {

        log.debug("customer dy id service call");

        return Optional.ofNullable(customerMapper
                .customerToCustomerDto(customerRepository
                        .findById(id)
                        .orElse(null)));
    }

    @Override
    public CustomerDTO savedNewCustomer(CustomerDTO customerDTO) {

        return customerMapper.customerToCustomerDto(customerRepository.save(
                customerMapper.customerDtoToCustomer(customerDTO)));

    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO) {

        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresentOrElse( customerUpdate -> {
            customerUpdate.setCustomerName(customerDTO.getCustomerName());
            customerUpdate.setLastModifiedDate(LocalDateTime.now());

            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customerUpdate))));

        }, () -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID id, CustomerDTO customerDTO) {

        var customerCheck = customerRepository.findById(id);

        if (customerCheck.isEmpty()) {
            return Optional.empty();
        }

        var customerPatch = customerCheck.get();

        if (StringUtils.hasText(customerDTO.getCustomerName())) {
            customerPatch.setCustomerName(customerDTO.getCustomerName());
        }

        if (customerDTO.getVersion() != null) {
            customerPatch.setVersion(customerDTO.getVersion());
        }

        customerPatch.setLastModifiedDate(LocalDateTime.now());

        return Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customerPatch)));
    }

    @Override
    public Boolean deleteCustomer(UUID id) {

        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
