import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../api'
import { useCart } from '../cart'

type Product = {
  id: string
  name: string
  description: string
  priceCents: number
  tags?: string[]
  attributes?: Record<string, any>
  contentBlocks?: { type: string, url: string }[]
}

export default function ProductPage() {
  const { id } = useParams()
  const [p, setP] = useState<Product | null>(null)
  const { add } = useCart()
  const [showAR, setShowAR] = useState(false)
  const [showAI, setShowAI] = useState(false)

  useEffect(() => {
    api.get(`/api/catalog/products/${id}`).then(r => setP(r.data))
  }, [id])

  if (!p) return <div className="card">Loading...</div>

  const imageUrl = p.contentBlocks?.find(block => block.type === 'image')?.url

  return (
    <div className="card">
      <Link to="/shop" className="small">← Back to Shop</Link>
      {imageUrl && (
        <img className="product-hero" src={imageUrl} alt={p.name} />
      )}
      <h2>{p.name}</h2>
      <p className="small">{p.description}</p>
      <p><strong>${(p.priceCents/100).toFixed(2)}</strong></p>

      <div className="small">
        Attributes: {p.attributes ? JSON.stringify(p.attributes) : '—'}
      </div>

      <div className="row" style={{ marginTop: 12 }}>
        <button className="btn" onClick={() => add({ productId: p.id, name: p.name, priceCents: p.priceCents })}>
          Add to Cart
        </button>
        <button className="btn secondary" onClick={() => setShowAI(v => !v)}>AI Recommendations (stub)</button>
        <button className="btn secondary" onClick={() => setShowAR(v => !v)}>AR Try-On (stub)</button>
      </div>

      {showAI && (
        <div className="card" style={{ marginTop: 12 }}>
          <h3 style={{ marginTop: 0 }}>AI Recommendations (demo)</h3>
          <p className="small">
            In the architecture plan this is powered by TensorFlow.js / recommendation helpers.
            For the local build, this is a safe placeholder you can expand later.
          </p>
          <ul className="small">
            <li>“Similar tags” suggestion: {p.tags?.[0] ?? 'handmade'} items</li>
            <li>“Frequently bought with” (future)</li>
          </ul>
        </div>
      )}

      {showAR && (
        <div className="card" style={{ marginTop: 12 }}>
          <h3 style={{ marginTop: 0 }}>AR Try-On (demo)</h3>
          <p className="small">
            In production this would use WebXR to overlay the jewelry. For now we simulate a “try-on” panel.
          </p>
          <div className="small">[WebXR overlay placeholder]</div>
        </div>
      )}
    </div>
  )
}
