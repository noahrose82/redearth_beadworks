import React, { createContext, useContext, useEffect, useMemo, useState } from 'react'
import api from './api'

type User = { userId: number, email: string, fullName: string, role: string }
type AuthCtx = {
  user: User | null
  login: (email: string, password: string) => Promise<void>
  logout: () => void
}

const Ctx = createContext<AuthCtx | null>(null)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null)

  async function loadMe() {
    const token = localStorage.getItem('jwt')
    if (!token) return
    try {
      const resp = await api.get('/api/auth/me')
      setUser(resp.data)
    } catch {
      localStorage.removeItem('jwt')
      setUser(null)
    }
  }

  useEffect(() => { loadMe() }, [])

  async function login(email: string, password: string) {
    const resp = await api.post('/api/auth/login', { email, password })
    localStorage.setItem('jwt', resp.data.token)
    setUser({
      userId: resp.data.userId,
      email: resp.data.email,
      fullName: resp.data.fullName,
      role: resp.data.role
    })
  }

  function logout() {
    localStorage.removeItem('jwt')
    setUser(null)
  }

  const value = useMemo(() => ({ user, login, logout }), [user])
  return <Ctx.Provider value={value}>{children}</Ctx.Provider>
}

export function useAuth() {
  const ctx = useContext(Ctx)
  if (!ctx) throw new Error('AuthProvider missing')
  return ctx
}