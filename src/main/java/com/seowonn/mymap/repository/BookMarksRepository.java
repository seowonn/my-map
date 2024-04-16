package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.BookMarks;
import com.seowonn.mymap.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarksRepository extends JpaRepository<BookMarks, Long> {

  Page<BookMarks> findAllByMember(Member member, Pageable pageable);

  Optional<BookMarks> findALlByMemberUserId(String userId);
}
