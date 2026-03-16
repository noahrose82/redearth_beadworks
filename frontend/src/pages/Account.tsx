import { useState } from 'react'
import api from '../api'
import { useAuth } from '../auth'

type OrderSummary = { id: number, status: string, totalCents: number, createdAt: string }

export default function Account() {
  const { user, login, logout } = useAuth()
  const [email, setEmail] = useState('customer@redearth.local')
  const [password, setPassword] = useState('Customer123!')
  const [orders, setOrders] = useState<OrderSummary[]>([])
  const [err, setErr] = useState('')

  async function doLogin() {
    setErr('')
    try {
      await login(email, password)
    } catch {
      setErr('Login failed')
    }
  }

  async function loadOrders() {
    const resp = await api.get('/api/orders/mine')
    setOrders(resp.data)
  }

  if (!user) {
    return (
      <div className="card">
        <h2>Account</h2>
        <p className="small">Login to view orders and checkout.</p>
        <div className="row">
          <input className="input" value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" />
          <input className="input" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" type="password" />
        </div>
        <button className="btn" style={{ marginTop: 12 }} onClick={doLogin}>Login</button>
        {err && <p className="small">{err}</p>}
        <p className="small" style={{ marginTop: 12 }}>
          Demo users: admin@redearth.local / Admin123! and customer@redearth.local / Customer123!
        </p>
      </div>
    )
  }

  return (
    <div className="card">
      <h2>Account</h2>
      <p className="small">Signed in as <strong>{user.email}</strong> ({user.role})</p>
      <div className="row">
        <button className="btn secondary" onClick={loadOrders}>Load My Orders</button>
        <button className="btn secondary" onClick={logout}>Logout</button>
      </div>

      {orders.length > 0 && (
        <div style={{ marginTop: 12 }}>
          <h3>Orders</h3>
          {orders.map(o => (
            <div key={o.id} className="row" style={{ justifyContent:'space-between', marginBottom: 8 }}>
              <div>
                <div><strong>#{o.id}</strong> — {o.status}</div>
                <div className="small">${(o.totalCents/100).toFixed(2)} • {new Date(o.createdAt).toLocaleString()}</div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
