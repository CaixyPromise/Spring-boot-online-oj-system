// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** errorHtml GET /api/auth/error */
export async function errorHtmlUsingGet1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/auth/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** errorHtml PUT /api/auth/error */
export async function errorHtmlUsingPut1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/auth/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** errorHtml POST /api/auth/error */
export async function errorHtmlUsingPost1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/auth/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** errorHtml DELETE /api/auth/error */
export async function errorHtmlUsingDelete1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/auth/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** errorHtml PATCH /api/auth/error */
export async function errorHtmlUsingPatch1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/auth/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
