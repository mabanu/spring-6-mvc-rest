package mvcrest.spring6mvcrest.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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

    @PutMapping("{id}")
    public ResponseEntity<Customer> handlerPutCustomer(@PathVariable("id") UUID id, @RequestBody Customer customer) {

        customerService.updateCustomer(id, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Customer> handlerPatchCustomer(@PathVariable("id") UUID id, @RequestBody Customer customer) {

        customerService.patchCustomer(id, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Customer> handlerDeleteCustomer(@PathVariable("id") UUID id) {

        customerService.deleteCustomer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
