﻿import {BASE_URL} from '@/constants';
import type {RequestOptions} from '@@/plugin-request/request';
import type {RequestConfig} from '@umijs/max';

// 与后端约定的响应数据格式
interface ResponseStructure
{
    success: boolean;
    data: any;
    errorCode?: number;
    errorMessage?: string;
}


/**
 * @name 错误处理
 * pro 自带的错误处理， 可以在这里做自己的改动
 * @doc https://umijs.org/docs/max/request#配置
 */
export const requestConfig: RequestConfig = {
    baseURL: BASE_URL,
    withCredentials: true,

    // 请求拦截器
    requestInterceptors: [
        (config: RequestOptions) => {
            config.headers = {
                ...config.headers,
                authToken: localStorage.getItem('authToken') ?? ''
            }

            return { ...config }
        },
    ],

    // 响应拦截器
    responseInterceptors: [
        (response) =>
        {
            // 请求地址
            const requestPath: string = response.config.url ?? '';
            console.log("请求地址：", requestPath)

            // 响应
            const {data} = response as unknown as ResponseStructure;
            if (!data) {
                throw new Error('服务异常');
            }

            // 错误码处理
            const code: number = data.code;
            // 未登录，且不为获取用户登录信息接口
            if (
                code === 40100 &&
                !requestPath.includes('user/get/login') &&
                !location.pathname.includes('/user/login')
            )
            {
                // 跳转至登录页
                window.location.href = `/user/login?redirect=${window.location.href}`;
                throw new Error('请先登录');
            }

            if (code !== 0) {
                throw new Error(data.message ?? '服务器错误');
            }
            return response;
        },
    ],
};
