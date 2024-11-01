// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getLangProvideList GET /api/question/judge/lang/provideList */
export async function getLangProvideListUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListOptionVOString_>('/api/question/judge/lang/provideList', {
    method: 'GET',
    ...(options || {}),
  });
}
