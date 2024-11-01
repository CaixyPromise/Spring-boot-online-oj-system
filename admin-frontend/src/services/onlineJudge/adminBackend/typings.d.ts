declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
    succeed?: boolean;
  };

  type BaseResponsePageQuestion_ = {
    code?: number;
    data?: PageQuestion_;
    message?: string;
    succeed?: boolean;
  };

  type BaseResponsePageUserAdminVO_ = {
    code?: number;
    data?: PageUserAdminVO_;
    message?: string;
    succeed?: boolean;
  };

  type BaseResponseUserAdminVO_ = {
    code?: number;
    data?: UserAdminVO;
    message?: string;
    succeed?: boolean;
  };

  type DeleteRequest = {
    id?: number;
  };

  type getUserByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type JudgeCase = {
    input?: string;
    output?: string;
  };

  type JudgeConfig = {
    memoryLimit?: number;
    stackLimit?: number;
    timeLimit?: number;
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

  type PageQuestion_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Question[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserAdminVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserAdminVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type Question = {
    acceptedNum?: number;
    answer?: string;
    content?: string;
    createTime?: string;
    creatorId?: number;
    difficulty?: number;
    favourNum?: number;
    id?: number;
    isDelete?: number;
    judgeCase?: string;
    judgeConfig?: string;
    submitNum?: number;
    tags?: string;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
  };

  type QuestionAddRequest = {
    answer?: string;
    content?: string;
    difficulty?: 'EASY' | 'MEDIUM' | 'HARD';
    judgeCase?: JudgeCase[];
    judgeConfig?: JudgeConfig;
    tags?: string[];
    title?: string;
  };

  type QuestionQueryRequest = {
    answer?: string;
    content?: string;
    current?: number;
    id?: number;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type QuestionUpdateRequest = {
    answer?: string;
    content?: string;
    id?: number;
    judgeCase?: JudgeCase[];
    judgeConfig?: JudgeConfig;
    tags?: string[];
    title?: string;
  };

  type UserAdminVO = {
    createTime?: string;
    githubId?: number;
    id?: number;
    isDelete?: number;
    nickName?: string;
    unionId?: string;
    updateTime?: string;
    userActive?: 'ACTIVE' | 'DISABLED';
    userAvatar?: string;
    userEmail?: string;
    userGender?: number;
    userPhone?: string;
    userRole?: 'ADMIN' | 'USER' | 'BAN';
  };

  type UserQueryRequest = {
    current?: number;
    email?: string;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    sortField?: string;
    sortOrder?: string;
  };

  type View = {
    contentType?: string;
  };
}
