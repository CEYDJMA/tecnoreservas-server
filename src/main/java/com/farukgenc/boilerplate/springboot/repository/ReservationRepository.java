package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.Reservation;
import com.farukgenc.boilerplate.springboot.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationStatus(String status);
    List<Reservation> findAllByTalent_Id(Long id);
    List<Reservation> findAllByExpert_Id(Long id);
    //List<Reservation> findAllByUser_UserRole(UserRole userRole);
}
