package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Seller;
import com.clonecode.orderweb.dto.MemberRegisterDto;

public interface MemberRegisterService {

    Customer registerCustomer(MemberRegisterDto memberRegisterDto);
    Seller registerSeller(MemberRegisterDto memberRegisterDto);

}
