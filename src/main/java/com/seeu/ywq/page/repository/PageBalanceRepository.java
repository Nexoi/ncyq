package com.seeu.ywq.page.repository;

import com.seeu.ywq.pay.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageBalanceRepository extends JpaRepository<Balance, Long> {
    @Query(value = "SELECT pb.uid, pb.balance, ul.nickname, ul.head_icon_url FROM ywq_pay_balance pb join ywq_user_login ul on ul.uid = pb.uid order by pb.balance desc limit :top", nativeQuery = true)
    List<Object[]> queryItTop1X(@Param("top") Integer top);
}
