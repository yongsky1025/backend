package com.example.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.query.Param;

import com.example.club.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    // 구글로그인 접근 여부

    // @Query("select m from Member m join fetch m.roles where m.email= :email and
    // m.fromSocial = :fromSocial")
    @EntityGraph(attributePaths = { "roles" }, type = EntityGraphType.LOAD)
    // @Query("select m from Member m where m.email= :email and m.fromSocial =
    // :fromSocial")
    Optional<Member> findByEmailAndFromSocial(@Param("email") String email, @Param("fromSocial") boolean fromSocial);
}
