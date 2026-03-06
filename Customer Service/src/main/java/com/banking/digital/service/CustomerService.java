package com.banking.digital.service;

import com.banking.digital.common.ApiResponse;
import com.banking.digital.common.ConstantMessages;
import com.banking.digital.dto.CustomerRequest;
import com.banking.digital.dto.CustomerResponse;
import com.banking.digital.entity.Customer;
import com.banking.digital.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public ResponseEntity<ApiResponse<CustomerResponse>> create(CustomerRequest request) {

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>(new ApiResponse<CustomerResponse>(HttpStatus.CONTINUE.value(), ConstantMessages.emailIDAlreadyExists,null),HttpStatus.ALREADY_REPORTED);
        }
        if (repository.findByMobile(request.getMobile()).isPresent()) {
            return new ResponseEntity<>(new ApiResponse<CustomerResponse>(HttpStatus.CONTINUE.value(), ConstantMessages.phoneNumberAlreadyExists,null),HttpStatus.ALREADY_REPORTED);
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setAddress(request.getAddress());

        repository.save(customer);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.customerCreatedSuccessfully,new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getMobile(),
                customer.getAddress()
        )),HttpStatus.ACCEPTED);
    }
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(Long id) {
        try {
            Customer customer = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException(ConstantMessages.customerNotFound));
            return new ResponseEntity<>(new ApiResponse<CustomerResponse>(HttpStatus.ACCEPTED.value(), ConstantMessages.customerDetails,new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getMobile(),
                    customer.getAddress()
            )),HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            return  new ResponseEntity<>(new ApiResponse<CustomerResponse>(HttpStatus.NOT_FOUND.value(), ConstantMessages.customerNotFound,null),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return  new ResponseEntity<>(new ApiResponse<CustomerResponse>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.internalServerError,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {

       return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED.value(), ConstantMessages.customerDetails,repository.findAll().stream()
                .map(c -> new CustomerResponse(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getMobile(),
                        c.getAddress()
                ))
                .collect(Collectors.toList())),HttpStatus.ACCEPTED);
    }
}
