"use client"
import React, {useEffect} from 'react';
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {useParams, useSearchParams} from "next/navigation";
import {Form, useForm} from "react-hook-form";
import {FormSubmitHandler} from "react-hook-form/dist/types/form";

interface ActiveFormProps
{

}

const ActiveFormPage: React.FC<ActiveFormProps> = () =>
{
    const { register, control, handleSubmit, formState: { errors }, setValue } = useForm({
        defaultValues: {
            name: '',
            gender: "1"
        }
    });
    const searchParams = useSearchParams();
    const token = searchParams.get('token');

    const onSubmit = ({data}: FormSubmitHandler) =>
    {
        console.log(data);
    }

    useEffect(() => {
        if (!token)
        {
            window.location.href = "/"
        }
    })

    return (
        <>
            <div style={{display: "flex", alignItems: "center", justifyContent: "center", height: "90vh"}}>
                <Card className="w-[350px]">
                    <Form control={control} onSubmit={onSubmit}>
                        <CardHeader>
                            <CardTitle>账号激活</CardTitle>
                            <CardDescription>初始化您的账号信息并激活账号</CardDescription>
                        </CardHeader>

                        <CardContent>
                            <div className="grid w-full items-center gap-4">
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="emailLabel">邮箱账号</Label>
                                    <Label htmlFor="account">1944630344@qq.com</Label>
                                </div>
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="name">用户名</Label>
                                    <Input required {...register("name", {required: "用户名是必须项"})} id="name" placeholder="输入您的用户名"/>
                                </div>
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="framework">性别</Label>
                                    <Select onValueChange={(value) => setValue("gender", value)} defaultValue="1">
                                        <SelectTrigger id="framework">
                                            <SelectValue placeholder="选择您的性别"/>
                                        </SelectTrigger>
                                        <SelectContent position="popper">
                                            <SelectItem value="1">男</SelectItem>
                                            <SelectItem value="2">女</SelectItem>
                                            <SelectItem value="0">未知</SelectItem>
                                        </SelectContent>
                                    </Select>
                                </div>
                            </div>
                        </CardContent>
                        <CardFooter className="flex justify-between">
                            <Button type="submit" className="w-full">提交并激活</Button>
                        </CardFooter>
                    </Form>

                </Card>
            </div>
        </>
    )
}

export default ActiveFormPage;
