import {UserRoleEnum} from "@/constant/access";

export const DEFAULT_USER: AuthAPI.UserVO = {
    nickName: "用户未登录",
    userActive: 'DISABLED',
    userAvatar: "",
    id: "",
    // @ts-ignore
    userRole: UserRoleEnum.NO_LOGIN,
}