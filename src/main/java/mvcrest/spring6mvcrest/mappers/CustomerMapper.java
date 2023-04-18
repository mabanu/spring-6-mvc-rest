package mvcrest.spring6mvcrest.mappers;

import mvcrest.spring6mvcrest.entities.Customer;
import mvcrest.spring6mvcrest.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDto(Customer customer);
}
