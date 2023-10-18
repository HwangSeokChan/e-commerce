package com.github.onsync.ecommerce.infrasture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserData, Long> {
}
