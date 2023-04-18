package mvcrest.spring6mvcrest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvcrest.spring6mvcrest.model.CustomerDTO;
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
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customers";

    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{id}";

    private final CustomerService customerService;

    @RequestMapping(value = CUSTOMER_PATH, method = RequestMethod.GET)
    public List<CustomerDTO> getListCustomers() {
        log.debug("get list customers controller method call");

        return customerService.listCustomers();
    }

    @RequestMapping(value = CUSTOMER_PATH_ID, method = RequestMethod.GET)
    public CustomerDTO getCustomerById(@PathVariable("id") UUID customerID) {
        log.debug("get customer by id controller method call");

        return customerService.getCustomerById(customerID).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> handlePostCustomer(@RequestBody CustomerDTO customerDTOPost) {

        CustomerDTO customerDTOSaved = customerService.savedNewCustomer(customerDTOPost);

        var header = new HttpHeaders();
        header.add("location", CUSTOMER_PATH + "/" + customerDTOSaved.getId().toString());

        return new ResponseEntity<>(customerDTOSaved, header, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> handlerPutCustomer(@PathVariable("id") UUID id, @RequestBody CustomerDTO customerDTO) {

        customerService.updateCustomer(id, customerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> handlerPatchCustomer(@PathVariable("id") UUID id, @RequestBody CustomerDTO customerDTO) {

        customerService.patchCustomer(id, customerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> handlerDeleteCustomer(@PathVariable("id") UUID id) {

        customerService.deleteCustomer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
