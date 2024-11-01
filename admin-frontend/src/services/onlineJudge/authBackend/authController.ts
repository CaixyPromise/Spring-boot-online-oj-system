// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** activeUserAccount POST /api/auth/active */
export async function activeUserAccountUsingPost1(
  body: API.UserActiveAccountRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/auth/active', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getLoginMe GET /api/auth/get/login/me */
export async function getLoginMeUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserVO_>('/api/auth/get/login/me', {
    method: 'GET',
    ...(options || {}),
  });
}

/** emailLogin POST /api/auth/login */
export async function emailLoginUsingPost1(
  body: API.EmailLoginRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLoginUserVO_>('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** userLogout GET /api/auth/logout */
export async function userLogoutUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/auth/logout', {
    method: 'GET',
    ...(options || {}),
  });
}

/** oauthLoginCallback GET /api/auth/oauth2/${param0}/callback */
export async function oauthLoginCallbackUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.oauthLoginCallbackUsingGET1Params,
  options?: { [key: string]: any },
) {
  const { provider: param0, ...queryParams } = params;
  return request<any>(`/api/auth/oauth2/${param0}/callback`, {
    method: 'GET',
    params: {
      ...queryParams,
      allParams: undefined,
      ...queryParams['allParams'],
    },
    ...(options || {}),
  });
}

/** initOAuthLogin GET /api/auth/oauth2/${param0}/login */
export async function initOAuthLoginUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.initOAuthLoginUsingGET1Params,
  options?: { [key: string]: any },
) {
  const { provider: param0, ...queryParams } = params;
  return request<API.BaseResponseString_>(`/api/auth/oauth2/${param0}/login`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** emailRegister POST /api/auth/register */
export async function emailRegisterUsingPost1(
  body: API.EmailRegisterRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
