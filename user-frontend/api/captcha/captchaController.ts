// @ts-ignore
/* eslint-disable */
import request from '@/lib/request';

/** getCaptcha GET /api/captcha/get */
export async function getCaptchaUsingGet1(options?: { [key: string]: any }) {
  return request<AuthAPI.BaseResponseCaptchaVO_>('/api/captcha/get', {
    method: 'GET',
    ...(options || {}),
  });
}
