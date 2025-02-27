import request from "./index"

// 登录请求
export const LoginAPI = (params: LoginRequest): Promise<LoginResponse> => 
  request.post("/api/v1/auth/login", params);
