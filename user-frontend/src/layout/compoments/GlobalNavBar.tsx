import React from 'react';
import Link from "next/link";
import {MenuItem, MenuItemType} from "../../../config/menu";
import {
    NavigationMenu,
    NavigationMenuContent,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList, NavigationMenuTrigger, navigationMenuTriggerStyle
} from "@/components/ui/navigation-menu";
import Image from "next/image";
import UserMenu from "@/layout/compoments/user-menu";
import {canAccess} from "@/access/checkAccess";
import {DEFAULT_SETTINGS} from "../../../config/config";


interface GlobalNavBarProps
{
    className?: string;
    props?: any;
    userInfo: API.UserVO
}

const GlobalNavBarPage: React.FC<GlobalNavBarProps> = ({className, props, userInfo}) =>
{
    const renderMenuItem = (item: MenuItemType): React.ReactNode =>
    {
        if (item.children && item.children.length > 0)
        {
            return (
                <NavigationMenuItem key={item.name}>
                    <NavigationMenuTrigger>{item.name}</NavigationMenuTrigger>
                    <NavigationMenuContent>
                        <ul className="grid gap-3 p-4 md:w-[400px] lg:w-[500px] lg:grid-cols-[.75fr_1fr]">
                            {item.children.map(child => (
                                <li key={child.name} className="row-span-3" >
                                    {renderMenuItem(child)}
                                </li>
                            ))}
                        </ul>
                    </NavigationMenuContent>
                </NavigationMenuItem>
            );
        }
        else if (canAccess(userInfo.userRole, item.access))
        {
            return (
                <NavigationMenuItem key={item.name}>
                    <Link href={item.path} legacyBehavior passHref>
                        <NavigationMenuLink className={navigationMenuTriggerStyle()} style={{fontSize: "18px", color: "grey"}}>
                            {item.name}
                        </NavigationMenuLink>
                    </Link>
                </NavigationMenuItem>
            );
        }
    };

    return (
        <>
            <header
                className="bg-gray-800 text-white py-4 border-t-2 border-gray-200 flex justify-between items-center border-b border-gray-200">
                <div className="container mx-auto flex justify-between items-center px-8 md:px-12 lg:px-16 w-full">
                    <div className="flex items-center space-x-7">
                        <Image src="/assets/logo192.png" alt="logo" width={26} height={26}/>
                        <h1 className="text-2xl font-bold pl-8 text-gray-400">{DEFAULT_SETTINGS.title}</h1>
                        <NavigationMenu className="pl-5">
                            <NavigationMenuList>
                                {MenuItem.map((item: MenuItemType) => renderMenuItem(item))}
                            </NavigationMenuList>
                        </NavigationMenu>
                    </div>
                    <div className="pr-8">
                        <UserMenu currentUser={userInfo}/>
                    </div>
                </div>
            </header>


        </>
    )
}

export default GlobalNavBarPage;
