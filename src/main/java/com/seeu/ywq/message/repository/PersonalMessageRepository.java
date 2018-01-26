package com.seeu.ywq.message.repository;

import com.seeu.ywq.message.model.PersonalMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 4:41 PM
 * Describe:
 */

public interface PersonalMessageRepository extends JpaRepository<PersonalMessage, Long> {


    Page<PersonalMessage> findAllByUid(@Param("uid") Long uid,
                                       Pageable pageable);

    Page<PersonalMessage> findAllByUidAndType(@Param("uid") Long uid,
                                              @Param("type") PersonalMessage.TYPE type,
                                              Pageable pageable);

    List<PersonalMessage> findAllByUidAndTypeAndCreateTimeAfter(@Param("uid") Long uid,
                                                                @Param("type") PersonalMessage.TYPE type,
                                                                @Param("createTime") Date createTime);
}
