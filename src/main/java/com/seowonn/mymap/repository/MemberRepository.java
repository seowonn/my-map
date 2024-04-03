package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByUserId(String userId);

  boolean existsByUserId(String userId);
}
