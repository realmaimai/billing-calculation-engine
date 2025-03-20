import { useMutation } from '@tanstack/react-query';
import apiClient from './apiClient';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface UserData {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface ApiResponse<T = any> {
  code: number;
  msg?: string;
  data?: T;
}

export interface AuthData {
  token: string;
  id: string;
}

const authService = {
  // Login
  login: async (credentials: LoginCredentials): Promise<ApiResponse<AuthData>> => {
    const { data } = await apiClient.post<ApiResponse<AuthData>>('/auth/login', credentials);

    if (data?.data?.token) {
      localStorage.setItem('auth_token', data.data.token);
      localStorage.setItem('user_id', data.data.id);
    }

    return data;
  },

  // Register
  register: async (userData: UserData): Promise<ApiResponse> => {
    const { data } = await apiClient.post<ApiResponse>('/auth/register', userData);
    return data;
  },

  // Check if user is authenticated
  isAuthenticated: () => !!localStorage.getItem('auth_token'),

  // Logout
  logout: () => {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_id');
  }
};

export const useLogin = () => {
  return useMutation({
    mutationFn: (credentials: LoginCredentials) => authService.login(credentials)
  });
};

export const useRegister = () => {
  return useMutation({
    mutationFn: (userData: UserData) => authService.register(userData)
  });
};

export default authService;