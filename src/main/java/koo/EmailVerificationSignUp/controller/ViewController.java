package koo.EmailVerificationSignUp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping("/social") // 소셜 로그인 이후 성공 페이지를 보여줄 컨트롤러 provider(platform)는 요청이 들어온 서드 파티 앱을 말하고 oauthId(socialId)는 그 서드 파티 앱의 PK를 말한다
    public String socialSuccess(Model model,
                                @RequestParam(value = "provider", required = false) String provider,
                                @RequestParam(value = "oauthId", required = false) String oauthId
    ) {
        model.addAttribute("provider", provider);
        model.addAttribute("oauthId", oauthId);

        return "social-success";
    }

}
