import {UserRoleEnum} from "@/constant/access";

export type MenuItemType = {
    name: string,
    path: string,
    icon: string,
    children?: MenuItemType[]
    access?: UserRoleEnum
}
export const MenuItem: MenuItemType[] = [
    {
        name: 'Home',
        path: '/',
        icon: 'home',
        // access: UserRoleEnum.USER
    },
    {
        name: 'About',
        path: '/about',
        icon: 'info',
        // access: UserRoleEnum.USER
    },
]