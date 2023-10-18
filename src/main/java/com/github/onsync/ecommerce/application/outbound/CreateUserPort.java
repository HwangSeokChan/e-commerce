package com.github.onsync.ecommerce.application.outbound;

import com.github.onsync.ecommerce.application.domain.User;

public interface CreateUserPort {

    User creatUser(User user);
}
