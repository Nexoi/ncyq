package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUid(@Param("uid") Long uid);

    Address findOneByIdAndUid(@Param("id") Long id, @Param("uid") Long uid);

    boolean existsByUid(@Param("uid") Long uid);
}
