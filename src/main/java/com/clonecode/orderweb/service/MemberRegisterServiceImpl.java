package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Address;
import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Seller;
import com.clonecode.orderweb.dto.MemberRegisterDto;
import com.clonecode.orderweb.repository.CustomerRepository;
import com.clonecode.orderweb.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberRegisterServiceImpl implements MemberRegisterService{

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public Customer registerCustomer(MemberRegisterDto dto) {
        if (customerRepository.existsByLoginId(dto.getLoginId())){
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setLoginId(dto.getLoginId());
        customer.setPassword(dto.getPassword());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setBillingAddress(new Address(dto.getCity(), dto.getStreetAddress()));
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Seller registerSeller(MemberRegisterDto dto) {
        if (sellerRepository.existsByLoginId(dto.getLoginId())){
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setLoginId(dto.getLoginId());
        seller.setPassword(dto.getPassword());
        seller.setPhoneNumber(dto.getPhoneNumber());
        seller.setAddress(new Address(dto.getCity(), dto.getStreetAddress()));
        return sellerRepository.save(seller);
    }
}
