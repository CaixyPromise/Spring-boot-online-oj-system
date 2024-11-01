// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** error GET /api/admin/error */
export async function errorUsingGet1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/admin/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** error PUT /api/admin/error */
export async function errorUsingPut1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/admin/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** error POST /api/admin/error */
export async function errorUsingPost1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/admin/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** error DELETE /api/admin/error */
export async function errorUsingDelete1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/admin/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** error PATCH /api/admin/error */
export async function errorUsingPatch1(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/admin/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
