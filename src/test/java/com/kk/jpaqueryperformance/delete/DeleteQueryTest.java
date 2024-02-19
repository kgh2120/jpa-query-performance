package com.kk.jpaqueryperformance.delete;


import com.kk.jpaqueryperformance.entity.Member;
import com.kk.jpaqueryperformance.entity.Team;
import com.kk.jpaqueryperformance.repository.MemberRepository;
import com.kk.jpaqueryperformance.repository.TeamRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kk.jpaqueryperformance.PerformanceLogger.logPerf;

@Slf4j
@Transactional
@SpringBootTest
public class DeleteQueryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    /*

    딱히 큰 차이는 없는 듯 하다.
        [delete] cost 11ms
        [deleteById] cost 11ms
        [deleteAll] cost 205ms
        [deleteAllWithList] cost 209ms
        [deleteAllById] cost 228ms
        [deleteAllInBatch] cost 193ms
        [deleteAllInBatchWithList] cost 294ms
        [deleteAllByIdInBatch] cost 214ms
     */

    @BeforeEach
    void beforeEach() {

        for (int j = 0; j < 1000; j++) {
            Member member = new Member("회원" + j, 25);
            em.persist(member);
        }

        em.flush();
        em.clear();
    }

    @Test
    void deleteQueryCheckTest() throws Exception {
        Member member = memberRepository.findById(1).orElseThrow();
        long start = System.currentTimeMillis();
        memberRepository.delete(member);
        em.flush();
        long end = System.currentTimeMillis();
        logPerf(log, "delete", start, end);
        // delete from member where id=?
    }

    @Test
    void deleteByIdQueryCheckTest() throws Exception {
        Member member = memberRepository.findById(1).orElseThrow();
        long start = System.currentTimeMillis();
        memberRepository.deleteById(member.getId());
        em.flush();
        long end = System.currentTimeMillis();
        logPerf(log, "deleteById", start, end);
        // delete from member where id=?
    }

    @Test
    void entityManagerDeleteQueryTest() throws Exception{
        long start = System.currentTimeMillis();
        em.createQuery("delete from Member m where m.id =:id")
                .setParameter("id", 1)
                .executeUpdate();
        long end = System.currentTimeMillis();
        logPerf(log, "jpqlDeleteById", start, end);
        // Hibernate: delete from member where id=?
    }
    @Test
    void jdbcDeleteQueryTest() throws Exception{
        long start = System.currentTimeMillis();
        jdbcTemplate.update("delete from Member where id = ?", 1);
        long end = System.currentTimeMillis();
        logPerf(log, "jdbcDeleteById", start, end);

        
    }

    @Test
    void deleteAllQueryCheckTest() throws Exception {
        long start = System.currentTimeMillis();
        memberRepository.deleteAll();
        em.flush();
        long end = System.currentTimeMillis();
        logPerf(log, "deleteAll", start, end);
        /*
        Hibernate: delete from member where id=?
        Hibernate: delete from member where id=?
        Hibernate: delete from member where id=?
          ...
         */
    }

    @Test
    void deleteAllWithListQueryCheckTest() throws Exception {
        long start = System.currentTimeMillis();
        memberRepository.deleteAll(memberRepository.findAll());
        em.flush();
        long end = System.currentTimeMillis();
        logPerf(log, "deleteAllWithList", start, end);
        /*
        Hibernate: delete from member where id=?
        Hibernate: delete from member where id=?
        Hibernate: delete from member where id=?
          ...
         */
    }

    @Test
    void deleteAllByIdQueryCheckTest() throws Exception {
        long start = System.currentTimeMillis();
        memberRepository.deleteAllById(memberRepository.findAll()
                .stream().map(Member::getId).toList());
        em.flush();
        long end = System.currentTimeMillis();
        logPerf(log, "deleteAllById", start, end);
        /*
        Hibernate: delete from member where id=?
        Hibernate: delete from member where id=?
        Hibernate: delete from member where id=?
          ...
         */
    }

    @Test
    void deleteAllInBatchQueryCheckTest() throws Exception {
        long start = System.currentTimeMillis();
        memberRepository.deleteAllInBatch();
        long end = System.currentTimeMillis();
        logPerf(log, "deleteAllInBatch", start, end);
        //Hibernate: delete from member
    }

    @Test
    void deleteAllInBatchQueryWithListCheckTest() throws Exception {
        List<Member> all = memberRepository.findAll();
        long start = System.currentTimeMillis();
        memberRepository.deleteAllInBatch(all);
        long end = System.currentTimeMillis();
        logPerf(log, "deleteAllInBatchWithList", start, end);
        // delete from member where id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=? or id=?
    }

    @Test
    void deleteAllInBatchQueryWithIdsCheckTest() throws Exception {
        List<Member> all = memberRepository.findAll();
        List<Integer> ids = all.stream().map(Member::getId)
                .toList();
        long start = System.currentTimeMillis();
        memberRepository.deleteAllByIdInBatch(ids);
        long end = System.currentTimeMillis();
        logPerf(log, "deleteAllByIdInBatch", start, end);
        // delete from member where id in (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
    }


}