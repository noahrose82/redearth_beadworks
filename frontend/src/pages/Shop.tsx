import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api'

type Product = {
  id: string
  name: string
  description: string
  priceCents: number
  tags?: string[]
  contentBlocks?: { type: string, url: string }[]
}

export default function Shop() {
  const [products, setProducts] = useState<Product[]>([])
  const [q, setQ] = useState('')

  async function load() {
    const resp = await api.get('/api/catalog/products', { params: q ? { q } : {} })
    setProducts(resp.data)
  }

  useEffect(() => { load() }, [])

  return (
    <div>
      <div className="row" style={{ marginBottom: 12 }}>
        <input className="input" placeholder="Search products..." value={q} onChange={e => setQ(e.target.value)} />
        <button className="btn" onClick={load}>Search</button>
      </div>
      <div className="grid">
        {products.map(p => {
          const imageUrl = p.contentBlocks?.find(block => block.type === 'image')?.url
          return (
            <Link key={p.id} to={`/product/${p.id}`} className="card product-card">
              {imageUrl && (
                <img className="product-thumb" src={imageUrl} alt={p.name} />
              )}
              <h1 style={{color: 'white'}}>NEW VERSION</h1>
              <h2 style={{color: 'white'}}>NEW SHOP VERSION</h2>
              <h3 style={{ marginTop: 0 }}>{p.name}</h3>
              <div className="small">{p.description}</div>
              <div style={{ marginTop: 8 }}><strong>${(p.priceCents/100).toFixed(2)}</strong></div>
              <div className="small">{p.tags?.join(', ')}</div>
            </Link>
          )
        })}
      </div>
    </div>
  )
}
