package koo.EmailVerificationSignUp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class MemberRegisterRequestDto {

    @NotBlank(message = "성함 입력은 필수 입니다.")
    @Size(min = 1, message = "이름은 최소 2자 이상이여야 합니다.")
    private String memberName;

    @Email
    @NotBlank(message = "이메일 입력은 필수 입니다.")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    @Pattern(regexp = "^.*(?=^.{4,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "비밀번호는 4~15자리의 숫자,문자,특수문자로 이루어져야합니다.")
    private String firstPassword;

    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    private String secondPassword;

}
