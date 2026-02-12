/**
 * API service for backend communication
 */

class APIService {
    constructor() {
        this.baseURL = 'http://localhost:8080/api/admin';
        this.defaultHeaders = {
            'Content-Type': 'application/json',
        };
    }

    /**
     * Set authorization header
     * @param {string} token - JWT token
     */
    setAuthToken(token) {
        this.defaultHeaders['Authorization'] = `Bearer ${token}`;
    }

    /**
     * Remove authorization header
     */
    removeAuthToken() {
        delete this.defaultHeaders['Authorization'];
    }

    /**
     * Make HTTP request
     * @param {string} url - Endpoint URL
     * @param {string} method - HTTP method
     * @param {object} data - Request data
     * @returns {Promise} - Response data
     */
    async request(url, method = 'GET', data = null) {
        const options = {
            method,
            headers: { ...this.defaultHeaders },
            credentials: 'include'
        };

        if (data) {
            options.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(`${this.baseURL}${url}`, options);

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            return await response.text();
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }

    // Authentication APIs

    /**
     * User login
     * @param {object} credentials - Login credentials
     * @returns {Promise} - Login response
     */
    async login(credentials) {
        return this.request('/login', 'POST', credentials);
    }

    /**
     * Refresh token
     * @param {string} refreshToken - Refresh token
     * @returns {Promise} - New tokens
     */
    async refreshToken(refreshToken) {
        return this.request('/refresh-token', 'POST', { refreshToken });
    }

    /**
     * Validate token
     * @param {string} token - JWT token
     * @returns {Promise} - Validation result
     */
    async validateToken(token) {
        return this.request(`/validate-token?token=${encodeURIComponent(token)}`, 'GET');
    }

    /**
     * User logout
     * @param {string} token - JWT token
     * @returns {Promise} - Logout result
     */
    async logout(token) {
        const headers = { ...this.defaultHeaders };
        headers['Authorization'] = `Bearer ${token}`;

        const options = {
            method: 'POST',
            headers,
            credentials: 'include'
        };

        const response = await fetch(`${this.baseURL}/logout`, options);
        return response.ok;
    }

    /**
     * Forgot password
     * @param {object} data - Email and phone number
     * @returns {Promise} - Reset link response
     */
    async forgotPassword(data) {
        return this.request('/forgot-password', 'POST', data);
    }

    /**
     * Reset password with token
     * @param {object} data - Token and new password
     * @returns {Promise} - Reset result
     */
    async resetPassword(data) {
        return this.request('/reset-password', 'POST', data);
    }

    /**
     * Change password
     * @param {object} data - Current and new password
     * @returns {Promise} - Change result
     */
    async changePassword(data) {
        return this.request('/change-password', 'POST', data);
    }

    // Admin APIs

    /**
     * Get admin info
     * @param {string} adminId - Admin ID
     * @returns {Promise} - Admin information
     */
    async getAdminInfo(adminId) {
        return this.request(`/info?adminId=${encodeURIComponent(adminId)}`, 'GET');
    }

    /**
     * Get admin profile
     * @param {string} adminId - Admin ID
     * @returns {Promise} - Admin profile
     */
    async getAdminProfile(adminId) {
        return this.request(`/profile?adminId=${encodeURIComponent(adminId)}`, 'GET');
    }

    /**
     * Validate session
     * @param {string} adminId - Admin ID
     * @returns {Promise} - Session validation result
     */
    async validateSession(adminId) {
        return this.request(`/validate-session?adminId=${encodeURIComponent(adminId)}`, 'GET');
    }

    /**
     * Get organizations
     * @param {string} adminId - Admin ID
     * @returns {Promise} - Organizations list
     */
    async getOrganizations(adminId) {
        return this.request(`/organizations?adminId=${encodeURIComponent(adminId)}`, 'GET');
    }

    // Mock data for dashboard (replace with real API calls)

    /**
     * Get dashboard stats
     * @returns {Promise} - Dashboard statistics
     */
    async getDashboardStats() {
        // This would be a real API call in production
        return new Promise(resolve => {
            setTimeout(() => {
                resolve({
                    totalCustomers: 1245,
                    totalOrders: 342,
                    totalRevenue: 45230,
                    activeUsers: 89,
                    revenueTrend: [12000, 19000, 30000, 50000, 40000, 60000, 45230],
                    customerGrowth: [400, 600, 800, 1000, 1100, 1200, 1245],
                    recentActivities: [
                        {
                            id: 1,
                            type: 'user',
                            title: 'New Customer Registration',
                            description: 'John Doe registered as a new customer',
                            time: new Date(Date.now() - 3600000).toISOString()
                        },
                        {
                            id: 2,
                            type: 'order',
                            title: 'Order Completed',
                            description: 'Order #ORD-2024-001 has been completed',
                            time: new Date(Date.now() - 7200000).toISOString()
                        },
                        {
                            id: 3,
                            type: 'system',
                            title: 'System Update',
                            description: 'Security patches applied to the system',
                            time: new Date(Date.now() - 10800000).toISOString()
                        }
                    ]
                });
            }, 500);
        });
    }
}

// Create singleton instance
const api = new APIService();

// Initialize API with token if exists
const token = Utils.getToken();
if (token) {
    api.setAuthToken(token);
}

// Make API available globally
window.api = api;