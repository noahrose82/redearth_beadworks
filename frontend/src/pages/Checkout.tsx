import { useState } from 'react'
import api from '../api'
import { useCart } from '../cart'

type OrderDetail = { id: number, status: string, totalCents: number, items: any[] }
type Intent = { intentId: string, clientSecret: string, amountCents: number, provider: string }

export default function Checkout() {
  const { items, totalCents, clear } = useCart()
  const [order, setOrder] = useState<OrderDetail | null>(null)
  const [intent, setIntent] = useState<Intent | null>(null)
  const [msg, setMsg] = useState<string>('')

  async function createOrder() {
  setMsg('')
  setOrder({
    id: Math.floor(Math.random() * 1000),
    status: 'CREATED',
    totalCents: totalCents,
    items: items
  })
}

  async function createIntent() {
  if (!order) return
  setIntent({
    intentId: `pi_${Date.now()}`,
    clientSecret: `secret_${Date.now()}`,
    amountCents: order.totalCents,
    provider: 'demo'
  })
}

  async function confirmPayment() {
  if (!order || !intent) return
  setMsg('Payment succeeded. Order is now PAID.')
  clear()
}

  if (items.length === 0 && !order) {
    return <div className="card">Cart is empty.</div>
  }

  return (
    <div className="card">
      <h2>Checkout</h2>
      <p className="small">This uses a Stripe-safe local stub (Payment Intent + Confirm) so you can demo end-to-end.</p>

      {!order ? (
        <>
          <div className="small">Total: <strong>${(totalCents/100).toFixed(2)}</strong></div>
          <button className="btn" style={{ marginTop: 12 }} onClick={createOrder}>Create Order</button>
        </>
      ) : (
        <>
          <div className="small">Order #{order.id} — Status: <strong>{order.status}</strong></div>
          <div className="small">Amount: <strong>${(order.totalCents/100).toFixed(2)}</strong></div>

          {!intent ? (
            <button className="btn" style={{ marginTop: 12 }} onClick={createIntent}>Create Payment Intent</button>
          ) : (
            <div style={{ marginTop: 12 }}>
              <div className="small">Intent: {intent.intentId}</div>
              <div className="small">Client Secret: {intent.clientSecret.slice(0, 18)}…</div>
              <button className="btn" style={{ marginTop: 8 }} onClick={confirmPayment}>Confirm Payment</button>
            </div>
          )}

          {msg && <p style={{ marginTop: 12 }}><strong>{msg}</strong></p>}
        </>
      )}
    </div>
  )
}
