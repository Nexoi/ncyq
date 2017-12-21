package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.UserLike;
import com.seeu.ywq.release.model.UserLikePKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeRepository extends JpaRepository<UserLike, UserLikePKeys> {
}
