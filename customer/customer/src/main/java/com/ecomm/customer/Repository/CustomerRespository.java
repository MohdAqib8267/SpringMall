package com.ecomm.customer.Repository;

import com.ecomm.customer.Modal.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRespository extends JpaRepository<Customer,Integer> {

    Optional<Customer> findByEmail(String email);
}
