// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** errorHtml GET /api/user/error */
export async function errorHtmlUsingGet1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/user/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** errorHtml PUT /api/user/error */
export async function errorHtmlUsingPut1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/user/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** errorHtml POST /api/user/error */
export async function errorHtmlUsingPost1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/user/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** errorHtml DELETE /api/user/error */
export async function errorHtmlUsingDelete1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/user/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** errorHtml PATCH /api/user/error */
export async function errorHtmlUsingPatch1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/user/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
