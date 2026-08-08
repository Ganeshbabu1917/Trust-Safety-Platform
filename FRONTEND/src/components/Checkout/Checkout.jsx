import React, { useState } from 'react';
import { validateTransaction } from '../../services/api';
import './Checkout.css';

function Checkout() {
    const [formData, setFormData] = useState({
        customerId: '',
        orderAmount: '',
        paymentMethod: 'COD',
        previousReturns: '0',
        deviceId: '',
        ipAddress: ''
    });
    
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showReviewModal, setShowReviewModal] = useState(false);

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
                customerId: formData.customerId,
                orderAmount: parseFloat(formData.orderAmount),
                paymentMethod: formData.paymentMethod,
                previousReturns: parseInt(formData.previousReturns) || 0,
                deviceId: formData.deviceId || 'DEV001',
                ipAddress: formData.ipAddress || '192.168.1.1'
            };

            const response = await validateTransaction(data);
            setResult(response);
        } catch (err) {
            setError('Failed to validate transaction. Please try again.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // ===== ACTION HANDLERS =====
    const handleReviewTransaction = (transactionId, riskScore, reasons, status) => {
        setShowReviewModal(true);
        // Store review data for modal
        window.reviewData = {
            transactionId,
            riskScore,
            reasons,
            status
        };
    };

    const handleBlockTransaction = (transactionId) => {
        if (window.confirm(`⚠️ Are you sure you want to BLOCK transaction ${transactionId}?`)) {
            alert(`✅ Transaction ${transactionId} has been BLOCKED.`);
            // Here you would call an API to update status
        }
    };

    const handleApproveTransaction = (transactionId) => {
        if (window.confirm(`✅ Approve transaction ${transactionId}?`)) {
            alert(`✅ Transaction ${transactionId} has been APPROVED.`);
            // Here you would call an API to update status
        }
    };

    const getStatusColor = (status) => {
        if (status === 'HIGH_RISK') return '#b71c1c';
        if (status === 'MEDIUM_RISK') return '#ff9800';
        return '#4caf50';
    };

    const getStatusIcon = (status) => {
        if (status === 'HIGH_RISK') return '🔴';
        if (status === 'MEDIUM_RISK') return '🟡';
        return '🟢';
    };

    const getActionButton = (action, transactionId, riskScore, reasons, status) => {
        if (action === 'MANUAL_VERIFICATION') {
            return (
                <button 
                    className="btn-block"
                    onClick={() => handleBlockTransaction(transactionId)}
                >
                    ⛔ Block Transaction
                </button>
            );
        } else if (action === 'REVIEW') {
            return (
                <button 
                    className="btn-review"
                    onClick={() => handleReviewTransaction(transactionId, riskScore, reasons, status)}
                >
                    🔍 Review Transaction
                </button>
            );
        } else {
            return (
                <button 
                    className="btn-approve"
                    onClick={() => handleApproveTransaction(transactionId)}
                >
                    ✅ Approve Transaction
                </button>
            );
        }
    };

    // ===== MODAL COMPONENT =====
    const ReviewModal = () => {
        const data = window.reviewData;
        if (!data) return null;

        return (
            <div className="modal-overlay" onClick={() => setShowReviewModal(false)}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <h2>📋 Review Transaction</h2>
                    <div className="modal-body">
                        <p><strong>Transaction ID:</strong> {data.transactionId}</p>
                        <p><strong>Risk Score:</strong> <span style={{color: getStatusColor(data.status)}}>{data.riskScore}%</span></p>
                        <p><strong>Status:</strong> {getStatusIcon(data.status)} {data.status}</p>
                        <p><strong>Reasons:</strong></p>
                        <ul>
                            {data.reasons.map((reason, i) => (
                                <li key={i}>{reason}</li>
                            ))}
                        </ul>
                    </div>
                    <div className="modal-actions">
                        <button 
                            className="btn-approve" 
                            onClick={() => {
                                alert('✅ Transaction Approved!');
                                setShowReviewModal(false);
                            }}
                        >
                            ✅ Approve
                        </button>
                        <button 
                            className="btn-block" 
                            onClick={() => {
                                alert('⛔ Transaction Blocked!');
                                setShowReviewModal(false);
                            }}
                        >
                            ⛔ Block
                        </button>
                        <button 
                            className="btn-close" 
                            onClick={() => setShowReviewModal(false)}
                        >
                            Close
                        </button>
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div className="checkout-page">
            <div className="checkout-container">
                <h1>🛒 Checkout Risk Analysis</h1>
                <p className="subtitle">Enter transaction details for fraud detection</p>

                <div className="checkout-grid">
                    {/* Form Section */}
                    <div className="form-section">
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Customer ID *</label>
                                <input
                                    type="text"
                                    name="customerId"
                                    value={formData.customerId}
                                    onChange={handleChange}
                                    placeholder="Enter customer ID"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>Order Amount (₹) *</label>
                                <input
                                    type="number"
                                    name="orderAmount"
                                    value={formData.orderAmount}
                                    onChange={handleChange}
                                    placeholder="Enter order amount"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>Payment Method *</label>
                                <select
                                    name="paymentMethod"
                                    value={formData.paymentMethod}
                                    onChange={handleChange}
                                    required
                                >
                                    <option value="COD">Cash on Delivery (COD)</option>
                                    <option value="ONLINE">Online Payment</option>
                                    <option value="CARD">Credit/Debit Card</option>
                                    <option value="UPI">UPI</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Previous Returns</label>
                                <input
                                    type="number"
                                    name="previousReturns"
                                    value={formData.previousReturns}
                                    onChange={handleChange}
                                    placeholder="Number of previous returns"
                                    min="0"
                                />
                            </div>

                            <div className="form-group">
                                <label>Device ID</label>
                                <input
                                    type="text"
                                    name="deviceId"
                                    value={formData.deviceId}
                                    onChange={handleChange}
                                    placeholder="Enter device ID"
                                />
                            </div>

                            <div className="form-group">
                                <label>IP Address</label>
                                <input
                                    type="text"
                                    name="ipAddress"
                                    value={formData.ipAddress}
                                    onChange={handleChange}
                                    placeholder="Enter IP address"
                                />
                            </div>

                            <button type="submit" className="btn-analyze" disabled={loading}>
                                {loading ? '🔍 Analyzing...' : '🚀 Analyze Risk'}
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
                                <h2>📊 Risk Analysis Result</h2>
                                
                                <div className="risk-score">
                                    <div className="score-circle" style={{ borderColor: getStatusColor(result.status) }}>
                                        <span className="score-number">{result.riskScore}%</span>
                                    </div>
                                    <div className="score-details">
                                        <h3 style={{ color: getStatusColor(result.status) }}>
                                            {getStatusIcon(result.status)} {result.status}
                                        </h3>
                                        <p>Transaction ID: {result.transactionId}</p>
                                    </div>
                                </div>

                                <div className="risk-reasons">
                                    <h4>Reasons:</h4>
                                    <ul>
                                        {result.reasons.map((reason, index) => (
                                            <li key={index}>{reason}</li>
                                        ))}
                                    </ul>
                                </div>

                                <div className="risk-action">
                                    <h4>Recommended Action:</h4>
                                    {getActionButton(
                                        result.action, 
                                        result.transactionId, 
                                        result.riskScore, 
                                        result.reasons, 
                                        result.status
                                    )}
                                </div>

                                <div className="risk-timestamp">
                                    <small>Analyzed at: {new Date().toLocaleString()}</small>
                                </div>
                            </div>
                        )}

                        {!result && !error && (
                            <div className="placeholder">
                                <div className="placeholder-icon">🔍</div>
                                <p>Enter transaction details and click "Analyze Risk"</p>
                                <small>Powered by AI Risk Scoring Agent</small>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            {/* Review Modal */}
            {showReviewModal && <ReviewModal />}
        </div>
    );
}

export default Checkout;