package com.clonecode.orderweb.dto;

import com.clonecode.orderweb.domain.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberRegisterDto {
    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입니다.")
    private String phoneNumber;

    @NotBlank(message = "아이디는 필수 입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotBlank(message = "도시는 필수 입니다.")
    private String city;

    @NotBlank(message = "도로명 주소를 입력해 주세요.")
    private String streetAddress;
}
