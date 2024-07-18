package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Seller;

public interface LoginService {
    Object login(String loginId, String password);
}
