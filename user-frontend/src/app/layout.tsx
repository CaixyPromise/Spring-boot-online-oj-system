"use client"
import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import {Toaster} from "@/components/ui/toaster";
import {Provider} from "react-redux";
import stores from "@/stores";
import BasicLayout from "@/layout/BasicLayout";
import React from "react";

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>)
{
    return (
        <html lang="zh-CN">
            <body>
                <Provider store={stores}>
                    <BasicLayout>
                        {children}
                    </BasicLayout>
                    <Toaster/>
                </Provider>
            </body>
        </html>
    );
}
