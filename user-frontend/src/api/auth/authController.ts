// @ts-ignore
/* eslint-disable */
import request from '@/lib/request';

/** activeUserAccount POST /api/auth/active */
export async function activeUserAccountUsingPost1(
  body: AuthAPI.UserActiveAccountRequest,
  options?: { [key: string]: any },
) {
  return request<AuthAPI.BaseResponseBoolean_>('/api/auth/active', {
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
  return request<AuthAPI.BaseResponseUserVO_>('/api/auth/get/login/me', {
    method: 'GET',
    ...(options || {}),
  });
}

/** emailLogin POST /api/auth/login */
export async function emailLoginUsingPost1(
  body: AuthAPI.EmailLoginRequest,
  options?: { [key: string]: any },
) {
  return request<AuthAPI.BaseResponseBoolean_>('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** githubLoginCallback GET /api/auth/oauth2/${param0}/callback */
export async function githubLoginCallbackUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: AuthAPI.githubLoginCallbackUsingGET1Params,
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

/** initiateGithubLogin GET /api/auth/oauth2/${param0}/login */
export async function initiateGithubLoginUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: AuthAPI.initiateGithubLoginUsingGET1Params,
  options?: { [key: string]: any },
) {
  const { provider: param0, ...queryParams } = params;
  return request<AuthAPI.BaseResponseString_>(`/api/auth/oauth2/${param0}/login`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** emailRegister POST /api/auth/register */
export async function emailRegisterUsingPost1(
  body: AuthAPI.EmailRegisterRequest,
  options?: { [key: string]: any },
) {
  return request<AuthAPI.BaseResponseBoolean_>('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
