package com.github.onsync.ecommerce.application.outbound;

import com.github.onsync.ecommerce.application.domain.User;

public interface UpdateUserPort {

    User updateUser(User user);
}
