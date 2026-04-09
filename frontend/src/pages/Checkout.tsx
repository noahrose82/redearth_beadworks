import { useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api'
import { useCart } from '../cart'

type OrderDetail = {
  id: number
  status: string
  totalCents: number
  items: any[]
}

type Intent = {
  intentId: string
  clientSecret: string
  amountCents: number
  provider: string
}

export default function Checkout() {
  const { items, totalCents, clear } = useCart()

  const [order, setOrder] = useState<OrderDetail | null>(null)
  const [intent, setIntent] = useState<Intent | null>(null)
  const [msg, setMsg] = useState('')
  const [processing, setProcessing] = useState(false)

  const [fullName, setFullName] = useState('')
  const [email, setEmail] = useState('')
  const [cardName, setCardName] = useState('')
  const [cardNumber, setCardNumber] = useState('')
  const [expiry, setExpiry] = useState('')
  const [cvv, setCvv] = useState('')
  const [address, setAddress] = useState('')
  const [city, setCity] = useState('')
  const [state, setState] = useState('')
  const [zip, setZip] = useState('')
  const [err, setErr] = useState('')

  async function createOrder() {
    setMsg('')
    setErr('')

    const token = localStorage.getItem('jwt')
    if (!token) {
      setErr('You must be logged in to place an order.')
      return
    }

    if (items.length === 0) {
      setErr('Cart is empty')
      return
    }

    try {
      const resp = await api.post('/api/orders', {
        items: items.map(i => ({
          productId: i.productId,
          quantity: i.quantity,
        })),
      })

      setOrder(resp.data)
    } catch (e) {
      console.error(e)
      setErr('Failed to create order')
    }
  }

  async function createIntent() {
    if (!order) return

    setErr('')
    setMsg('')

    try {
      const resp = await api.post('/api/checkout/intent', {
        orderId: order.id,
      })

      setIntent(resp.data)
    } catch (e) {
      console.error(e)
      setErr('Failed to create payment intent')
    }
  }

  async function confirmPayment() {
    if (!order || !intent) return

    setProcessing(true)
    setErr('')
    setMsg('')

    try {
      if (
        !fullName ||
        !email ||
        !cardName ||
        !cardNumber ||
        !expiry ||
        !cvv ||
        !address ||
        !city ||
        !state ||
        !zip
      ) {
        setErr('Please complete all payment and billing fields.')
        return
      }

      if (cardNumber.replace(/\s/g, '').length < 12) {
        setErr('Please enter a valid card number.')
        return
      }

      if (cvv.length < 3) {
        setErr('Please enter a valid CVV.')
        return
      }

      if (items.length === 0) {
        setErr('Cart is empty')
        return
      }

      const resp = await api.post('/api/checkout/confirm', {
        orderId: order.id,
        intentId: intent.intentId,
      })

      setMsg(`Payment ${resp.data.paymentStatus}. Order is now ${resp.data.orderStatus}.`)
      clear()
    } catch (e) {
      console.error(e)
      setErr('Failed to confirm payment')
    } finally {
      setProcessing(false)
    }
  }

  if (items.length === 0 && !order) {
    return (
      <div className="card">
        <h2>Checkout</h2>
        <p className="small">Your cart is empty.</p>
        <Link to="/shop" className="btn">
          Return to Shop
        </Link>
      </div>
    )
  }

  return (
    <div className="details-grid">
      <div className="details-panel">
        <h2 style={{ marginTop: 0 }}>Payment Information</h2>
        <p className="small" style={{ marginBottom: 16 }}>
          Enter billing and payment details to complete your order.
        </p>

        <div style={{ display: 'grid', gap: 12 }}>
          <input
            className="input"
            placeholder="Full Name"
            value={fullName}
            onChange={e => setFullName(e.target.value)}
          />

          <input
            className="input"
            placeholder="Email"
            value={email}
            onChange={e => setEmail(e.target.value)}
          />

          <input
            className="input"
            placeholder="Name on Card"
            value={cardName}
            onChange={e => setCardName(e.target.value)}
          />

          <input
            className="input"
            placeholder="Card Number"
            value={cardNumber}
            onChange={e => setCardNumber(e.target.value)}
          />

          <div className="row">
            <input
              className="input"
              placeholder="MM/YY"
              value={expiry}
              onChange={e => setExpiry(e.target.value)}
            />
            <input
              className="input"
              placeholder="CVV"
              value={cvv}
              onChange={e => setCvv(e.target.value)}
            />
          </div>

          <input
            className="input"
            placeholder="Billing Address"
            value={address}
            onChange={e => setAddress(e.target.value)}
          />

          <div className="row">
            <input
              className="input"
              placeholder="City"
              value={city}
              onChange={e => setCity(e.target.value)}
            />
            <input
              className="input"
              placeholder="State"
              value={state}
              onChange={e => setState(e.target.value)}
            />
            <input
              className="input"
              placeholder="ZIP"
              value={zip}
              onChange={e => setZip(e.target.value)}
            />
          </div>
        </div>

        {err && (
          <p style={{ marginTop: 14, color: 'darkred' }}>
            <strong>{err}</strong>
          </p>
        )}

        {msg && (
          <p style={{ marginTop: 14, color: 'green' }}>
            <strong>{msg}</strong>
          </p>
        )}
      </div>

      <div className="details-panel">
        <h2 style={{ marginTop: 0 }}>Order Summary</h2>

        <div style={{ display: 'grid', gap: 12, marginBottom: 18 }}>
          {items.map(i => (
            <div key={i.productId} className="row" style={{ justifyContent: 'space-between' }}>
              <div>
                <div>
                  <strong>{i.name}</strong>
                </div>
                <div className="small">Qty: {i.quantity}</div>
              </div>
              <div>
                <strong>${((i.priceCents * i.quantity) / 100).toFixed(2)}</strong>
              </div>
            </div>
          ))}
        </div>

        <hr style={{ border: 'none', borderTop: '1px solid #ddd', margin: '16px 0' }} />

        <div className="row" style={{ justifyContent: 'space-between', marginBottom: 16 }}>
          <strong>Total</strong>
          <strong>${(totalCents / 100).toFixed(2)}</strong>
        </div>

        {!order ? (
          <button className="btn" onClick={createOrder}>
            Create Order
          </button>
        ) : !intent ? (
          <div>
            <div className="small" style={{ marginBottom: 8 }}>
              Order #{order.id} — {order.status}
            </div>
            <button className="btn" onClick={createIntent}>
              Create Payment Intent
            </button>
          </div>
        ) : (
          <div>
            <div className="small" style={{ marginBottom: 8 }}>
              Order #{order.id} — {order.status}
            </div>
            <div className="small" style={{ marginBottom: 8 }}>
              Payment Provider: {intent.provider}
            </div>
            <button className="btn" onClick={confirmPayment} disabled={processing}>
              {processing ? 'Processing...' : 'Confirm Payment'}
            </button>
          </div>
        )}
      </div>
    </div>
  )
}