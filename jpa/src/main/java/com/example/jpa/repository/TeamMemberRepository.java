package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;
import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    // 팀 정보를 이용해 팀원 찾기
    List<TeamMember> findByTeam(Team team);

    @Query("select m, t from TeamMember m join m.team t where t =:team")
    List<Object[]> findByMemberAndTeam(@Param("team") Team team);

    @Query("select m, t from TeamMember m join m.team t where t.id =:id")
    List<Object[]> findByMemberAndTeam2(@Param("id") Long id);

    @Query("select m, t from TeamMember m left join m.team t")
    List<Object[]> findByMemberAndTeam3();

    @NativeQuery("select from teamtbl join team_member tm on tm.team_id = t.team_id")
    List<Object[]> findByMemberAndTeam4();

}
