// @ts-ignore
/* eslint-disable */
import request from '@/lib/request';

/** error GET /api/auth/error */
export async function errorUsingGet1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/auth/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** error PUT /api/auth/error */
export async function errorUsingPut1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/auth/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** error POST /api/auth/error */
export async function errorUsingPost1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/auth/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** error DELETE /api/auth/error */
export async function errorUsingDelete1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/auth/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** error PATCH /api/auth/error */
export async function errorUsingPatch1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/auth/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
