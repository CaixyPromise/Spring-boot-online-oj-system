// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** deleteUser POST /api/user/delete */
export async function deleteUserUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getLoginUser GET /api/user/get/login */
export async function getLoginUserUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginUserVO_>('/api/user/get/login', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getMe GET /api/user/get/me */
export async function getMeUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseAboutMeVO_>('/api/user/get/me', {
    method: 'GET',
    ...(options || {}),
  });
}

/** listUserVOByPage POST /api/user/list/page/vo */
export async function listUserVoByPageUsingPost1(
  body: API.UserQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageUserVO_>('/api/user/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** modifyPassword POST /api/user/modify/password */
export async function modifyPasswordUsingPost1(
  body: API.UserModifyPasswordRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/user/modify/password', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateUser POST /api/user/update */
export async function updateUserUsingPost1(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateMyUser POST /api/user/update/my */
export async function updateMyUserUsingPost1(
  body: API.UserUpdateSelfRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/user/update/my', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
