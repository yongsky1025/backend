package com.example.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Transactional
    @Test
    public void readTest4() {

        TeamMember member = teamMemberRepository.findById(3L).get();
        System.out.println(member);
        // ......
        System.out.println(member.getTeam());

    }

    // team1 추가 및 홍길동 정보 삽입(team정보 조인)
    @Test
    public void insertTest() {
        Team team = Team.builder().name("team4").build();
        teamRepository.save(team);

        TeamMember member = TeamMember.builder()
                .name("홍길동")
                .team(team)
                .build();
        teamMemberRepository.save(member);
    }

    // team1 이 존재할 경우
    // team1 에 홍성아 정보 삽입(team정보 조인)
    @Test
    public void insertTest2() {

        // Team team = Team.builder().id(1L).build();
        Team team = teamRepository.findById(3L).get();

        TeamMember member = TeamMember.builder()
                .name("홍성아")
                .team(team)
                .build();
        teamMemberRepository.save(member);
    }

    // team2 추가
    @Test
    public void insertTest3() {

        Team team = Team.builder().name("team3").build();
        teamRepository.save(team);
    }

    @Test
    public void readTest() {

        // Team team = Team.builder().id(1L).build();
        Team team = teamRepository.findById(1L).get();
        System.out.println(team); // Team(id=1, name=null)

        // 외래키가 적용된 테이블이기 때문에 join을 바로 해서 코드 실행
        TeamMember member = teamMemberRepository.findById(1L).get();
        System.out.println(member); // TeamMember(id=1, name=홍길동, team=Team(id=1, name=team1))

        // 팀원 => 팀 조회
        // System.out.println("팀 명 " + member.getTeam().getName());

        // 팀 => 팀원 조회 (x) @OneToMany
    }

    @Test
    public void updateTest() {

        // 팀 이름 변경
        Team team = teamRepository.findById(1L).get();
        team.changeName("플라워");
        System.out.println(teamRepository.save(team));

        // 팀 변경
        TeamMember teamMember = teamMemberRepository.findById(2L).get();
        teamMember.changeTeam(Team.builder().id(3L).build());
        System.out.println(teamMemberRepository.save(teamMember));

    }

    @Test
    public void deleteTest() {

        // 팀원 삭제
        // teamRepository.deleteById(1L); // 기존 팀원이 있어 삭제 불가

        // 1. 팀원(팀 정보를 이용해서) 찾기
        List<TeamMember> result = teamMemberRepository.findByTeam(Team.builder().id(1L).build());
        // 2. 삭제하려고 하는 팀의 팀원들 다른 팀으로 변경
        result.forEach(m -> {
            m.changeTeam(Team.builder().id(3L).build());
            teamMemberRepository.save(m); // 팀을 넣고
        });
        // 3. 팀 삭제
        teamRepository.deleteById(1L);

    }

    @Test
    public void deleteTest2() {

        // 1. 팀원(팀 정보를 이용해서) 찾기
        List<TeamMember> result = teamMemberRepository.findByTeam(Team.builder().id(2L).build());
        // 2. 삭제하려고 하는 팀의 팀원을 삭제
        result.forEach(m -> {
            teamMemberRepository.delete(m);
        });
        // 3. 팀 삭제
        teamRepository.deleteById(2L);

    }

    // ------------- 팀 => 멤버 조회

    @Transactional
    @Test
    public void readTest2() {

        Team team = teamRepository.findById(3L).get();

        // 팀 => 팀원 조회
        // @OneToMany(mappedBy = "team")
        // private List<TeamMember> members = new ArrayList<>();

        System.out.println(team); // select * from teamtbl where id=3;
        System.out.println(team.getMembers()); // select * from team_member where team_id = 3;

    }

    @Test
    public void readTest3() {

        Team team = teamRepository.findById(3L).get();

        // 팀 => 팀원 조회(left join 처리)
        // @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
        System.out.println(team);
        System.out.println(team.getMembers());
    }

    // cascade 개념 적용
    @Test
    public void insertCascadeTest() {

        Team team = Team.builder().name("new").build();

        team.getMembers().add(TeamMember.builder().name("강감찬").team(team).build());
        teamRepository.save(team);
    }

    @Test
    public void removeCascadeTest() {
        teamRepository.deleteById(5L);
    }

    @Commit
    @Transactional
    @Test
    public void updateCascadeTest() {

        Team team = teamRepository.findById(6L).get();
        team.changeName("sunflower");
        TeamMember teamMember = team.getMembers().get(0);
        teamMember.changeName("홍시루");

        // teamRepository.save(team);
    }

    // orphanRemoval = true 적용
    @Commit
    @Transactional
    @Test
    public void removeOrphanTest() {
        Team team = teamRepository.findById(3L).get();
        team.getMembers().remove(0);
        teamRepository.save(team);
    }

    // @Query 테스트
    @Test
    public void testQuery() {

        Team team = teamRepository.findById(3L).get();
        List<Object[]> result = teamMemberRepository.findByMemberAndTeam(team);

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));

            TeamMember member = (TeamMember) objects[0];
            Team team1 = (Team) objects[1];

            System.out.println(member);
            System.out.println(team1);

        }
    }

    @Test
    public void testQuery2() {

        List<Object[]> result = teamMemberRepository.findByMemberAndTeam2(3L);

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));

            TeamMember member = (TeamMember) objects[0];
            Team team1 = (Team) objects[1];

            System.out.println(member);
            System.out.println(team1);

        }
    }

    @Test
    public void testQuery3() {

        List<Object[]> result = teamMemberRepository.findByMemberAndTeam3();

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));

            TeamMember member = (TeamMember) objects[0];
            Team team1 = (Team) objects[1];

            System.out.println(member);
            System.out.println(team1);

        }
    }

}
