import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Dashboard API
export const getDashboardStats = async () => {
    try {
        const response = await api.get('/dashboard/stats');
        return response.data;
    } catch (error) {
        console.error('Error fetching dashboard stats:', error);
        return {
            totalOrders: 0,
            fraudOrders: 0,
            counterfeitListings: 0,
            fakeReviews: 0,
            todaySavings: 0
        };
    }
};

// Risk Scoring API
export const validateTransaction = async (transactionData) => {
    try {
        const response = await api.post('/checkout/validate', transactionData);
        return response.data;
    } catch (error) {
        console.error('Error validating transaction:', error);
        return null;
    }
};

// Authenticity API
export const validateProduct = async (productData) => {
    try {
        const response = await api.post('/products/validate', productData);
        return response.data;
    } catch (error) {
        console.error('Error validating product:', error);
        return null;
    }
};

// Review Moderation API
export const validateReview = async (reviewData) => {
    try {
        const response = await api.post('/reviews/validate', reviewData);
        return response.data;
    } catch (error) {
        console.error('Error validating review:', error);
        return null;
    }
};

export default api;