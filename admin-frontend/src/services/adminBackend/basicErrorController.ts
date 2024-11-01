// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** errorHtml GET /api/admin/error */
export async function errorHtmlUsingGet1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/admin/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** errorHtml PUT /api/admin/error */
export async function errorHtmlUsingPut1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/admin/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** errorHtml POST /api/admin/error */
export async function errorHtmlUsingPost1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/admin/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** errorHtml DELETE /api/admin/error */
export async function errorHtmlUsingDelete1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/admin/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** errorHtml PATCH /api/admin/error */
export async function errorHtmlUsingPatch1(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/api/admin/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
