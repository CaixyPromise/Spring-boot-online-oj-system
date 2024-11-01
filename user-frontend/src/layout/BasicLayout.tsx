import React from 'react';
import GlobalNavBar from "@/layout/compoments/GlobalNavBar";
import {useSelector} from "react-redux";
import GlobalFooter from "@/components/GlobalFooter";

interface BasicLayoutProps
{
    children?: React.ReactNode;
}

const BasicLayoutPage: React.FC<BasicLayoutProps> = ({children}) =>
{
    const currentYear = new Date().getFullYear();

    const userInfo: API.UserVO = useSelector(state => state.LoginUser)
    return (
        <>
            <div className="flex min-h-screen w-full flex-col ">
                <GlobalNavBar  userInfo={userInfo}/>

                <main
                    className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">

                    {children}
                </main>

                <GlobalFooter/>
            </div>
        </>
    )
}

export default BasicLayoutPage;
