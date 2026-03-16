import { Link } from 'react-router-dom'

export default function Home() {
  return (
    <div>
      <div className="hero">
        <h1>Handcrafted Jewelry Inspired by the Southwest</h1>
        <p>
          Explore beadwork, silver pieces, and artisan-inspired designs that blend traditional
          influence with modern style. Red Earth Beadworks brings together craftsmanship,
          culture, and curated detail.
        </p>

        <div className="row" style={{ marginTop: 24, flexWrap: 'wrap' }}>
          <Link to="/shop" className="btn">Shop Collection</Link>
          <Link to="/account" className="btn secondary">My Account</Link>
        </div>
      </div>

      <section style={{ marginTop: 12 }}>
        <h2 className="section-title">Featured Categories</h2>

        <div className="grid">
          <div className="card">
            <h3 style={{ marginTop: 0 }}>Bracelets</h3>
            <p className="product-description">
              Discover handcrafted bracelets featuring beadwork, silver accents,
              and Southwestern-inspired patterns.
            </p>
          </div>

          <div className="card">
            <h3 style={{ marginTop: 0 }}>Necklaces</h3>
            <p className="product-description">
              Explore statement necklaces, feather pendants, and layered designs
              rooted in artisan tradition.
            </p>
          </div>

          <div className="card">
            <h3 style={{ marginTop: 0 }}>Earrings</h3>
            <p className="product-description">
              Shop elegant drop earrings, hoop styles, and handcrafted silver pieces
              designed to stand out.
            </p>
          </div>
        </div>
      </section>

      <section style={{ marginTop: 32 }}>
        <div className="card" style={{ padding: 28 }}>
          <h2 className="section-title" style={{ marginBottom: 10 }}>Why Red Earth Beadworks?</h2>
          <p className="product-description" style={{ maxWidth: 900 }}>
            Red Earth Beadworks was designed as a modern ecommerce experience centered around
            handcrafted jewelry inspired by Southwestern design, natural materials, and cultural artistry.
            The platform demonstrates a full-stack architecture with a React frontend, Spring Boot backend,
            MongoDB product catalog, MySQL transactional data, and future-ready AI and AR extensions.
          </p>
        </div>
      </section>
    </div>
  )
}