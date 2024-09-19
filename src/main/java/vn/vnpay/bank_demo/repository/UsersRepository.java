package vn.vnpay.bank_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vnpay.bank_demo.model.entity.Users;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findByUserName(String username);

    Optional<Users> findByEmail(String email);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
}
