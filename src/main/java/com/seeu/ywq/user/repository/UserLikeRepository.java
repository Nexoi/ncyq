package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.UserLike;
import com.seeu.ywq.user.model.UserLikePKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeRepository extends JpaRepository<UserLike, UserLikePKeys> {
}
