// 这个文件专门定义请求参数的类型，和响应的类型

interface LoginRequest {
  email: string;
  password: string;
}

interface LoginResponseData {
  id: number;
  username: string;
  firstName: string;
  token: string;
  type: string;
}

interface LoginResponse {
  code: number;
  msg: string;
  data: LoginResponseData;
}