package mvcrest.spring6mvcrest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.Customer;
import mvcrest.spring6mvcrest.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Customer> getListCustomers(){
        log.debug("get list customers controller method call");

        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerID){
        log.debug("get customer by id controller method call");

        return customerService.getCustomerById(customerID);
    }

    @PostMapping
    public ResponseEntity<Customer> handlePostCustomer(@RequestBody Customer customerPost){

        Customer customerSaved = customerService.savedNewCustomer(customerPost);

        var header = new HttpHeaders();
        header.add("location", "/api/v1/customers/" + customerSaved.getId().toString());

        return new ResponseEntity<>(customerSaved, header, HttpStatus.CREATED);
    }


}
