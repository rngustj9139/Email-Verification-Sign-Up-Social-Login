package koo.EmailVerificationSignUp.service;

import koo.EmailVerificationSignUp.entity.Member;
import koo.EmailVerificationSignUp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member member) {
        return memberRepository.save(member);
    }

    public Member findOneMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member findOneMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void updateAuthKey(Map<String, String> map) {
        String memberEmail = map.get("email");
        String authKey = map.get("authKey");

        Member findedMember = memberRepository.findByEmail(memberEmail);
        findedMember.setAuthKey(authKey);
    }

    public void updateAuthStatus(Map<String, String> map) {
        String authKey = map.get("authKey");
        Member findedMember = memberRepository.findByEmail(map.get("email"));
        String savedAuthKey = findedMember.getAuthKey();

        if (authKey.equals(savedAuthKey)) {
            findedMember.setAuthStatus(1);
        }
    }

}
