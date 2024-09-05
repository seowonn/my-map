package com.seowonn.mymap.domain.member.repository;

import com.seowonn.mymap.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByUserId(String userId);

  boolean existsByUserId(String userId);
}
