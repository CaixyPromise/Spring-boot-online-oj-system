// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** editQuestion POST /api/question/edit */
export async function editQuestionUsingPost1(
  body: API.QuestionEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/question/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getQuestionById GET /api/question/get */
export async function getQuestionByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseQuestion_>('/api/question/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getQuestionVOById GET /api/question/get/vo */
export async function getQuestionVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseQuestionVO_>('/api/question/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listQuestionVOByPage POST /api/question/list/page/vo */
export async function listQuestionVoByPageUsingPost1(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionVO_>('/api/question/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyQuestionVOByPage POST /api/question/my/list/page/vo */
export async function listMyQuestionVoByPageUsingPost1(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionVO_>('/api/question/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** doQuestionSubmit POST /api/question/question_submit/do */
export async function doQuestionSubmitUsingPost1(
  body: API.QuestionSubmitAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/question/question_submit/do', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listQuestionSubmitByPage POST /api/question/question_submit/list/page */
export async function listQuestionSubmitByPageUsingPost1(
  body: API.QuestionSubmitQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionSubmitVO_>('/api/question/question_submit/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
