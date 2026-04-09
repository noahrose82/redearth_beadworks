import axios from 'axios'

const API_BASE =
  import.meta.env.VITE_API_BASE_URL ||
  'https://redearthbeadworks-production.up.railway.app'

const api = axios.create({
  baseURL: API_BASE,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwt')

  if (token) {
    config.headers = config.headers ?? {}
    ;(config.headers as Record<string, string>)['Authorization'] = `Bearer ${token}`
  }

  return config
})

export default api