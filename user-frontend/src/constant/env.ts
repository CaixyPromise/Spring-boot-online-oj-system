
export const API_DEV = 'http://localhost:8100';

export const API_PRODUCTION = 'https://api.caixyowo.cn';

export const BASE_URL = process.env.NODE_ENV === 'development' ? API_DEV : API_PRODUCTION;
