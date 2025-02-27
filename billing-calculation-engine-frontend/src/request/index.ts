import axios from "axios"

const instance = axios.create({
    baseURL:"http://localhost:8080",
    timeout:5000,
    headers: {
        'Content-Type': 'application/json',
    }
})

// 请求拦截器
instance.interceptors.request.use(config=>{
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config
},err=>{
    return Promise.reject(err)
});

// 响应拦截器
instance.interceptors.response.use(res => {
    console.log('API Response:', res);
    return res.data
}, err => {
    console.error('API Error:', {
        status: err.response?.status,
        data: err.response?.data,
        config: err.config
    });
    if (err.response?.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }
    return Promise.reject(err)
})

export default instance