import { useEffect, useState } from 'react'
import api from '../api'

type Product = {
  id?: string
  name: string
  description: string
  priceCents: number
  tags: string[]
  attributes?: any
  contentBlocks?: { type: string; url: string }[]
}

type OrderSummary = {
  id: number
  status: string
  totalCents: number
  createdAt: string
}

const emptyDraft: Product = {
  name: '',
  description: '',
  priceCents: 1000,
  tags: ['handmade'],
  attributes: { material: 'turquoise' },
  contentBlocks: [{ type: 'image', url: '' }]
}

export default function Admin() {
  const [products, setProducts] = useState<Product[]>([])
  const [orders, setOrders] = useState<OrderSummary[]>([])
  const [draft, setDraft] = useState<Product>(emptyDraft)
  const [editingId, setEditingId] = useState<string | null>(null)
  const [msg, setMsg] = useState('')

  async function loadProducts() {
    const resp = await api.get('/api/products')
    setProducts(resp.data)
  }

  async function loadOrders() {
    const resp = await api.get('/api/admin/orders')
    setOrders(resp.data)
  }

  async function loadAll() {
    await Promise.all([loadProducts(), loadOrders()])
  }

  function startEdit(product: Product) {
    setEditingId(product.id || null)
    setDraft({
      id: product.id,
      name: product.name,
      description: product.description,
      priceCents: product.priceCents,
      tags: product.tags || [],
      attributes: product.attributes || { material: 'turquoise' },
      contentBlocks: product.contentBlocks?.length
        ? product.contentBlocks
        : [{ type: 'image', url: '' }]
    })
    setMsg('')
  }

  function cancelEdit() {
    setEditingId(null)
    setDraft(emptyDraft)
    setMsg('')
  }

  async function saveProduct() {
    setMsg('')

    const cleanDraft = {
      ...draft,
      tags: draft.tags.filter(Boolean),
      contentBlocks: draft.contentBlocks?.filter(b => b.url?.trim())
    }

    if (editingId) {
      const resp = await api.put(`/api/products/${editingId}`, cleanDraft)
      setMsg(`Updated ${resp.data.name}`)
    } else {
      const resp = await api.post('/api/products', cleanDraft)
      setMsg(`Created ${resp.data.name}`)
    }

    setDraft(emptyDraft)
    setEditingId(null)
    await loadProducts()
  }

  async function removeProduct(id?: string) {
    if (!id) return
    setMsg('')
    await api.delete(`/api/products/${id}`)
    setMsg('Product deleted')
    if (editingId === id) {
      cancelEdit()
    }
    await loadProducts()
  }

  async function updateOrderStatus(orderId: number, status: string) {
    setMsg('')
    await api.put(`/api/admin/orders/${orderId}`, { status })
    setMsg(`Order #${orderId} updated to ${status}`)
    await loadOrders()
  }

  useEffect(() => {
    loadAll()
  }, [])

  return (
    <div style={{ display: 'grid', gap: 24 }}>
      <div className="card">
        <h2>Admin Dashboard</h2>
        <p className="small">Manage products and orders (ADMIN only).</p>

        <h3 style={{ marginTop: 16 }}>
          {editingId ? 'Edit Product' : 'Create Product'}
        </h3>

        <div className="row" style={{ marginBottom: 8 }}>
          <input
            className="input"
            placeholder="Name"
            value={draft.name}
            onChange={e => setDraft({ ...draft, name: e.target.value })}
          />
          <input
            className="input"
            placeholder="Price (cents)"
            type="number"
            value={draft.priceCents}
            onChange={e => setDraft({ ...draft, priceCents: Number(e.target.value) })}
          />
        </div>

        <div style={{ marginBottom: 8 }}>
          <input
            className="input"
            placeholder="Description"
            value={draft.description}
            onChange={e => setDraft({ ...draft, description: e.target.value })}
          />
        </div>

        <div className="row" style={{ marginBottom: 8 }}>
          <input
            className="input"
            placeholder="Tags (comma separated)"
            value={draft.tags.join(', ')}
            onChange={e =>
              setDraft({
                ...draft,
                tags: e.target.value.split(',').map(tag => tag.trim())
              })
            }
          />
        </div>

        <div style={{ marginBottom: 8 }}>
          <input
            className="input"
            placeholder="Image URL (example: /cuff1.jpg)"
            value={draft.contentBlocks?.[0]?.url || ''}
            onChange={e =>
              setDraft({
                ...draft,
                contentBlocks: [{ type: 'image', url: e.target.value }]
              })
            }
          />
        </div>

        <div className="row" style={{ marginTop: 12 }}>
          <button className="btn" onClick={saveProduct}>
            {editingId ? 'Update Product' : 'Create Product'}
          </button>

          {editingId && (
            <button className="btn secondary" onClick={cancelEdit}>
              Cancel
            </button>
          )}
        </div>

        {msg && <p className="small" style={{ marginTop: 12 }}>{msg}</p>}
      </div>

      <div className="card">
        <h3 style={{ marginTop: 0 }}>All Products</h3>

        <div className="grid">
          {products.map(p => {
            const imageUrl = p.contentBlocks?.find(block => block.type === 'image')?.url

            return (
              <div className="card" key={p.id}>
                {imageUrl && (
                  <img
                    src={imageUrl}
                    alt={p.name}
                    className="product-thumb"
                    style={{ marginBottom: 10 }}
                  />
                )}

                <strong>{p.name}</strong>
                <div className="small">${(p.priceCents / 100).toFixed(2)}</div>
                <div className="small" style={{ margin: '6px 0' }}>{p.description}</div>
                <div className="small">{p.tags?.join(', ')}</div>

                <div className="row" style={{ marginTop: 12 }}>
                  <button
                    className="btn secondary"
                    onClick={() => startEdit(p)}
                  >
                    Edit
                  </button>

                  <button
                    className="btn secondary"
                    onClick={() => removeProduct(p.id)}
                  >
                    Delete
                  </button>
                </div>
              </div>
            )
          })}
        </div>
      </div>

      <div className="card">
        <h3 style={{ marginTop: 0 }}>Manage Orders</h3>

        {orders.length === 0 ? (
          <p className="small">No orders found.</p>
        ) : (
          <div style={{ display: 'grid', gap: 12 }}>
            {orders.map(order => (
              <div key={order.id} className="card">
                <div className="row" style={{ justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <div><strong>Order #{order.id}</strong></div>
                    <div className="small">
                      ${(order.totalCents / 100).toFixed(2)} • {new Date(order.createdAt).toLocaleString()}
                    </div>
                    <div className="small">Status: {order.status}</div>
                  </div>

                  <div className="row" style={{ flexWrap: 'wrap' }}>
                    <button
                      className="btn secondary"
                      onClick={() => updateOrderStatus(order.id, 'NEW')}
                    >
                      NEW
                    </button>
                    <button
                      className="btn secondary"
                      onClick={() => updateOrderStatus(order.id, 'PAID')}
                    >
                      PAID
                    </button>
                    <button
                      className="btn secondary"
                      onClick={() => updateOrderStatus(order.id, 'SHIPPED')}
                    >
                      SHIPPED
                    </button>
                    <button
                      className="btn secondary"
                      onClick={() => updateOrderStatus(order.id, 'CANCELLED')}
                    >
                      CANCELLED
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}