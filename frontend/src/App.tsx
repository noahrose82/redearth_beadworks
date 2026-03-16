import React from 'react'
import { Routes, Route, Link, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './auth'
import { CartProvider, useCart } from './cart'
import Home from './pages/Home'
import Shop from './pages/Shop'
import Product from './pages/Product'
import Cart from './pages/Cart'
import Checkout from './pages/Checkout'
import Account from './pages/Account'
import Admin from './pages/Admin'

function Nav() {
  const { user, logout } = useAuth()
  const { items } = useCart()

  return (
    <div className="nav">
      <Link to="/" className="nav-brand">Red Earth</Link>
      <Link to="/shop" className="nav-link">Shop</Link>
      <Link to="/cart" className="nav-link">Cart ({items.length})</Link>

      <div className="nav-spacer" />

      {user ? (
        <>
          <Link to="/account" className="nav-link">{user.fullName}</Link>
          {user.role === 'ADMIN' && <Link to="/admin" className="nav-link">Admin</Link>}
          <button className="btn secondary" onClick={logout}>Logout</button>
        </>
      ) : (
        <Link to="/account" className="nav-link">Login</Link>
      )}
    </div>
  )
}

function RequireAuth({ children }: { children: JSX.Element }) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/account" replace />
  return children
}

function RequireAdmin({ children }: { children: JSX.Element }) {
  const { user } = useAuth()
  if (!user || user.role !== 'ADMIN') return <Navigate to="/" replace />
  return children
}

export default function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <Nav />
        <div className="container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/shop" element={<Shop />} />
            <Route path="/product/:id" element={<Product />} />
            <Route path="/cart" element={<Cart />} />
            <Route path="/checkout" element={<RequireAuth><Checkout /></RequireAuth>} />
            <Route path="/account" element={<Account />} />
            <Route path="/admin" element={<RequireAdmin><Admin /></RequireAdmin>} />
          </Routes>
        </div>
      </CartProvider>
    </AuthProvider>
  )
}