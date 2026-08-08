import React, { useState, useEffect } from 'react';
import Checkout from './components/Checkout/Checkout';
import ProductListing from './components/Product/ProductListing';
import ReviewModeration from './components/Review/ReviewModeration';  // ← ADD THIS
import { getDashboardStats } from './services/api';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
    PieChart, Pie, Cell, LineChart, Line
} from 'recharts';
import './App.css';

function App() {
    const [currentPage, setCurrentPage] = useState('dashboard');
    const [dashboardData, setDashboardData] = useState({
        totalOrders: 0,
        fraudOrders: 0,
        counterfeitListings: 0,
        fakeReviews: 0,
        todaySavings: 0
    });
    const [loading, setLoading] = useState(true);

    // Sample trend data (will be replaced with real data later)
    const trendData = [
        { day: 'Mon', orders: 45, fraud: 5 },
        { day: 'Tue', orders: 52, fraud: 8 },
        { day: 'Wed', orders: 38, fraud: 3 },
        { day: 'Thu', orders: 60, fraud: 12 },
        { day: 'Fri', orders: 55, fraud: 7 },
        { day: 'Sat', orders: 42, fraud: 4 },
        { day: 'Sun', orders: 30, fraud: 2 },
    ];

    const riskData = [
        { name: 'High Risk', value: dashboardData.fraudOrders || 1 },
        { name: 'Medium Risk', value: 5 },
        { name: 'Low Risk', value: dashboardData.totalOrders - (dashboardData.fraudOrders || 1) - 5 },
    ];

    const savingsData = [
        { day: 'Mon', savings: 12000 },
        { day: 'Tue', savings: 18000 },
        { day: 'Wed', savings: 10000 },
        { day: 'Thu', savings: 25000 },
        { day: 'Fri', savings: 20000 },
        { day: 'Sat', savings: 15000 },
        { day: 'Sun', savings: 125000 },
    ];

    const COLORS = ['#b71c1c', '#ff9800', '#4caf50'];

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        setLoading(true);
        const data = await getDashboardStats();
        if (data) {
            setDashboardData(data);
        }
        setLoading(false);
    };

    // Dashboard Content
    const renderDashboard = () => (
        <div className="dashboard">
            <h1>📊 Trust &amp; Safety Dashboard</h1>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '50px' }}>
                    <h2>🔄 Loading data from backend...</h2>
                </div>
            ) : (
                <>
                    {/* Stats Cards */}
                    <div className="stats-grid">
                        <div className="stat-card blue">
                            <h3>Total Orders</h3>
                            <p className="number">{dashboardData.totalOrders}</p>
                        </div>
                        <div className="stat-card red">
                            <h3>Fraud Orders</h3>
                            <p className="number">{dashboardData.fraudOrders}</p>
                        </div>
                        <div className="stat-card orange">
                            <h3>Counterfeit Listings</h3>
                            <p className="number">{dashboardData.counterfeitListings}</p>
                        </div>
                        <div className="stat-card purple">
                            <h3>Fake Reviews</h3>
                            <p className="number">{dashboardData.fakeReviews}</p>
                        </div>
                    </div>

                    {/* Today's Savings */}
                    <div className="savings-card">
                        <h2>💰 Today's Savings</h2>
                        <p className="savings-amount">₹{dashboardData.todaySavings.toLocaleString()}</p>
                    </div>

                    {/* Charts Row 1 */}
                    <div className="charts-row">
                        <div className="chart-card">
                            <h3>📈 Fraud Trends</h3>
                            <ResponsiveContainer width="100%" height={250}>
                                <BarChart data={trendData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="day" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Bar dataKey="orders" fill="#3949ab" name="Total Orders" />
                                    <Bar dataKey="fraud" fill="#e53935" name="Fraud Orders" />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        <div className="chart-card">
                            <h3>🎯 Risk Distribution</h3>
                            <ResponsiveContainer width="100%" height={250}>
                                <PieChart>
                                    <Pie
                                        data={riskData}
                                        cx="50%"
                                        cy="50%"
                                        labelLine={true}
                                        label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                                        outerRadius={80}
                                        fill="#8884d8"
                                        dataKey="value"
                                    >
                                        {riskData.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                        ))}
                                    </Pie>
                                    <Tooltip />
                                </PieChart>
                            </ResponsiveContainer>
                        </div>
                    </div>

                    {/* Chart Row 2 - Savings Trend */}
                    <div className="charts-row">
                        <div className="chart-card full-width">
                            <h3>📊 Daily Savings Trend</h3>
                            <ResponsiveContainer width="100%" height={250}>
                                <LineChart data={savingsData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="day" />
                                    <YAxis />
                                    <Tooltip formatter={(value) => [`₹${value.toLocaleString()}`, 'Savings']} />
                                    <Legend />
                                    <Line type="monotone" dataKey="savings" stroke="#2e7d32" strokeWidth={3} dot={{ r: 5 }} />
                                </LineChart>
                            </ResponsiveContainer>
                        </div>
                    </div>

                    {/* Recent Alerts */}
                    <div className="alerts-section">
                        <h2>🚨 Recent Alerts</h2>
                        <div className="alert-item high">
                            <span className="alert-icon">⚠️</span>
                            <span><strong>Transaction #TXN001</strong> - High Risk (85%) - 2 min ago</span>
                            <span className="alert-badge">COD • 5 returns</span>
                        </div>
                        <div className="alert-item medium">
                            <span className="alert-icon">🚫</span>
                            <span><strong>Product #PRD001</strong> - Counterfeit (75%) - 5 min ago</span>
                            <span className="alert-badge">Price mismatch</span>
                        </div>
                        <div className="alert-item high">
                            <span className="alert-icon">📝</span>
                            <span><strong>Review #REV001</strong> - Fake Review (80%) - 10 min ago</span>
                            <span className="alert-badge">Bot pattern</span>
                        </div>
                        <div className="alert-item low">
                            <span className="alert-icon">✅</span>
                            <span><strong>Transaction #TXN002</strong> - Low Risk (10%) - 15 min ago</span>
                            <span className="alert-badge">Approved</span>
                        </div>
                    </div>
                </>
            )}
        </div>
    );

    return (
        <div className="app">
            <header className="header">
                <div className="logo">🔒 AI TrustShield</div>
                <nav className="nav">
                    <span 
                        className={currentPage === 'dashboard' ? 'active' : ''} 
                        onClick={() => setCurrentPage('dashboard')}
                    >
                        Dashboard
                    </span>
                    <span 
                        className={currentPage === 'checkout' ? 'active' : ''} 
                        onClick={() => setCurrentPage('checkout')}
                    >
                        Checkout
                    </span>
                    <span 
                        className={currentPage === 'listings' ? 'active' : ''} 
                        onClick={() => setCurrentPage('listings')}
                    >
                        Listings
                    </span>
                    <span 
                        className={currentPage === 'reviews' ? 'active' : ''} 
                        onClick={() => setCurrentPage('reviews')}
                    >
                        Reviews
                    </span>
                </nav>
            </header>

            {currentPage === 'dashboard' && renderDashboard()}
            {currentPage === 'checkout' && <Checkout />}
            {currentPage === 'listings' && <ProductListing />}
            {currentPage === 'reviews' && <ReviewModeration />}  {/* ← REPLACED THIS */}
        </div>
    );
}

export default App;