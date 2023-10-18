package com.github.onsync.ecommerce.infrasture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserData, Long> {

    Optional<UserData> findByLoginId(String loginId);
}
