package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Seller;
import com.clonecode.orderweb.repository.CustomerRepository;
import com.clonecode.orderweb.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Object login(String loginId, String password) {
        if(customerRepository.existsByLoginId(loginId)){
            Customer customer = customerRepository.findByLoginId(loginId);
            if (customer != null && customer.getPassword().equals(password)){
                return customer;
            }
        } else if (sellerRepository.existsByLoginId(loginId)){
            Seller seller = sellerRepository.findByLoginId(loginId);
            if (seller != null && seller.getPassword().equals(password)){
                return seller;
            }
        }
        return null;
    }
}
