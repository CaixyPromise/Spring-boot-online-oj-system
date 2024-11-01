import {UserRoleEnum} from "@/constant/access";

declare namespace GlobalAPI
{
    type UserVO = {
        createTime?: string;
        id?: number | string;
        nickName?: string;
        userActive?: 'ACTIVE' | 'DISABLED';
        userAvatar?: string;
        userRole?: UserRoleEnum
    };
    type BaseResponse<T> = {
        code: number;
        message: string;
        data: T;
    }
}