import { Link, useNavigate } from 'react-router-dom'
import { useCart } from '../cart'

export default function CartPage() {
  const { items, remove, totalCents } = useCart()
  const nav = useNavigate()

  return (
    <div>
      <div className="hero" style={{ marginBottom: 24 }}>
        <h1>Your Cart</h1>
        <p>
          Review your selected pieces before continuing to checkout. Update your cart and confirm
          your total before placing an order.
        </p>
      </div>

      {items.length === 0 ? (
        <div className="details-panel">
          <h2 style={{ marginTop: 0 }}>Your cart is empty</h2>
          <p className="product-description" style={{ marginBottom: 20 }}>
            You have not added any items yet. Browse the collection to discover handcrafted jewelry
            inspired by Southwestern artistry.
          </p>
          <Link to="/shop" className="btn">Shop Now</Link>
        </div>
      ) : (
        <div className="details-grid">
          <div className="details-panel">
            <h2 style={{ marginTop: 0, marginBottom: 18 }}>Cart Items</h2>

            <div style={{ display: 'grid', gap: 14 }}>
              {items.map(i => (
                <div
                  key={i.productId}
                  className="card"
                  style={{
                    padding: 18,
                    borderRadius: 16,
                    border: '1px solid #e8e1d8',
                    boxShadow: 'none'
                  }}
                >
                  <div
                    className="row"
                    style={{ justifyContent: 'space-between', alignItems: 'flex-start', gap: 18 }}
                  >
                    <div>
                      <div style={{ fontSize: '1.05rem', fontWeight: 800, marginBottom: 6 }}>
                        {i.name}
                      </div>
                      <div className="product-description">
                        ${(i.priceCents / 100).toFixed(2)} × {i.quantity}
                      </div>
                    </div>

                    <button className="btn secondary" onClick={() => remove(i.productId)}>
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="details-panel">
            <h2 style={{ marginTop: 0, marginBottom: 18 }}>Order Summary</h2>

            <div className="meta-list">
              <div className="meta-item">
                <strong>Items:</strong> {items.length}
              </div>
              <div className="meta-item">
                <strong>Estimated Total:</strong> ${(totalCents / 100).toFixed(2)}
              </div>
            </div>

            <div
              style={{
                marginTop: 16,
                paddingTop: 16,
                borderTop: '1px solid #e8e1d8',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
              }}
            >
              <span style={{ fontSize: '1rem', fontWeight: 700 }}>Total</span>
              <span style={{ fontSize: '1.8rem', fontWeight: 800 }}>
                ${(totalCents / 100).toFixed(2)}
              </span>
            </div>

            <div className="row" style={{ marginTop: 20, flexWrap: 'wrap' }}>
              <button className="btn" onClick={() => nav('/checkout')}>
                Proceed to Checkout
              </button>
              <Link to="/shop" className="btn secondary">
                Continue Shopping
              </Link>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}