import {getCaptchaUsingGet1} from "@/api/captcha/captchaController";
import {sendCaptchaUsingPost1} from "@/api/captcha/emailCaptchaController";
import {EmailCategoryEnums} from "@/enums";
import {emailLoginUsingPost1} from "@/api/auth/authController";

export const queryServer = {
    captchaImage: async () =>
    {
        const {data} = await getCaptchaUsingGet1();

        if (data?.code === 0)
        {
            return data.data
        }
        return null;
    },
    sendRegisterCaptcha: async (toEmail: string): Promise<boolean> =>
    {
        const {data} = await sendCaptchaUsingPost1({
            toEmail: toEmail,
            type: EmailCategoryEnums.REGISTER
        })
        return data?.code === 0;
    },
    emailLogin: async (): Promise<AuthAPI.UserVO> => {
        const {data} = await emailLoginUsingPost1({
            email: formField.email,
            password: formField.password
        });
        return
    }
}