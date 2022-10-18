package koo.EmailVerificationSignUp.repository;

import koo.EmailVerificationSignUp.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);

        return member;
    }

    public Member findById(Long id) {
        Member findedMember = em.find(Member.class, id);

        return findedMember;
    }

    public Member findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                                      .setParameter("email", email)
                                      .getSingleResult();
    }

}
