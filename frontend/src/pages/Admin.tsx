import { useEffect, useState } from 'react'
import api from '../api'

type Product = { id?: string, name: string, description: string, priceCents: number, tags: string[], attributes?: any }

export default function Admin() {
  const [products, setProducts] = useState<Product[]>([])
  const [draft, setDraft] = useState<Product>({
    name: '',
    description: '',
    priceCents: 1000,
    tags: ['handmade'],
    attributes: { material: 'turquoise' }
  })
  const [msg, setMsg] = useState('')

  async function load() {
    const resp = await api.get('/api/catalog/products')
    setProducts(resp.data)
  }

  async function create() {
    setMsg('')
    const resp = await api.post('/api/catalog/products', draft)
    setMsg(`Created ${resp.data.name}`)
    setDraft({ ...draft, name: '', description: '' })
    await load()
  }

  useEffect(() => { load() }, [])

  return (
    <div className="card">
      <h2>Admin Dashboard</h2>
      <p className="small">Create products in MongoDB (ADMIN only).</p>

      <div className="row">
        <input className="input" placeholder="Name" value={draft.name} onChange={e => setDraft({ ...draft, name: e.target.value })} />
        <input className="input" placeholder="Price (cents)" type="number" value={draft.priceCents} onChange={e => setDraft({ ...draft, priceCents: Number(e.target.value) })} />
      </div>
      <div style={{ marginTop: 8 }}>
        <input className="input" placeholder="Description" value={draft.description} onChange={e => setDraft({ ...draft, description: e.target.value })} />
      </div>
      <button className="btn" style={{ marginTop: 12 }} onClick={create}>Create Product</button>
      {msg && <p className="small">{msg}</p>}

      <h3 style={{ marginTop: 16 }}>All Products</h3>
      <div className="grid">
        {products.map(p => (
          <div className="card" key={p.id}>
            <strong>{p.name}</strong>
            <div className="small">${(p.priceCents/100).toFixed(2)}</div>
            <div className="small">{p.description}</div>
            <div className="small">{p.tags?.join(', ')}</div>
          </div>
        ))}
      </div>
    </div>
  )
}
