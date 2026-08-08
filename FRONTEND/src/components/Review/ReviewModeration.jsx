import React, { useState } from 'react';
import { validateReview } from '../../services/api';
import './ReviewModeration.css';

function ReviewModeration() {
    const [formData, setFormData] = useState({
        reviewId: '',
        productId: '',
        userId: '',
        rating: '5',
        reviewText: ''
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
                reviewId: formData.reviewId || 'REV' + Date.now(),
                productId: parseInt(formData.productId) || 1,
                userId: formData.userId || 'USER001',
                rating: parseInt(formData.rating),
                reviewText: formData.reviewText
            };

            const response = await validateReview(data);
            setResult(response);
        } catch (err) {
            setError('Failed to moderate review. Please try again.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // ===== ACTION HANDLERS =====
    const handleApproveReview = (reviewId) => {
        if (window.confirm(`✅ Approve review ${reviewId}?`)) {
            alert(`✅ Review ${reviewId} has been APPROVED and published!`);
            setResult(null);
            setFormData({
                reviewId: '',
                productId: '',
                userId: '',
                rating: '5',
                reviewText: ''
            });
        }
    };

    const handleRemoveReview = (reviewId) => {
        if (window.confirm(`🚫 Are you sure you want to REMOVE review ${reviewId}?`)) {
            alert(`🚫 Review ${reviewId} has been REMOVED.`);
            setResult(null);
            setFormData({
                reviewId: '',
                productId: '',
                userId: '',
                rating: '5',
                reviewText: ''
            });
        }
    };

    const handleReviewReview = (reviewId) => {
        alert(`🔍 Reviewing: ${reviewId}\n\nPlease check the review content carefully.`);
    };

    const getStatusColor = (status) => {
        if (status === 'FLAG_FOR_REVIEW') return '#b71c1c';
        if (status === 'REVIEW') return '#ff9800';
        return '#4caf50';
    };

    const getStatusIcon = (status) => {
        if (status === 'FLAG_FOR_REVIEW') return '📝';
        if (status === 'REVIEW') return '🔍';
        return '✅';
    };

    const getActionButton = (status, reviewId) => {
        if (status === 'FLAG_FOR_REVIEW') {
            return (
                <button 
                    className="btn-remove"
                    onClick={() => handleRemoveReview(reviewId)}
                >
                    🚫 Remove Review
                </button>
            );
        } else if (status === 'REVIEW') {
            return (
                <button 
                    className="btn-review"
                    onClick={() => handleReviewReview(reviewId)}
                >
                    🔍 Review
                </button>
            );
        } else {
            return (
                <button 
                    className="btn-approve"
                    onClick={() => handleApproveReview(reviewId)}
                >
                    ✅ Approve Review
                </button>
            );
        }
    };

    const quickFill = (type) => {
        if (type === 'fake') {
            setFormData({
                ...formData,
                productId: '1',
                userId: 'USER001',
                rating: '5',
                reviewText: 'Amazing!! Best Product!! Excellent!! Highly recommend!! Very Good!!'
            });
        } else if (type === 'bot') {
            setFormData({
                ...formData,
                productId: '1',
                userId: 'USER002',
                rating: '5',
                reviewText: 'GREAT PRODUCT! AMAZING QUALITY! BEST PURCHASE! AWESOME! PERFECT!'
            });
        } else if (type === 'genuine') {
            setFormData({
                ...formData,
                productId: '1',
                userId: 'USER003',
                rating: '4',
                reviewText: 'The product is decent. It took 2 days to arrive. The quality is average for the price. I would recommend it for casual use.'
            });
        }
    };

    return (
        <div className="review-page">
            <div className="review-container">
                <h1>✍️ Review Moderation</h1>
                <p className="subtitle">Analyze reviews for spam, AI-generated text, and fraud</p>

                <div className="quick-fill">
                    <span>Quick Test:</span>
                    <button onClick={() => quickFill('fake')} className="btn-quick fake">Fake Review</button>
                    <button onClick={() => quickFill('bot')} className="btn-quick bot">Bot Pattern</button>
                    <button onClick={() => quickFill('genuine')} className="btn-quick genuine">Genuine Review</button>
                </div>

                <div className="review-grid">
                    <div className="form-section">
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Review ID</label>
                                <input
                                    type="text"
                                    name="reviewId"
                                    value={formData.reviewId}
                                    onChange={handleChange}
                                    placeholder="Auto-generated if left blank"
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group half">
                                    <label>Product ID *</label>
                                    <input
                                        type="number"
                                        name="productId"
                                        value={formData.productId}
                                        onChange={handleChange}
                                        placeholder="Enter product ID"
                                        required
                                    />
                                </div>
                                <div className="form-group half">
                                    <label>User ID *</label>
                                    <input
                                        type="text"
                                        name="userId"
                                        value={formData.userId}
                                        onChange={handleChange}
                                        placeholder="Enter user ID"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label>Rating *</label>
                                <select
                                    name="rating"
                                    value={formData.rating}
                                    onChange={handleChange}
                                    required
                                >
                                    <option value="5">⭐⭐⭐⭐⭐ (5 stars)</option>
                                    <option value="4">⭐⭐⭐⭐ (4 stars)</option>
                                    <option value="3">⭐⭐⭐ (3 stars)</option>
                                    <option value="2">⭐⭐ (2 stars)</option>
                                    <option value="1">⭐ (1 star)</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Review Text *</label>
                                <textarea
                                    name="reviewText"
                                    value={formData.reviewText}
                                    onChange={handleChange}
                                    placeholder="Enter the review text to analyze..."
                                    rows="5"
                                    required
                                />
                            </div>

                            <button type="submit" className="btn-analyze" disabled={loading}>
                                {loading ? '🔍 Analyzing...' : '🔍 Analyze Review'}
                            </button>
                        </form>
                    </div>

                    <div className="results-section">
                        {error && (
                            <div className="error-message">
                                ❌ {error}
                            </div>
                        )}

                        {result && (
                            <div className="result-card">
                                <h2>📊 Review Analysis Result</h2>
                                
                                <div className="fraud-score">
                                    <div className="score-circle" style={{ borderColor: getStatusColor(result.status) }}>
                                        <span className="score-number">{result.fraudScore}%</span>
                                    </div>
                                    <div className="score-details">
                                        <h3 style={{ color: getStatusColor(result.status) }}>
                                            {getStatusIcon(result.status)} {result.status}
                                        </h3>
                                        <p>Review ID: {result.reviewId}</p>
                                    </div>
                                </div>

                                <div className="review-text-display">
                                    <h4>Review Content:</h4>
                                    <div className="review-text-box">
                                        "{formData.reviewText}"
                                    </div>
                                </div>

                                <div className="review-details">
                                    <div className="detail-row">
                                        <span className="label">Rating:</span>
                                        <span className="value">⭐ {formData.rating}/5</span>
                                    </div>
                                    <div className="detail-row">
                                        <span className="label">User:</span>
                                        <span className="value">{formData.userId}</span>
                                    </div>
                                    <div className="detail-row">
                                        <span className="label">Product:</span>
                                        <span className="value">#{formData.productId}</span>
                                    </div>
                                </div>

                                <div className="fraud-reasons">
                                    <h4>Reasons:</h4>
                                    <ul>
                                        {result.reasons.map((reason, index) => (
                                            <li key={index}>{reason}</li>
                                        ))}
                                    </ul>
                                </div>

                                <div className="fraud-action">
                                    <h4>Recommended Action:</h4>
                                    {getActionButton(result.status, result.reviewId)}
                                </div>

                                <div className="fraud-timestamp">
                                    <small>Analyzed at: {new Date().toLocaleString()}</small>
                                </div>
                            </div>
                        )}

                        {!result && !error && (
                            <div className="placeholder">
                                <div className="placeholder-icon">🔍</div>
                                <p>Enter review details and click "Analyze Review"</p>
                                <small>Powered by AI Review Moderation Agent</small>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ReviewModeration;