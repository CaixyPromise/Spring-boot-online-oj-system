// @ts-ignore
/* eslint-disable */
import request from '@/lib/request';

/** sendCaptcha POST /api/captcha/email/send */
export async function sendCaptchaUsingPost1(
  body: CaptchaAPI.EmailSenderCaptchaRequest,
  options?: { [key: string]: any },
) {
  return request<CaptchaAPI.BaseResponseBoolean_>('/api/captcha/email/send', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
