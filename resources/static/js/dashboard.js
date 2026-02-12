/**
 * Dashboard functionality
 */

class Dashboard {
    constructor() {
        this.charts = {};
        this.tokenRefreshInterval = null;
        this.init();
    }

    init() {
        // Check authentication
        if (!Utils.requireAuth()) return;

        // Setup event listeners
        this.setupEventListeners();

        // Load dashboard data
        this.loadDashboardData();

        // Start token refresh timer
        this.startTokenRefreshTimer();

        // Update current date
        this.updateCurrentDate();
    }

    setupEventListeners() {
        // Menu toggle
        const menuToggle = document.getElementById('menuToggle');
        if (menuToggle) {
            menuToggle.addEventListener('click', () => this.toggleSidebar());
        }

        // User profile dropdown
        const userProfile = document.getElementById('userProfile');
        if (userProfile) {
            userProfile.addEventListener('click', (e) => this.toggleDropdown(e));
        }

        // Close dropdown when clicking outside
        document.addEventListener('click', (e) => {
            const dropdown = document.getElementById('dropdownMenu');
            if (dropdown && !userProfile.contains(e.target)) {
                dropdown.classList.remove('show');
            }
        });

        // Notifications
        const notificationBtn = document.getElementById('notificationBtn');
        const notificationPanel = document.getElementById('notificationPanel');
        const closeNotifications = document.getElementById('closeNotifications');

        if (notificationBtn) {
            notificationBtn.addEventListener('click', () => this.toggleNotifications());
        }

        if (closeNotifications) {
            closeNotifications.addEventListener('click', () => this.closeNotifications());
        }

        // Close notifications when clicking outside
        document.addEventListener('click', (e) => {
            if (notificationPanel &&
                !notificationBtn.contains(e.target) &&
                !notificationPanel.contains(e.target)) {
                this.closeNotifications();
            }
        });

        // Sidebar menu items
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => this.handleMenuClick(e));
        });

        // Profile modal
        const profileLink = document.getElementById('profileLink');
        const closeProfileModal = document.getElementById('closeProfileModal');
        const profileModal = document.getElementById('profileModal');

        if (profileLink) {
            profileLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.showProfileModal();
            });
        }

        if (closeProfileModal) {
            closeProfileModal.addEventListener('click', () => this.hideProfileModal());
        }

        if (profileModal) {
            profileModal.addEventListener('click', (e) => {
                if (e.target === profileModal) {
                    this.hideProfileModal();
                }
            });
        }

        // Logout
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                authHandler.logout();
            });
        }

        // Settings button
        const settingsBtn = document.getElementById('settingsBtn');
        if (settingsBtn) {
            settingsBtn.addEventListener('click', () => {
                // Navigate to settings section
                this.switchSection('settings');
            });
        }

        // Period selectors
        document.querySelectorAll('.period-select').forEach(select => {
            select.addEventListener('change', () => this.updateCharts());
        });
    }

    toggleSidebar() {
        const sidebar = document.getElementById('sidebar');
        if (sidebar) {
            sidebar.classList.toggle('show');
        }
    }

    toggleDropdown(event) {
        event.stopPropagation();
        const dropdown = document.getElementById('dropdownMenu');
        if (dropdown) {
            dropdown.classList.toggle('show');
        }
    }

    toggleNotifications() {
        const panel = document.getElementById('notificationPanel');
        if (panel) {
            panel.classList.toggle('show');
        }
    }

    closeNotifications() {
        const panel = document.getElementById('notificationPanel');
        if (panel) {
            panel.classList.remove('show');
        }
    }

    handleMenuClick(event) {
        event.preventDefault();
        const menuItem = event.currentTarget;
        const section = menuItem.dataset.section;

        // Update active menu item
        document.querySelectorAll('.menu-item').forEach(item => {
            item.classList.remove('active');
        });
        menuItem.classList.add('active');

        // Switch section
        this.switchSection(section);

        // Close sidebar on mobile
        if (window.innerWidth < 992) {
            this.toggleSidebar();
        }
    }

    switchSection(sectionId) {
        // Hide all sections
        document.querySelectorAll('.content-section').forEach(section => {
            section.classList.remove('active');
        });

        // Show selected section
        const targetSection = document.getElementById(`${sectionId}Section`);
        if (targetSection) {
            targetSection.classList.add('active');

            // Load section-specific data
            if (sectionId === 'dashboard') {
                this.loadDashboardData();
            } else if (sectionId === 'profile') {
                this.loadProfileData();
            }
        }
    }

    async loadDashboardData() {
        try {
            // Show loading state
            this.showLoading();

            // Load stats
            const stats = await api.getDashboardStats();

            // Update stats cards
            this.updateStats(stats);

            // Initialize charts
            this.initCharts(stats);

            // Load activities
            this.loadActivities(stats.recentActivities);

        } catch (error) {
            console.error('Failed to load dashboard data:', error);
            Utils.showToast('Failed to load dashboard data', 'error');
        } finally {
            this.hideLoading();
        }
    }

    updateStats(stats) {
        // Update total customers
        const totalCustomers = document.getElementById('totalCustomers');
        if (totalCustomers) {
            totalCustomers.textContent = stats.totalCustomers.toLocaleString();
        }

        // Update total orders
        const totalOrders = document.getElementById('totalOrders');
        if (totalOrders) {
            totalOrders.textContent = stats.totalOrders.toLocaleString();
        }

        // Update total revenue
        const totalRevenue = document.getElementById('totalRevenue');
        if (totalRevenue) {
            totalRevenue.textContent = `$${stats.totalRevenue.toLocaleString()}`;
        }

        // Update active users
        const activeUsers = document.getElementById('activeUsers');
        if (activeUsers) {
            activeUsers.textContent = stats.activeUsers.toLocaleString();
        }
    }

    initCharts(stats) {
        // Initialize revenue chart
        const revenueCtx = document.getElementById('revenueChart');
        if (revenueCtx) {
            if (this.charts.revenue) {
                this.charts.revenue.destroy();
            }

            this.charts.revenue = new Chart(revenueCtx, {
                type: 'line',
                data: {
                    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                    datasets: [{
                        label: 'Revenue',
                        data: stats.revenueTrend,
                        borderColor: '#4361ee',
                        backgroundColor: 'rgba(67, 97, 238, 0.1)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                drawBorder: false
                            },
                            ticks: {
                                callback: function(value) {
                                    return '$' + value.toLocaleString();
                                }
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
        }

        // Initialize customer growth chart
        const customerCtx = document.getElementById('customerChart');
        if (customerCtx) {
            if (this.charts.customers) {
                this.charts.customers.destroy();
            }

            this.charts.customers = new Chart(customerCtx, {
                type: 'bar',
                data: {
                    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                    datasets: [{
                        label: 'Customers',
                        data: stats.customerGrowth,
                        backgroundColor: '#7209b7',
                        borderColor: '#7209b7',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                drawBorder: false
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
        }
    }

    updateCharts() {
        // In a real application, this would fetch new data based on selected period
        // For now, we'll just simulate an update
        if (this.charts.revenue) {
            const newData = Array.from({length: 7}, () =>
                Math.floor(Math.random() * 50000) + 10000
            );
            this.charts.revenue.data.datasets[0].data = newData;
            this.charts.revenue.update();
        }
    }

    loadActivities(activities) {
        const activityList = document.getElementById('activityList');
        if (!activityList) return;

        activityList.innerHTML = '';

        activities.forEach(activity => {
            const activityItem = document.createElement('div');
            activityItem.className = 'activity-item';

            let icon = 'fa-user';
            if (activity.type === 'order') icon = 'fa-shopping-cart';
            if (activity.type === 'system') icon = 'fa-cog';

            activityItem.innerHTML = `
                <div class="activity-icon">
                    <i class="fas ${icon}"></i>
                </div>
                <div class="activity-content">
                    <h4>${activity.title}</h4>
                    <p>${activity.description}</p>
                    <div class="activity-time">${Utils.timeAgo(activity.time)}</div>
                </div>
            `;

            activityList.appendChild(activityItem);
        });
    }

    async loadProfileData() {
        const userInfo = Utils.getUserInfo();
        if (!userInfo) return;

        try {
            const profile = await api.getAdminProfile(userInfo.adminId);

            // Update profile modal content
            const profileView = document.querySelector('.profile-view');
            if (profileView) {
                profileView.innerHTML = this.createProfileHTML(profile);
            }
        } catch (error) {
            console.error('Failed to load profile:', error);
            Utils.showToast('Failed to load profile data', 'error');
        }
    }

    createProfileHTML(profile) {
        return `
            <div class="profile-header">
                <div class="profile-avatar">
                    <div class="avatar-large">
                        <i class="fas fa-user"></i>
                    </div>
                    <div class="profile-name">
                        <h3>${profile.name || profile.username}</h3>
                        <p>${profile.role || 'Administrator'}</p>
                    </div>
                </div>
            </div>

            <div class="profile-details">
                <div class="detail-group">
                    <h4>Contact Information</h4>
                    <div class="detail-item">
                        <i class="fas fa-envelope"></i>
                        <div>
                            <label>Email</label>
                            <p>${profile.email || 'Not provided'}</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-phone"></i>
                        <div>
                            <label>Phone</label>
                            <p>${profile.phone || 'Not provided'}</p>
                        </div>
                    </div>
                </div>

                <div class="detail-group">
                    <h4>Account Information</h4>
                    <div class="detail-item">
                        <i class="fas fa-user-circle"></i>
                        <div>
                            <label>Username</label>
                            <p>${profile.username}</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-shield-alt"></i>
                        <div>
                            <label>Role</label>
                            <p>${profile.role}</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-calendar-alt"></i>
                        <div>
                            <label>Member Since</label>
                            <p>${Utils.formatDate(profile.createdAt || new Date())}</p>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    showProfileModal() {
        const modal = document.getElementById('profileModal');
        if (modal) {
            modal.classList.add('show');
            this.loadProfileData();
        }
    }

    hideProfileModal() {
        const modal = document.getElementById('profileModal');
        if (modal) {
            modal.classList.remove('show');
        }
    }

    showLoading() {
        // Implement loading overlay if needed
    }

    hideLoading() {
        // Hide loading overlay if implemented
    }

    updateCurrentDate() {
        const dateElement = document.getElementById('currentDate');
        if (dateElement) {
            dateElement.textContent = Utils.formatDate(new Date());
        }
    }

    startTokenRefreshTimer() {
        const userInfo = Utils.getUserInfo();
        if (!userInfo || !userInfo.exp) return;

        const expiresAt = userInfo.exp * 1000;
        const now = Date.now();
        const timeUntilExpiry = expiresAt - now;

        // Refresh token 5 minutes before expiry
        const refreshTime = Math.max(timeUntilExpiry - (5 * 60 * 1000), 10000);

        this.tokenRefreshInterval = setTimeout(async () => {
            try {
                const refreshToken = localStorage.getItem('refreshToken');
                if (refreshToken) {
                    const response = await api.refreshToken(refreshToken);

                    if (response.data && response.data.accessToken) {
                        Utils.saveToken(response.data.accessToken);
                        api.setAuthToken(response.data.accessToken);

                        if (response.data.refreshToken) {
                            localStorage.setItem('refreshToken', response.data.refreshToken);
                        }

                        Utils.showToast('Session refreshed', 'success');
                        this.startTokenRefreshTimer(); // Restart timer
                    }
                }
            } catch (error) {
                console.error('Token refresh failed:', error);
                Utils.showToast('Session expired. Please login again.', 'warning');
                setTimeout(() => authHandler.logout(), 2000);
            }
        }, refreshTime);

        // Update token timer display
        this.updateTokenTimer();
    }

    updateTokenTimer() {
        const timerElement = document.getElementById('tokenTimer');
        if (!timerElement) return;

        const userInfo = Utils.getUserInfo();
        if (!userInfo || !userInfo.exp) return;

        const updateDisplay = () => {
            const now = Math.floor(Date.now() / 1000);
            const remaining = userInfo.exp - now;

            if (remaining <= 0) {
                timerElement.textContent = 'Expired';
                return;
            }

            const minutes = Math.floor(remaining / 60);
            const seconds = remaining % 60;
            timerElement.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;
        };

        updateDisplay();
        setInterval(updateDisplay, 1000);
    }

    destroy() {
        // Clean up intervals
        if (this.tokenRefreshInterval) {
            clearTimeout(this.tokenRefreshInterval);
        }

        // Destroy charts
        Object.values(this.charts).forEach(chart => {
            if (chart && chart.destroy) {
                chart.destroy();
            }
        });
    }
}

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.includes('dashboard.html')) {
        window.dashboard = new Dashboard();
    }
});
/**
 * Check if user is authenticated
 * @returns {boolean} - True if authenticated
 */
static isAuthenticated() {
    const token = this.getToken();
    if (!token) return false;

    try {
        // Check if token is expired
        const payload = JSON.parse(atob(token.split('.')[1]));
        const isExpired = payload.exp * 1000 < Date.now();

        if (isExpired) {
            this.removeToken();
            return false;
        }

        return true;
    } catch (e) {
        this.removeToken();
        return false;
    }
}

/**
 * Redirect to login if not authenticated
 */
static requireAuth() {
    if (!this.isAuthenticated()) {
        // Check if we're already on login page
        if (!window.location.pathname.includes('/login')) {
            window.location.href = '/login';
        }
        return false;
    }
    return true;
}
// Add to your Dashboard class
setupMenuHandlers() {
    // Handle sidebar menu clicks
    document.querySelectorAll('.menu-item').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            this.loadSection(section);

            // Update active menu item
            document.querySelectorAll('.menu-item').forEach(i => {
                i.classList.remove('active');
            });
            item.classList.add('active');

            // Update page title
            this.updatePageTitle(item.textContent.trim());
        });
    });

    // Handle quick action clicks
    document.querySelectorAll('.action-item').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            this.loadSection(section);
        });
    });

    // Handle dropdown menu clicks
    document.querySelectorAll('.dropdown-item').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            if (section === 'profile') {
                this.loadProfile();
            } else if (section) {
                this.loadSection(section);
            }
        });
    });
}

loadSection(section) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(sec => {
        sec.classList.remove('active');
    });

    // Show requested section
    const sectionElement = document.getElementById(`${section}Section`);
    if (sectionElement) {
        sectionElement.classList.add('active');
    } else {
        // Load section dynamically
        this.loadDynamicSection(section);
    }
}

updatePageTitle(title) {
    const pageTitle = document.getElementById('pageTitle');
    if (pageTitle) {
        pageTitle.textContent = title;
    }
}