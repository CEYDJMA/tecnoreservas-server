package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.User;
import com.farukgenc.boilerplate.springboot.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
public interface UserRepository extends JpaRepository<User, Long> {

    static List<Reservation> findAllByReservationUserRole(UserRole userRole) {
        return null;
    }

    User findByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
