// @ts-ignore
/* eslint-disable */
import request from '@/lib/request';

/** errorHtml GET /api/captcha/error */
export async function errorHtmlUsingGet1(options?: { [key: string]: any }) {
  return request<AuthAPI.ModelAndView>('/api/captcha/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** errorHtml PUT /api/captcha/error */
export async function errorHtmlUsingPut1(options?: { [key: string]: any }) {
  return request<AuthAPI.ModelAndView>('/api/captcha/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** errorHtml POST /api/captcha/error */
export async function errorHtmlUsingPost1(options?: { [key: string]: any }) {
  return request<AuthAPI.ModelAndView>('/api/captcha/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** errorHtml DELETE /api/captcha/error */
export async function errorHtmlUsingDelete1(options?: { [key: string]: any }) {
  return request<AuthAPI.ModelAndView>('/api/captcha/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** errorHtml PATCH /api/captcha/error */
export async function errorHtmlUsingPatch1(options?: { [key: string]: any }) {
  return request<AuthAPI.ModelAndView>('/api/captcha/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
