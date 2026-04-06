import { useEffect, useMemo, useState } from 'react'
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
  contentBlocks?: { type: string; url: string }[]
}

export default function ProductPage() {
  const { id } = useParams()
  const [p, setP] = useState<Product | null>(null)
  const [allProducts, setAllProducts] = useState<Product[]>([])
  const { add } = useCart()
  const [showAI, setShowAI] = useState(false)
  const [loading, setLoading] = useState(true)

 useEffect(() => {
  setLoading(true)
  api.get(`/api/products/${id}`)
    .then(r => setP(r.data))
    .finally(() => setLoading(false))
}, [id])

  useEffect(() => {
    api.get('/api/products').then(r => setAllProducts(r.data))
  }, [])

  const relatedProducts = useMemo(() => {
    if (!p || !p.tags || p.tags.length === 0) return []

    return allProducts
      .filter(other => other.id !== p.id)
      .map(other => {
        const otherTags = other.tags || []
        const score = otherTags.filter(tag => p.tags?.includes(tag)).length
        return { ...other, score }
      })
      .filter(item => item.score > 0)
      .sort((a, b) => b.score - a.score)
      .slice(0, 4)
  }, [p, allProducts])

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
      <p><strong>${(p.priceCents / 100).toFixed(2)}</strong></p>

      {p.attributes && (
        <div className="small">
          {Object.entries(p.attributes).map(([k, v]) => (
            <div key={k}>
              <strong>{k}:</strong> {String(v)}
            </div>
          ))}
        </div>
      )}

      <div className="row" style={{ marginTop: 12, flexWrap: 'wrap' }}>
        <button
          className="btn"
          onClick={() => add({ productId: p.id, name: p.name, priceCents: p.priceCents })}
        >
          Add to Cart
        </button>

        <button className="btn secondary" onClick={() => setShowAI(v => !v)}>
          AI Recommendations
        </button>

        <Link
          to={`/ar/${p.id}`}
          state={{ imageUrl }}
          className="btn secondary"
        >
          Try On (AR)
        </Link>
      </div>

      {showAI && (
        <div className="card" style={{ marginTop: 12 }}>
          <h3 style={{ marginTop: 0 }}>Recommended for You</h3>
          <p className="small" style={{ marginBottom: 12 }}>
            These recommendations are based on shared product tags and related catalog metadata.
          </p>

          {relatedProducts.length === 0 ? (
            <p className="small">No related products found yet.</p>
          ) : (
            <div className="grid">
              {relatedProducts.map(item => {
                const relatedImage = item.contentBlocks?.find(block => block.type === 'image')?.url

                return (
                  <Link key={item.id} to={`/product/${item.id}`} className="product-card">
                    {relatedImage && (
                      <img className="product-thumb" src={relatedImage} alt={item.name} />
                    )}
                    <h4 className="product-card-title">{item.name}</h4>
                    <p className="product-description">{item.description}</p>
                    <div className="price" style={{ fontSize: '1.1rem' }}>
                      ${(item.priceCents / 100).toFixed(2)}
                    </div>
                  </Link>
                )
              })}
            </div>
          )}
        </div>
      )}
    </div>
  )
}