import React, {useContext, useState} from 'react';
import {CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import Link from "next/link";
import {Button} from "@/components/ui/button";
import {FormStateEnum, LoginFormProps, OAuthProviderType} from "@/app/login/typing.d";
import {useToast} from "@/hooks/use-toast";
import {Icons} from "@/components/ui/icons";
import {Separator} from "@/components/ui/separator";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {emailLoginUsingPost1} from "@/api/auth/authController";
import {useDispatch} from "react-redux";
import {setLoginUser} from "@/stores/LoginUser/index";
import {AppDispatch} from "@/stores";
import {GlobalAPI} from "@/api";
import FormStateContext from "@/app/login/context";

const OAuthProvider: OAuthProviderType[] = [
    {
        name: "Google",
        icon: <Icons.google/>,
        url: "https://google.com",
    },
    {
        name: "Github",
        icon: <Icons.Github/>,
        url: "https://github.com"
    }
]

const OAuthButton = ({provider}: { provider: OAuthProviderType }) => (
    <Button variant="outline" className="w-full">
        <div style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            width: "100%"
        }}>
            <div style={{
                width: "1.0rem",
                height: "1.0rem",
                marginRight: "0.5rem",
                justifyContent: "left"
            }}>
                {provider.icon}
            </div>
            <p style={{
                textAlign: "center",
                flex: 1 /* 让<p>占据多余的空间，确保文字在视觉上居中 */
            }}>{`使用${provider.name}登录`}</p>
        </div>
    </Button>
);

const LoginFormPage: React.FC<LoginFormProps> = ({email, setEmail}) =>
{
    const {toast} = useToast();
    const {setFormState, userAccount, setUserAccount} = useContext(FormStateContext);
    const [formField, setFormField] = useState<{
        email: string,
        password: string
    }>({
        email: "",
        password: ""
    })
    const dispatch = useDispatch<AppDispatch>();
    const [loginHandler, isPending] = useAsyncHandler<GlobalAPI.BaseResponse<AuthAPI.UserVO>>();

    const handleFieldChange = (value: string, type: string) =>
    {
        setFormField(prevState => ({
            ...prevState,
            [type]: value
        }));
    }

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    {
        const value = e.target.value
        setEmail(value);
        handleFieldChange(value, "email")
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    {
        const value = e.target.value
        handleFieldChange(value, "password")
    };

    const onLoginClick = async () =>
    {
        if (formField.email === "" || formField.password === "")
        {
            toast({
                description: "请输入邮箱和密码",
                duration: 5000,
            })
        }
        const result = await loginHandler(async () =>
        {
            return await emailLoginUsingPost1({
                email: formField.email,
                password: formField.password
            });
        }, [])
        if (result?.code === 0)
        {
            toast({
                description: "登录成功",
                duration: 5000,
                position: "top-center"
            });
            dispatch(setLoginUser(result?.data));
        }
    }
    const onForgetPasswordClick = () =>
    {
        setFormState(FormStateEnum.FORGET);
    }
    return (
        <>
            <CardHeader>
                <CardTitle className="text-2xl">Login</CardTitle>
                <CardDescription>
                    Enter your email below to login to your account
                </CardDescription>
            </CardHeader>

            <CardContent>
                <div className="grid gap-4">
                    <div className="grid gap-2">
                        <Label htmlFor="email">邮箱</Label>
                        <Input
                            id="email"
                            type="email"
                            placeholder="m@example.com"
                            required
                            value={email}
                            onChange={handleEmailChange}
                        />
                    </div>
                    <div className="grid gap-2">
                        <div className="flex items-center">
                            <Label htmlFor="password">密码</Label>
                            <Link href="#" className="ml-auto inline-block text-sm underline"
                                  onClick={onForgetPasswordClick}>
                                已注册，忘记密码？
                            </Link>
                        </div>
                        <Input.Password
                            id="password"
                            type="password"
                            required
                            placeholder="输入您的密码"
                            onChange={handlePasswordChange}
                        />
                    </div>
                    <Button onClick={onLoginClick} className="w-full" disabled={isPending}>
                        登录
                    </Button>
                    <Separator/>
                    {
                        OAuthProvider.map((provider) => (
                            <OAuthButton key={provider.name} provider={provider}/>
                        ))
                    }
                </div>

                <div className="mt-4 text-center text-sm">
                    没有账号?{" "}
                    <a onClick={() => setFormState(FormStateEnum.REGISTER)} className="underline">
                        去注册
                    </a>
                </div>
            </CardContent>
        </>
    )
}

export default LoginFormPage;
