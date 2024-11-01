"use client"
import React, {FormEventHandler, useState} from 'react';
import {FormStateEnum, LoginPageProps, LoginPayloadType} from "@/app/login/typing.d";

import {Tabs, TabsContent, TabsList, TabsTrigger} from '@/components/ui/tabs';
import LoginForm from "@/app/login/components/LoginForm";
import {Card} from "@/components/ui/card";
import RegisterForm from "@/app/login/components/RegisterForm";
import FormStateContext from "@/app/login/context";


const LoginPage: React.FC<LoginPageProps> = () =>
{
    const [formState, setFormState] = useState<FormStateEnum>(FormStateEnum.LOGIN);
    const [userAccount, setUserAccount] = useState<string>("");

    const handleFormStateChange = (formEvent: FormStateEnum | string) =>
    {
        setFormState(formEvent as FormStateEnum)
    }

    return (
        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh'}}>
            <Tabs value={formState} onValueChange={handleFormStateChange} className="tabs-container">
                <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value={FormStateEnum.LOGIN}>登录</TabsTrigger>
                    <TabsTrigger value={FormStateEnum.REGISTER}>注册</TabsTrigger>
                </TabsList>
                <FormStateContext.Provider value={{
                    formState,
                    setFormState,
                    userAccount,
                    setUserAccount
                }}>
                    <Card className="card-container" style={{ boxShadow: '0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)'}}>
                        <TabsContent value={FormStateEnum.LOGIN}>
                            <LoginForm
                                email={userAccount}
                                setEmail={setUserAccount}
                            />
                        </TabsContent>
                        <TabsContent value={FormStateEnum.REGISTER}>
                            <RegisterForm
                            />
                        </TabsContent>
                    </Card>
                </FormStateContext.Provider>
            </Tabs>
        </div>
    )
}

export default LoginPage;
