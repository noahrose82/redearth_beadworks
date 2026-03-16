import React, { createContext, useContext, useMemo, useState } from 'react'

export type CartItem = { productId: string, name: string, priceCents: number, quantity: number }

type CartCtx = {
  items: CartItem[]
  add: (item: Omit<CartItem, 'quantity'>) => void
  remove: (productId: string) => void
  clear: () => void
  totalCents: number
}

const Ctx = createContext<CartCtx | null>(null)

export function CartProvider({ children }: { children: React.ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([])

  function add(item: Omit<CartItem, 'quantity'>) {
    setItems(prev => {
      const existing = prev.find(p => p.productId === item.productId)
      if (existing) return prev.map(p => p.productId === item.productId ? { ...p, quantity: p.quantity + 1 } : p)
      return [...prev, { ...item, quantity: 1 }]
    })
  }

  function remove(productId: string) {
    setItems(prev => prev.filter(p => p.productId !== productId))
  }

  function clear() { setItems([]) }

  const totalCents = items.reduce((sum, i) => sum + i.priceCents * i.quantity, 0)

  const value = useMemo(() => ({ items, add, remove, clear, totalCents }), [items, totalCents])
  return <Ctx.Provider value={value}>{children}</Ctx.Provider>
}

export function useCart() {
  const ctx = useContext(Ctx)
  if (!ctx) throw new Error('CartProvider missing')
  return ctx
}
