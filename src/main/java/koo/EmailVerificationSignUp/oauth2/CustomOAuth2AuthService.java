package koo.EmailVerificationSignUp.oauth2;

import koo.EmailVerificationSignUp.entity.Member;
import koo.EmailVerificationSignUp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CustomOAuth2AuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

    private final MemberRepository memberRepository;

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2AuthService");

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId();
        String userNameAttributeName = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String providerId = oAuth2User.getAttribute("sub");
        log.info("providerId = {}", providerId);
        String name = oAuth2User.getAttribute("name");
        log.info("name = {}", name);
        String email = oAuth2User.getAttribute("email");
        log.info("email = {}", email);
        String password = UUID.randomUUID().toString();
        log.info("password = {}", password);

        Optional<Member> findedMember = memberRepository.findByName(name);

        if (!findedMember.isPresent()) {
            Member member = new Member();
            member.setAuthStatus(1);
            member.setEmail(email);
            member.setMemberName(name);
            member.setPassword(password);
            log.info("===member save===");

            memberRepository.save(member);
        }

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

}
