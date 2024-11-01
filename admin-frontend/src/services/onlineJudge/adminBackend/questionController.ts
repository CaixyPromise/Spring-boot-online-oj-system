// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addQuestion POST /api/admin/question/add */
export async function addQuestionUsingPost1(
  body: API.QuestionAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/admin/question/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteQuestion POST /api/admin/question/delete */
export async function deleteQuestionUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/admin/question/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listQuestionByPage POST /api/admin/question/list/page */
export async function listQuestionByPageUsingPost1(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestion_>('/api/admin/question/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateQuestion POST /api/admin/question/update */
export async function updateQuestionUsingPost1(
  body: API.QuestionUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/admin/question/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
