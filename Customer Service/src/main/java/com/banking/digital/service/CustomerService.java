package com.banking.digital.service;

import com.banking.digital.dto.CustomerRequest;
import com.banking.digital.dto.CustomerResponse;
import com.banking.digital.entity.Customer;
import com.banking.digital.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public CustomerResponse create(CustomerRequest request) {

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setAddress(request.getAddress());

        repository.save(customer);

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getMobile(),
                customer.getAddress()
        );
    }
    public CustomerResponse getById(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getMobile(),
                customer.getAddress()
        );
    }
    public List<CustomerResponse> getAll() {
        return repository.findAll().stream()
                .map(c -> new CustomerResponse(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getMobile(),
                        c.getAddress()
                ))
                .collect(Collectors.toList());
    }
}
