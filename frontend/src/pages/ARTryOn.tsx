import { useEffect, useMemo, useRef, useState } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'

type LocationState = {
  imageUrl?: string
}

export default function ARTryOn() {
  const { id } = useParams()
  const location = useLocation()
  const state = (location.state || {}) as LocationState

  const [userPhoto, setUserPhoto] = useState<string | null>(null)
  const [overlayX, setOverlayX] = useState(220)
  const [overlayY, setOverlayY] = useState(220)
  const [overlayWidth, setOverlayWidth] = useState(140)
  const [rotation, setRotation] = useState(0)
  const [dragging, setDragging] = useState(false)
  const [dragOffset, setDragOffset] = useState({ x: 0, y: 0 })
  const previewRef = useRef<HTMLDivElement | null>(null)

  const productImage = useMemo(() => state.imageUrl || '', [state.imageUrl])

  useEffect(() => {
    resetOverlay()
  }, [productImage])

  function onPhotoChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0]
    if (!file) return

    const reader = new FileReader()
    reader.onload = () => {
      setUserPhoto(String(reader.result))
    }
    reader.readAsDataURL(file)
  }

  function resetOverlay() {
    if (productImage.toLowerCase().includes('earring')) {
      setOverlayX(260)
      setOverlayY(170)
      setOverlayWidth(90)
      setRotation(0)
      return
    }

    if (productImage.toLowerCase().includes('necklace') || productImage.toLowerCase().includes('pendant')) {
      setOverlayX(220)
      setOverlayY(260)
      setOverlayWidth(180)
      setRotation(0)
      return
    }

    if (productImage.toLowerCase().includes('ring')) {
      setOverlayX(260)
      setOverlayY(300)
      setOverlayWidth(80)
      setRotation(0)
      return
    }

    setOverlayX(220)
    setOverlayY(220)
    setOverlayWidth(140)
    setRotation(0)
  }

  function startDrag(e: React.MouseEvent<HTMLImageElement>) {
    const rect = e.currentTarget.getBoundingClientRect()
    setDragging(true)
    setDragOffset({
      x: e.clientX - rect.left,
      y: e.clientY - rect.top
    })
  }

  function startTouchDrag(e: React.TouchEvent<HTMLImageElement>) {
    const touch = e.touches[0]
    const rect = e.currentTarget.getBoundingClientRect()
    setDragging(true)
    setDragOffset({
      x: touch.clientX - rect.left,
      y: touch.clientY - rect.top
    })
  }

  function onDragMove(e: React.MouseEvent<HTMLDivElement>) {
    if (!dragging || !previewRef.current) return

    const rect = previewRef.current.getBoundingClientRect()
    setOverlayX(e.clientX - rect.left - dragOffset.x)
    setOverlayY(e.clientY - rect.top - dragOffset.y)
  }

  function onTouchMove(e: React.TouchEvent<HTMLDivElement>) {
    if (!dragging || !previewRef.current) return

    const touch = e.touches[0]
    const rect = previewRef.current.getBoundingClientRect()
    setOverlayX(touch.clientX - rect.left - dragOffset.x)
    setOverlayY(touch.clientY - rect.top - dragOffset.y)
  }

  function stopDrag() {
    setDragging(false)
  }

  async function downloadPreview() {
    if (!userPhoto || !productImage) return

    const canvas = document.createElement('canvas')
    canvas.width = 700
    canvas.height = 700
    const ctx = canvas.getContext('2d')
    if (!ctx) return

    const baseImg = new Image()
    baseImg.crossOrigin = 'anonymous'
    baseImg.src = userPhoto

    const overlayImg = new Image()
    overlayImg.crossOrigin = 'anonymous'
    overlayImg.src = productImage

    await Promise.all([
      new Promise(resolve => {
        baseImg.onload = resolve
      }),
      new Promise(resolve => {
        overlayImg.onload = resolve
      })
    ])

    ctx.drawImage(baseImg, 0, 0, canvas.width, canvas.height)

    const aspect = overlayImg.height / overlayImg.width
    const overlayHeight = overlayWidth * aspect

    ctx.save()
    ctx.translate(overlayX + overlayWidth / 2, overlayY + overlayHeight / 2)
    ctx.rotate((rotation * Math.PI) / 180)
    ctx.globalAlpha = 0.85
    ctx.drawImage(
      overlayImg,
      -overlayWidth / 2,
      -overlayHeight / 2,
      overlayWidth,
      overlayHeight
    )
    ctx.restore()

    const link = document.createElement('a')
    link.download = `ar-tryon-${id || 'preview'}.png`
    link.href = canvas.toDataURL('image/png')
    link.click()
  }

  return (
    <div className="card">
      <Link to={`/product/${id}`} className="small">← Back to Product</Link>

      <h2 style={{ marginBottom: 8 }}>AR Try-On Preview</h2>
      <p className="small" style={{ marginBottom: 16 }}>
        Upload a photo, then position and size the jewelry preview on top of the image.
      </p>

      <div className="row" style={{ marginBottom: 16, flexWrap: 'wrap' }}>
        <input type="file" accept="image/*" onChange={onPhotoChange} />
        <button className="btn secondary" onClick={resetOverlay}>Reset</button>
        <button className="btn" onClick={downloadPreview} disabled={!userPhoto || !productImage}>
          Download Preview
        </button>
      </div>

      <div className="details-grid">
        <div
          ref={previewRef}
          onMouseMove={onDragMove}
          onMouseUp={stopDrag}
          onMouseLeave={stopDrag}
          onTouchMove={onTouchMove}
          onTouchEnd={stopDrag}
          style={{
            position: 'relative',
            width: 700,
            maxWidth: '100%',
            aspectRatio: '1 / 1',
            overflow: 'hidden',
            borderRadius: 18,
            border: '1px solid #ddd',
            background: '#fff'
          }}
        >
          {userPhoto ? (
            <img
              src={userPhoto}
              alt="User upload"
              style={{
                width: '100%',
                height: '100%',
                objectFit: 'cover',
                display: 'block'
              }}
            />
          ) : (
            <div
              style={{
                width: '100%',
                height: '100%',
                display: 'grid',
                placeItems: 'center',
                color: '#666',
                background: '#f7f7f7'
              }}
            >
              Upload a photo to begin the try-on preview
            </div>
          )}

          {userPhoto && productImage && (
            <img
              src={productImage}
              alt="Jewelry overlay"
              draggable={false}
              onMouseDown={startDrag}
              onTouchStart={startTouchDrag}
              style={{
                position: 'absolute',
                left: overlayX,
                top: overlayY,
                width: overlayWidth,
                transform: `rotate(${rotation}deg)`,
                cursor: 'move',
                userSelect: 'none',
                opacity: 0.85,
                mixBlendMode: 'multiply',
                filter: 'drop-shadow(0 4px 6px rgba(0,0,0,0.25))'
              }}
            />
          )}
        </div>

        <div className="details-panel">
          <h3 style={{ marginTop: 0 }}>Adjust Overlay</h3>

          <div className="meta-list">
            <label className="meta-item">
              <strong>Horizontal Position</strong>
              <input
                className="input"
                type="range"
                min="0"
                max="500"
                value={overlayX}
                onChange={e => setOverlayX(Number(e.target.value))}
              />
            </label>

            <label className="meta-item">
              <strong>Vertical Position</strong>
              <input
                className="input"
                type="range"
                min="0"
                max="500"
                value={overlayY}
                onChange={e => setOverlayY(Number(e.target.value))}
              />
            </label>

            <label className="meta-item">
              <strong>Size</strong>
              <input
                className="input"
                type="range"
                min="60"
                max="320"
                value={overlayWidth}
                onChange={e => setOverlayWidth(Number(e.target.value))}
              />
            </label>

            <label className="meta-item">
              <strong>Rotation</strong>
              <input
                className="input"
                type="range"
                min="-180"
                max="180"
                value={rotation}
                onChange={e => setRotation(Number(e.target.value))}
              />
            </label>
          </div>

          <p className="small">
            This try-on feature allows the user to preview jewelry placement by overlaying the
            selected product image on an uploaded photo.
          </p>
        </div>
      </div>
    </div>
  )
}