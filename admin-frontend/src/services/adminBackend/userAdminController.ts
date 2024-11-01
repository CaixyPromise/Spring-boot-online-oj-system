// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** deleteUserById POST /api/admin/user/delete */
export async function deleteUserByIdUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/admin/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getUserById GET /api/admin/user/get */
export async function getUserByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserAdminVO_>('/api/admin/user/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listUserByPage POST /api/admin/user/list/page */
export async function listUserByPageUsingPost1(
  body: API.UserQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageUserAdminVO_>('/api/admin/user/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
