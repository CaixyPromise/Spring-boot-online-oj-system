import {UserRoleEnum} from "@/constant/access";

export const canAccess = (userRole: UserRoleEnum, needAccess?: UserRoleEnum) =>
{
    if (needAccess === UserRoleEnum.NO_LOGIN) return true;
    if (needAccess === UserRoleEnum.USER)
    {
        return userRole === UserRoleEnum.USER || userRole === UserRoleEnum.ADMIN;
    }
    if (needAccess === UserRoleEnum.ADMIN)
    {
        return userRole === UserRoleEnum.ADMIN;
    }
    return true;
}