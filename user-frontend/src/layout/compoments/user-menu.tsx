import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {Button} from "@/components/ui/button";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {CircleUser, Search} from "lucide-react";
import {Input} from "@/components/ui/input";
import {UserRoleEnum} from "@/constant/access";
import Link from "next/link";
import {DEFAULT_SETTINGS} from "../../../config/config";

interface UserMenuProps
{
    currentUser: API.UserVO
}

const UserMenu = ({currentUser}: UserMenuProps) =>
{
    return (
        <div className="flex w-full items-center gap-4 md:ml-auto md:gap-2 lg:gap-4">
            <form className="ml-auto flex-1 sm:flex-initial">
                <div className="relative flex items-center">
                    <Search className="absolute ml-2 text-muted-foreground" style={{paddingLeft: "5px"}}/>
                    <Input
                        type="search"
                        placeholder="Search Question..."
                        className="pl-8 sm:w-[300px] md:w-[200px] lg:w-[300px]"
                    />
                    {currentUser.nickName &&
                        <h3 style={{marginLeft: "8px", whiteSpace: "nowrap"}}>{currentUser.nickName}</h3>}
                </div>
            </form>
            <DropdownMenu>
            <DropdownMenuTrigger asChild>
                    <Button variant="secondary" size="icon" className="rounded-full">
                        <CircleUser className="h-5 w-5"/>
                        <span className="sr-only">Toggle user menu</span>
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                    {
                        currentUser.userRole === UserRoleEnum.NO_LOGIN ?
                            <>
                                <DropdownMenuLabel>您好，欢迎来到{" "}{DEFAULT_SETTINGS.title}</DropdownMenuLabel>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem onClick={() => {
                                    window.location.href = "/login"
                                }}>
                                    去登录
                                </DropdownMenuItem>
                            </>
                            :
                            <>
                                <DropdownMenuLabel>{currentUser.nickName}</DropdownMenuLabel>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem>用户设置</DropdownMenuItem>
                                {/*<DropdownMenuItem>Support</DropdownMenuItem>*/}
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem>退出</DropdownMenuItem>
                            </>
                    }
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    );
}
export default UserMenu;