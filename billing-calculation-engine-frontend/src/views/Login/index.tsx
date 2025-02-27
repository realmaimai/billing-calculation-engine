import { useState } from "react"
import { Input, Button, message } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { useNavigate } from "react-router-dom"
import AuthService from "@/services/AuthService"

const view = () => {
  let navigateTo = useNavigate();
  const [loading, setLoading] = useState(false);
  const [usernameVal, setUsernameVal] = useState("");
  const [passwordVal, setPasswordVal] = useState("");

  const handleLogin = async () => {
    if (!usernameVal.trim() || !passwordVal.trim()) {
      message.warning("Please enter both email and password");
      return;
    }

    setLoading(true);
    try {
      const response = await AuthService.login({
        email: usernameVal,
        password: passwordVal
      });
      AuthService.setAuthData({ code: 200, msg: "Login successful", data: response });
      navigateTo("/page1");
    } catch (error) {
      message.error("Login failed. Please check your credentials.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50">
      <div className="w-full max-w-[400px] p-8 mx-4 bg-white rounded-2xl shadow-[0_4px_12px_rgba(0,0,0,0.1)] transition-all duration-300 hover:shadow-[0_4px_24px_rgba(0,0,0,0.12)]">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-2xl font-semibold text-slate-800 mb-2">
            Welcome Back
          </h1>
          <p className="text-slate-600">
            Please sign in to continue
          </p>
        </div>

        {/* Form */}
        <div className="space-y-6">
          <div className="space-y-5">
            {/* Email Input */}
            <div className="space-y-1.5">
              <label className="block text-sm font-medium text-slate-700">
                Email
              </label>
              <Input
                prefix={<UserOutlined className="text-slate-400" />}
                placeholder="Enter your email"
                onChange={(e) => setUsernameVal(e.target.value)}
                disabled={loading}
                className="h-11 !bg-white border-slate-200 hover:border-blue-500 focus:border-blue-500 focus:shadow-[0_0_0_3px_rgba(59,130,246,0.1)] transition-all duration-300"
              />
            </div>

            {/* Password Input */}
            <div className="space-y-1.5">
              <label className="block text-sm font-medium text-slate-700">
                Password
              </label>
              <Input.Password
                prefix={<LockOutlined className="text-slate-400" />}
                placeholder="Enter your password"
                onChange={(e) => setPasswordVal(e.target.value)}
                disabled={loading}
                className="h-11 !bg-white border-slate-200 hover:border-blue-500 focus:border-blue-500 focus:shadow-[0_0_0_3px_rgba(59,130,246,0.1)] transition-all duration-300"
              />
            </div>
          </div>

          {/* Sign In Button */}
          <Button
            type="primary"
            block
            onClick={handleLogin}
            loading={loading}
            className="h-11 bg-blue-500 hover:bg-blue-600 active:bg-blue-700 border-0 shadow-sm text-base font-medium transition-all duration-300"
          >
            Sign In
          </Button>

          {/* Footer */}
          <div className="text-center space-y-4 pt-2">
            <a 
              href="#" 
              className="inline-block text-sm text-blue-500 hover:text-blue-600 font-medium transition-colors duration-200"
            >
              Forgot password?
            </a>
            <div className="flex items-center justify-center text-sm text-slate-500 gap-2">
              <LockOutlined className="text-emerald-500" />
              <span>Secure SSL Connection</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default view