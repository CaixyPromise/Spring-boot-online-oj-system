// @ts-ignore
/* eslint-disable */
import request from '@/lib/request';

/** getActiveToken GET /api/auth/token/fetch/active/${param0} */
export async function getActiveTokenUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: AuthAPI.getActiveTokenUsingGET1Params,
  options?: { [key: string]: any },
) {
  const { token: param0, ...queryParams } = params;
  return request<AuthAPI.BaseResponseString_>(`/api/auth/token/fetch/active/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** searchNickName GET /api/auth/token/search/nickName */
export async function searchNickNameUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: AuthAPI.searchNickNameUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<AuthAPI.BaseResponseBoolean_>('/api/auth/token/search/nickName', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
