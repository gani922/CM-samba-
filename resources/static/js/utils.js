/**
 * Utility functions for the authentication system
 */

class Utils {
    /**
     * Show toast notification
     * @param {string} message - The message to display
     * @param {string} type - Type of toast (success, error, warning, info)
     * @param {number} duration - Duration in milliseconds
     */
    static showToast(message, type = 'info', duration = 3000) {
        const toast = document.getElementById('toast');
        if (!toast) return;

        toast.textContent = message;
        toast.className = `toast ${type}`;
        toast.classList.add('show');

        setTimeout(() => {
            toast.classList.remove('show');
        }, duration);
    }

    /**
     * Validate email format
     * @param {string} email - Email to validate
     * @returns {boolean} - True if valid
     */
    static validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    /**
     * Validate password strength
     * @param {string} password - Password to validate
     * @returns {object} - Strength score and messages
     */
    static validatePasswordStrength(password) {
        const checks = {
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            numbers: /\d/.test(password),
            special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
        };

        const score = Object.values(checks).filter(Boolean).length;
        let strength = 'Very weak';
        let color = '#f72585';

        if (score >= 4) {
            strength = 'Strong';
            color = '#4cc9f0';
        } else if (score >= 3) {
            strength = 'Moderate';
            color = '#f8961e';
        } else if (score >= 2) {
            strength = 'Weak';
            color = '#f8961e';
        }

        return {
            score,
            strength,
            color,
            checks
        };
    }

    /**
     * Format date to readable string
     * @param {Date|string} date - Date to format
     * @returns {string} - Formatted date string
     */
    static formatDate(date) {
        const d = new Date(date);
        return d.toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    /**
     * Format time difference
     * @param {Date} date - Date to compare
     * @returns {string} - Human readable time difference
     */
    static timeAgo(date) {
        const now = new Date();
        const diff = now - new Date(date);
        const minutes = Math.floor(diff / 60000);
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);

        if (days > 0) return `${days} day${days > 1 ? 's' : ''} ago`;
        if (hours > 0) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
        if (minutes > 0) return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
        return 'Just now';
    }

    /**
     * Generate a simple CAPTCHA
     * @returns {object} - CAPTCHA code and display
     */
    static generateCaptcha() {
        const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789';
        let code = '';
        for (let i = 0; i < 6; i++) {
            code += chars.charAt(Math.floor(Math.random() * chars.length));
        }

        // Create distorted text for display
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        canvas.width = 200;
        canvas.height = 60;

        // Background
        ctx.fillStyle = '#f8f9fa';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        // Add noise
        for (let i = 0; i < 100; i++) {
            ctx.fillStyle = `rgba(${Math.random() * 100}, ${Math.random() * 100}, ${Math.random() * 100}, 0.1)`;
            ctx.fillRect(
                Math.random() * canvas.width,
                Math.random() * canvas.height,
                2, 2
            );
        }

        // Draw text with distortion
        ctx.font = 'bold 30px Arial';
        ctx.fillStyle = '#2b2d42';

        for (let i = 0; i < code.length; i++) {
            const x = 20 + i * 30;
            const y = 40 + Math.random() * 10 - 5;
            const rotation = Math.random() * 0.3 - 0.15;

            ctx.save();
            ctx.translate(x, y);
            ctx.rotate(rotation);
            ctx.fillText(code.charAt(i), 0, 0);
            ctx.restore();
        }

        // Add lines
        for (let i = 0; i < 5; i++) {
            ctx.beginPath();
            ctx.moveTo(Math.random() * canvas.width, Math.random() * canvas.height);
            ctx.lineTo(Math.random() * canvas.width, Math.random() * canvas.height);
            ctx.strokeStyle = `rgba(67, 97, 238, ${0.3 + Math.random() * 0.3})`;
            ctx.lineWidth = 1;
            ctx.stroke();
        }

        return {
            code,
            display: canvas.toDataURL()
        };
    }

    /**
     * Debounce function to limit function calls
     * @param {Function} func - Function to debounce
     * @param {number} wait - Wait time in milliseconds
     * @returns {Function} - Debounced function
     */
    static debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    /**
     * Get token from localStorage
     * @returns {string|null} - Token or null
     */
    static getToken() {
        return localStorage.getItem('authToken');
    }

    /**
     * Save token to localStorage
     * @param {string} token - Token to save
     */
    static saveToken(token) {
        localStorage.setItem('authToken', token);
    }

    /**
     * Remove token from localStorage
     */
    static removeToken() {
        localStorage.removeItem('authToken');
    }

    /**
     * Check if user is authenticated
     * @returns {boolean} - True if authenticated
     */
    static isAuthenticated() {
        const token = this.getToken();
        if (!token) return false;

        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.exp * 1000 > Date.now();
        } catch (e) {
            return false;
        }
    }

    /**
     * Get user info from token
     * @returns {object|null} - User info or null
     */
    static getUserInfo() {
        const token = this.getToken();
        if (!token) return null;

        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return {
                username: payload.sub,
                role: payload.role,
                adminId: payload.adminId,
                exp: payload.exp
            };
        } catch (e) {
            return null;
        }
    }

    /**
     * Redirect to login if not authenticated
     */
    static requireAuth() {
        if (!this.isAuthenticated()) {
            window.location.href = 'index.html';
            return false;
        }
        return true;
    }
}

// Make Utils available globally
window.Utils = Utils;