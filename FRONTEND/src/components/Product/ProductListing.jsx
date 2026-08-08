import React, { useState } from 'react';
import { validateProduct } from '../../services/api';
import './ProductListing.css';

function ProductListing() {
    const [formData, setFormData] = useState({
        productId: '',
        sellerId: '',
        title: '',
        description: '',
        price: '',
        brand: '',
        imageUrl: '',
        msrp: ''
    });
    
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setResult(null);

        try {
            const data = {
                productId: formData.productId || 'PROD' + Date.now(),
                sellerId: formData.sellerId || 'SELL001',
                title: formData.title,
                description: formData.description,
                price: parseFloat(formData.price),
                brand: formData.brand,
                imageUrl: formData.imageUrl || 'https://via.placeholder.com/300',
                msrp: parseFloat(formData.msrp)
            };

            const response = await validateProduct(data);
            setResult(response);
        } catch (err) {
            setError('Failed to validate product. Please try again.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // ===== ACTION HANDLERS =====
    const handleApproveListing = (productId) => {
        if (window.confirm(`✅ Approve listing ${productId}?`)) {
            alert(`✅ Listing ${productId} has been APPROVED and published!`);
            setResult(null);
            setFormData({
                productId: '',
                sellerId: '',
                title: '',
                description: '',
                price: '',
                brand: '',
                imageUrl: '',
                msrp: ''
            });
        }
    };

    const handleRejectListing = (productId) => {
        if (window.confirm(`🚫 Are you sure you want to REJECT listing ${productId}?`)) {
            alert(`🚫 Listing ${productId} has been REJECTED.`);
            setResult(null);
            setFormData({
                productId: '',
                sellerId: '',
                title: '',
                description: '',
                price: '',
                brand: '',
                imageUrl: '',
                msrp: ''
            });
        }
    };

    const handleReviewListing = (productId) => {
        alert(`🔍 Reviewing listing: ${productId}\n\nPlease check the product details and images carefully.`);
    };

    const getStatusColor = (status) => {
        if (status === 'FLAG_FOR_REVIEW') return '#b71c1c';
        if (status === 'REVIEW') return '#ff9800';
        return '#4caf50';
    };

    const getStatusIcon = (status) => {
        if (status === 'FLAG_FOR_REVIEW') return '🚫';
        if (status === 'REVIEW') return '🔍';
        return '✅';
    };

    const getActionButton = (status, productId) => {
        if (status === 'FLAG_FOR_REVIEW') {
            return (
                <button 
                    className="btn-reject"
                    onClick={() => handleRejectListing(productId)}
                >
                    🚫 Reject Listing
                </button>
            );
        } else if (status === 'REVIEW') {
            return (
                <button 
                    className="btn-review"
                    onClick={() => handleReviewListing(productId)}
                >
                    🔍 Review Listing
                </button>
            );
        } else {
            return (
                <button 
                    className="btn-approve"
                    onClick={() => handleApproveListing(productId)}
                >
                    ✅ Approve Listing
                </button>
            );
        }
    };

    return (
        <div className="product-page">
            <div className="product-container">
                <h1>📦 Product Authenticity Check</h1>
                <p className="subtitle">Enter product details to detect counterfeits</p>

                <div className="product-grid">
                    {/* Form Section */}
                    <div className="form-section">
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Product ID</label>
                                <input
                                    type="text"
                                    name="productId"
                                    value={formData.productId}
                                    onChange={handleChange}
                                    placeholder="Auto-generated if left blank"
                                />
                            </div>

                            <div className="form-group">
                                <label>Seller ID *</label>
                                <input
                                    type="text"
                                    name="sellerId"
                                    value={formData.sellerId}
                                    onChange={handleChange}
                                    placeholder="Enter seller ID"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>Product Title *</label>
                                <input
                                    type="text"
                                    name="title"
                                    value={formData.title}
                                    onChange={handleChange}
                                    placeholder="Enter product title"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>Description *</label>
                                <textarea
                                    name="description"
                                    value={formData.description}
                                    onChange={handleChange}
                                    placeholder="Enter product description"
                                    rows="3"
                                    required
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group half">
                                    <label>Price (₹) *</label>
                                    <input
                                        type="number"
                                        name="price"
                                        value={formData.price}
                                        onChange={handleChange}
                                        placeholder="Enter price"
                                        required
                                    />
                                </div>
                                <div className="form-group half">
                                    <label>MSRP (₹) *</label>
                                    <input
                                        type="number"
                                        name="msrp"
                                        value={formData.msrp}
                                        onChange={handleChange}
                                        placeholder="Enter MSRP"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label>Brand</label>
                                <input
                                    type="text"
                                    name="brand"
                                    value={formData.brand}
                                    onChange={handleChange}
                                    placeholder="Enter brand name"
                                />
                            </div>

                            <div className="form-group">
                                <label>Image URL</label>
                                <input
                                    type="text"
                                    name="imageUrl"
                                    value={formData.imageUrl}
                                    onChange={handleChange}
                                    placeholder="https://example.com/product.jpg"
                                />
                            </div>

                            <button type="submit" className="btn-analyze" disabled={loading}>
                                {loading ? '🔍 Analyzing...' : '🔍 Check Authenticity'}
                            </button>
                        </form>
                    </div>

                    {/* Results Section */}
                    <div className="results-section">
                        {error && (
                            <div className="error-message">
                                ❌ {error}
                            </div>
                        )}

                        {result && (
                            <div className="result-card">
                                <h2>📊 Authenticity Result</h2>
                                
                                <div className="counterfeit-score">
                                    <div className="score-circle" style={{ borderColor: getStatusColor(result.status) }}>
                                        <span className="score-number">{result.counterfeitScore}%</span>
                                    </div>
                                    <div className="score-details">
                                        <h3 style={{ color: getStatusColor(result.status) }}>
                                            {getStatusIcon(result.status)} {result.status}
                                        </h3>
                                        <p>Product ID: {result.productId}</p>
                                    </div>
                                </div>

                                <div className="product-details">
                                    <h4>Product Details:</h4>
                                    <div className="detail-row">
                                        <span className="label">Title:</span>
                                        <span className="value">{formData.title}</span>
                                    </div>
                                    <div className="detail-row">
                                        <span className="label">Price:</span>
                                        <span className="value">₹{formData.price}</span>
                                    </div>
                                    <div className="detail-row">
                                        <span className="label">MSRP:</span>
                                        <span className="value">₹{formData.msrp}</span>
                                    </div>
                                    <div className="detail-row">
                                        <span className="label">Brand:</span>
                                        <span className="value">{formData.brand || 'N/A'}</span>
                                    </div>
                                </div>

                                <div className="authenticity-reasons">
                                    <h4>Reasons:</h4>
                                    <ul>
                                        {result.reasons.map((reason, index) => (
                                            <li key={index}>{reason}</li>
                                        ))}
                                    </ul>
                                </div>

                                <div className="authenticity-action">
                                    <h4>Recommended Action:</h4>
                                    {getActionButton(result.status, result.productId)}
                                </div>

                                <div className="authenticity-timestamp">
                                    <small>Analyzed at: {new Date().toLocaleString()}</small>
                                </div>
                            </div>
                        )}

                        {!result && !error && (
                            <div className="placeholder">
                                <div className="placeholder-icon">🔍</div>
                                <p>Enter product details and click "Check Authenticity"</p>
                                <small>Powered by AI Authenticity Agent</small>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ProductListing;