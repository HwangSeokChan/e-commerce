package com.github.onsync.ecommerce.infrasture;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    Long id;
    String loginId;
    String password;
    String name;
    String regNo;
}
