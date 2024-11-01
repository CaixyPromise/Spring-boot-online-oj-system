declare namespace API {
  type AboutMeVO = {
    createTime?: string;
    id?: number;
    nickName?: string;
    userActive?: 'ACTIVE' | 'DISABLED';
    userAvatar?: string;
    userEmail?: string;
    userGender?: number;
    userRole?: 'ADMIN' | 'USER' | 'BAN';
  };

  type BaseResponseAboutMeVO_ = {
    code?: number;
    data?: AboutMeVO;
    message?: string;
    succeed?: boolean;
  };

  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
    succeed?: boolean;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
    succeed?: boolean;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
    succeed?: boolean;
  };

  type DeleteRequest = {
    id?: number;
  };

  type LoginUserVO = {
    id?: number;
    token?: string;
    tokenExpiration?: number;
  };

  type ModelAndView = {
    empty?: boolean;
    model?: Record<string, any>;
    modelMap?: Record<string, any>;
    reference?: boolean;
    status?:
      | 'CONTINUE'
      | 'SWITCHING_PROTOCOLS'
      | 'PROCESSING'
      | 'CHECKPOINT'
      | 'OK'
      | 'CREATED'
      | 'ACCEPTED'
      | 'NON_AUTHORITATIVE_INFORMATION'
      | 'NO_CONTENT'
      | 'RESET_CONTENT'
      | 'PARTIAL_CONTENT'
      | 'MULTI_STATUS'
      | 'ALREADY_REPORTED'
      | 'IM_USED'
      | 'MULTIPLE_CHOICES'
      | 'MOVED_PERMANENTLY'
      | 'FOUND'
      | 'MOVED_TEMPORARILY'
      | 'SEE_OTHER'
      | 'NOT_MODIFIED'
      | 'USE_PROXY'
      | 'TEMPORARY_REDIRECT'
      | 'PERMANENT_REDIRECT'
      | 'BAD_REQUEST'
      | 'UNAUTHORIZED'
      | 'PAYMENT_REQUIRED'
      | 'FORBIDDEN'
      | 'NOT_FOUND'
      | 'METHOD_NOT_ALLOWED'
      | 'NOT_ACCEPTABLE'
      | 'PROXY_AUTHENTICATION_REQUIRED'
      | 'REQUEST_TIMEOUT'
      | 'CONFLICT'
      | 'GONE'
      | 'LENGTH_REQUIRED'
      | 'PRECONDITION_FAILED'
      | 'PAYLOAD_TOO_LARGE'
      | 'REQUEST_ENTITY_TOO_LARGE'
      | 'URI_TOO_LONG'
      | 'REQUEST_URI_TOO_LONG'
      | 'UNSUPPORTED_MEDIA_TYPE'
      | 'REQUESTED_RANGE_NOT_SATISFIABLE'
      | 'EXPECTATION_FAILED'
      | 'I_AM_A_TEAPOT'
      | 'INSUFFICIENT_SPACE_ON_RESOURCE'
      | 'METHOD_FAILURE'
      | 'DESTINATION_LOCKED'
      | 'UNPROCESSABLE_ENTITY'
      | 'LOCKED'
      | 'FAILED_DEPENDENCY'
      | 'TOO_EARLY'
      | 'UPGRADE_REQUIRED'
      | 'PRECONDITION_REQUIRED'
      | 'TOO_MANY_REQUESTS'
      | 'REQUEST_HEADER_FIELDS_TOO_LARGE'
      | 'UNAVAILABLE_FOR_LEGAL_REASONS'
      | 'INTERNAL_SERVER_ERROR'
      | 'NOT_IMPLEMENTED'
      | 'BAD_GATEWAY'
      | 'SERVICE_UNAVAILABLE'
      | 'GATEWAY_TIMEOUT'
      | 'HTTP_VERSION_NOT_SUPPORTED'
      | 'VARIANT_ALSO_NEGOTIATES'
      | 'INSUFFICIENT_STORAGE'
      | 'LOOP_DETECTED'
      | 'BANDWIDTH_LIMIT_EXCEEDED'
      | 'NOT_EXTENDED'
      | 'NETWORK_AUTHENTICATION_REQUIRED';
    view?: View;
    viewName?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type UserModifyPasswordRequest = {
    confirmPassword?: string;
    newPassword?: string;
    oldPassword?: string;
  };

  type UserQueryRequest = {
    current?: number;
    email?: string;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    sortField?: string;
    sortOrder?: string;
  };

  type UserUpdateRequest = {
    id?: number;
    userAvatar?: string;
    userGender?: number;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserUpdateSelfRequest = {
    userAvatar?: string;
    userGender?: number;
    userName?: string;
    userProfile?: string;
  };

  type UserVO = {
    createTime?: string;
    id?: number;
    nickName?: string;
    userActive?: 'ACTIVE' | 'DISABLED';
    userAvatar?: string;
    userEmail?: string;
    userRole?: 'ADMIN' | 'USER' | 'BAN';
  };

  type View = {
    contentType?: string;
  };
}
