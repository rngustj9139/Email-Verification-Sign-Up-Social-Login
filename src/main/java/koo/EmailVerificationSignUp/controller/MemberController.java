package koo.EmailVerificationSignUp.controller;

import koo.EmailVerificationSignUp.dto.MemberRegisterRequestDto;
import koo.EmailVerificationSignUp.entity.Member;
import koo.EmailVerificationSignUp.mail.MailSendService;
import koo.EmailVerificationSignUp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MailSendService mss;

    @GetMapping("/register/member") // 회원가입 GET
    public String registerMemberForm(Model model) {
        MemberRegisterRequestDto memberRegisterRequestDto = new MemberRegisterRequestDto();
        model.addAttribute("memberRegisterRequestDto", memberRegisterRequestDto);

        return "registerMember";
    }

    @PostMapping("/register/member") // 회원가입 POST
    public String registerMember(@Validated @ModelAttribute MemberRegisterRequestDto memberRegisterRequestDto, BindingResult bindingResult) {
        if (!memberRegisterRequestDto.getFirstPassword().equals(memberRegisterRequestDto.getSecondPassword())) {
            bindingResult.rejectValue("secondPassword", "twoPasswordNotEqual");
        }

        if (bindingResult.hasErrors()) {
            return "registerMember";
        }

        // 기본 정보 저장
        Member member = new Member();
        member.setMemberName(memberRegisterRequestDto.getMemberName());
        member.setEmail(memberRegisterRequestDto.getEmail());
        member.setPassword(memberRegisterRequestDto.getFirstPassword());
        memberService.join(member);

        //임의의 authKey 생성 & 이메일 발송
        String authKey = mss.sendAuthMail(memberRegisterRequestDto.getEmail());

        Map<String, String> map = new HashMap<String, String>();
        map.put("email", memberRegisterRequestDto.getEmail());
        map.put("authKey", authKey);
        log.info("members email & authKey = {}", map);

        //DB에 authKey 업데이트
        memberService.updateAuthKey(map);

        return "redirect:/register/member";
    }

}
