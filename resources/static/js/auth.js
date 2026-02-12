/**
 * Authentication handling for login, forgot password, etc.
 */

class AuthHandler {
    constructor() {
        this.captcha = null;
        this.init();
    }

    init() {
        // Initialize CAPTCHA if on login or forgot password page
        if (document.getElementById('captchaDisplay') ||
            document.getElementById('forgotCaptchaDisplay')) {
            this.refreshCaptcha();
        }

        // Setup event listeners
        this.setupEventListeners();
    }

    setupEventListeners() {
        // Login form
        const loginForm = document.getElementById('loginForm');
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => this.handleLogin(e));
        }

        // Forgot password form
        const forgotForm = document.getElementById('forgotPasswordForm');
        if (forgotForm) {
            forgotForm.addEventListener('submit', (e) => this.handleForgotPassword(e));
        }

        // Reset password form
        const resetForm = document.getElementById('resetPasswordForm');
        if (resetForm) {
            resetForm.addEventListener('submit', (e) => this.handleResetPassword(e));
        }

        // Change password form
        const changeForm = document.getElementById('changePasswordForm');
        if (changeForm) {
            changeForm.addEventListener('submit', (e) => this.handleChangePassword(e));
        }

        // Password toggle buttons
        document.querySelectorAll('.toggle-password').forEach(button => {
            button.addEventListener('click', (e) => this.togglePasswordVisibility(e));
        });

        // CAPTCHA refresh buttons
        const refreshCaptchaBtn = document.getElementById('refreshCaptcha');
        if (refreshCaptchaBtn) {
            refreshCaptchaBtn.addEventListener('click', () => this.refreshCaptcha());
        }

        const refreshForgotCaptchaBtn = document.getElementById('refreshForgotCaptcha');
        if (refreshForgotCaptchaBtn) {
            refreshForgotCaptchaBtn.addEventListener('click', () => this.refreshForgotCaptcha());
        }

        // Password strength indicator
        const newPasswordInput = document.getElementById('newPassword');
        if (newPasswordInput) {
            newPasswordInput.addEventListener('input', () => this.updatePasswordStrength());
        }

        // Check authentication on dashboard
        if (window.location.pathname.includes('dashboard.html')) {
            this.checkAuthAndLoadProfile();
        }
    }

    refreshCaptcha() {
        this.captcha = Utils.generateCaptcha();
        const display = document.getElementById('captchaDisplay');
        if (display) {
            display.innerHTML = `<img src="${this.captcha.display}" alt="CAPTCHA">`;
        }
    }

    refreshForgotCaptcha() {
        this.captcha = Utils.generateCaptcha();
        const display = document.getElementById('forgotCaptchaDisplay');
        if (display) {
            display.innerHTML = `<img src="${this.captcha.display}" alt="CAPTCHA">`;
        }
    }

    togglePasswordVisibility(event) {
        const button = event.currentTarget;
        const input = button.parentElement.querySelector('input[type="password"], input[type="text"]');
        const icon = button.querySelector('i');

        if (input.type === 'password') {
            input.type = 'text';
            icon.className = 'fas fa-eye-slash';
        } else {
            input.type = 'password';
            icon.className = 'fas fa-eye';
        }
    }

    updatePasswordStrength() {
        const password = document.getElementById('newPassword')?.value;
        if (!password) return;

        const strength = Utils.validatePasswordStrength(password);
        const bar = document.querySelector('.strength-bar');
        const text = document.getElementById('strengthText');

        if (bar) {
            bar.style.width = `${strength.score * 20}%`;
            bar.style.background = strength.color;
        }

        if (text) {
            text.textContent = strength.strength;
            text.style.color = strength.color;
        }
    }

    async handleLogin(event) {
        event.preventDefault();

        const form = event.target;
        const username = form.username.value.trim();
        const password = form.password.value;
        const captchaInput = form.captcha?.value.trim();
        const rememberMe = form.rememberMe?.checked;

        // Validate inputs
        if (!username || !password) {
            Utils.showToast('Please fill in all required fields', 'error');
            return;
        }

        if (this.captcha && captchaInput !== this.captcha.code) {
            Utils.showToast('Invalid CAPTCHA code. Please try again.', 'error');
            this.refreshCaptcha();
            return;
        }

        // Show loading state
        const loginBtn = document.getElementById('loginBtn');
        const loginText = document.getElementById('loginText');
        const loginSpinner = document.getElementById('loginSpinner');

        if (loginBtn && loginText && loginSpinner) {
            loginBtn.disabled = true;
            loginText.textContent = 'Signing in...';
            loginSpinner.classList.remove('hidden');
        }

        try {
            // Call login API
            const response = await api.login({
                username,
                password,
                captcha: captchaInput
            });

            if (response && response.data) {
                // Save token
                Utils.saveToken(response.data.accessToken);
                api.setAuthToken(response.data.accessToken);

                // Save refresh token if available
                if (response.data.refreshToken) {
                    localStorage.setItem('refreshToken', response.data.refreshToken);
                }

                // Save user info
                localStorage.setItem('userInfo', JSON.stringify({
                    username: response.data.username,
                    role: response.data.role,
                    adminId: response.data.adminId
                }));

                Utils.showToast('Login successful!', 'success');

                // Redirect to dashboard
                setTimeout(() => {
                    window.location.href = 'dashboard.html';
                }, 1000);
            } else {
                throw new Error('Invalid response from server');
            }
        } catch (error) {
            console.error('Login failed:', error);
            Utils.showToast('Login failed. Please check your credentials.', 'error');
            this.refreshCaptcha();
        } finally {
            // Reset button state
            if (loginBtn && loginText && loginSpinner) {
                loginBtn.disabled = false;
                loginText.textContent = 'Sign In';
                loginSpinner.classList.add('hidden');
            }
        }
    }

    async handleForgotPassword(event) {
        event.preventDefault();

        const form = event.target;
        const email = form.email.value.trim();
        const phoneNumber = form.phoneNumber?.value.trim();
        const captchaInput = form.captcha?.value.trim();

        // Validate inputs
        if (!email) {
            Utils.showToast('Please enter your email address', 'error');
            return;
        }

        if (!Utils.validateEmail(email)) {
            Utils.showToast('Please enter a valid email address', 'error');
            return;
        }

        if (this.captcha && captchaInput !== this.captcha.code) {
            Utils.showToast('Invalid CAPTCHA code. Please try again.', 'error');
            this.refreshForgotCaptcha();
            return;
        }

        // Show loading state
        const forgotBtn = document.getElementById('forgotPasswordBtn');
        const forgotText = document.getElementById('forgotPasswordText');
        const forgotSpinner = document.getElementById('forgotPasswordSpinner');

        if (forgotBtn && forgotText && forgotSpinner) {
            forgotBtn.disabled = true;
            forgotText.textContent = 'Sending...';
            forgotSpinner.classList.remove('hidden');
        }

        try {
            // Call forgot password API
            await api.forgotPassword({
                email,
                phoneNumber,
                captcha: captchaInput
            });

            Utils.showToast('Reset link sent to your email if account exists', 'success');

            // Clear form
            form.reset();
            this.refreshForgotCaptcha();

        } catch (error) {
            console.error('Forgot password failed:', error);
            Utils.showToast('Failed to send reset link. Please try again.', 'error');
        } finally {
            // Reset button state
            if (forgotBtn && forgotText && forgotSpinner) {
                forgotBtn.disabled = false;
                forgotText.textContent = 'Send Reset Link';
                forgotSpinner.classList.add('hidden');
            }
        }
    }

    async handleResetPassword(event) {
        event.preventDefault();

        const form = event.target;
        const token = form.token.value;
        const newPassword = form.newPassword.value;
        const confirmPassword = form.confirmPassword?.value;

        // Validate inputs
        if (!token) {
            Utils.showToast('Invalid reset token', 'error');
            return;
        }

        if (!newPassword) {
            Utils.showToast('Please enter a new password', 'error');
            return;
        }

        if (newPassword !== confirmPassword) {
            Utils.showToast('Passwords do not match', 'error');
            return;
        }

        const strength = Utils.validatePasswordStrength(newPassword);
        if (strength.score < 3) {
            Utils.showToast('Password is too weak. Please use a stronger password.', 'error');
            return;
        }

        // Show loading state
        const resetBtn = document.getElementById('resetPasswordBtn');
        const resetText = document.getElementById('resetPasswordText');
        const resetSpinner = document.getElementById('resetPasswordSpinner');

        if (resetBtn && resetText && resetSpinner) {
            resetBtn.disabled = true;
            resetText.textContent = 'Resetting...';
            resetSpinner.classList.remove('hidden');
        }

        try {
            // Call reset password API
            await api.resetPassword({
                token,
                newPassword
            });

            Utils.showToast('Password reset successful! You can now login.', 'success');

            // Show success message and redirect
            const successNotice = document.querySelector('.security-notice.success');
            if (successNotice) {
                successNotice.style.display = 'flex';
            }

            setTimeout(() => {
                window.location.href = 'index.html';
            }, 3000);

        } catch (error) {
            console.error('Reset password failed:', error);
            Utils.showToast('Failed to reset password. Token may be invalid or expired.', 'error');
        } finally {
            // Reset button state
            if (resetBtn && resetText && resetSpinner) {
                resetBtn.disabled = false;
                resetText.textContent = 'Reset Password';
                resetSpinner.classList.add('hidden');
            }
        }
    }

    async handleChangePassword(event) {
        event.preventDefault();

        if (!Utils.requireAuth()) return;

        const form = event.target;
        const currentPassword = form.currentPassword.value;
        const newPassword = form.newPassword.value;
        const confirmPassword = form.confirmPassword?.value;

        // Validate inputs
        if (!currentPassword || !newPassword) {
            Utils.showToast('Please fill in all fields', 'error');
            return;
        }

        if (newPassword !== confirmPassword) {
            Utils.showToast('New passwords do not match', 'error');
            return;
        }

        if (currentPassword === newPassword) {
            Utils.showToast('New password must be different from current password', 'error');
            return;
        }

        const strength = Utils.validatePasswordStrength(newPassword);
        if (strength.score < 3) {
            Utils.showToast('Password is too weak. Please use a stronger password.', 'error');
            return;
        }

        // Show loading state
        const changeBtn = document.getElementById('changePasswordBtn');
        const changeText = document.getElementById('changePasswordText');
        const changeSpinner = document.getElementById('changePasswordSpinner');

        if (changeBtn && changeText && changeSpinner) {
            changeBtn.disabled = true;
            changeText.textContent = 'Changing...';
            changeSpinner.classList.remove('hidden');
        }

        try {
            // Call change password API
            await api.changePassword({
                currentPassword,
                newPassword
            });

            Utils.showToast('Password changed successfully!', 'success');

            // Clear form
            form.reset();

            // Redirect to dashboard after delay
            setTimeout(() => {
                window.location.href = '/dashboard';
            }, 2000);

        } catch (error) {
            console.error('Change password failed:', error);
            Utils.showToast('Failed to change password. Please check your current password.', 'error');
        } finally {
            // Reset button state
            if (changeBtn && changeText && changeSpinner) {
                changeBtn.disabled = false;
                changeText.textContent = 'Change Password';
                changeSpinner.classList.add('hidden');
            }
        }
    }

    async checkAuthAndLoadProfile() {
        if (!Utils.requireAuth()) return;

        const userInfo = Utils.getUserInfo();
        if (!userInfo) {
            window.location.href = 'index.html';
            return;
        }

        // Update UI with user info
        this.updateUserProfile(userInfo);

        // Validate session
        try {
            await api.validateSession(userInfo.adminId);
        } catch (error) {
            console.error('Session validation failed:', error);
            Utils.showToast('Your session has expired. Please login again.', 'warning');
            setTimeout(() => {
                this.logout();
            }, 2000);
        }
    }

    updateUserProfile(userInfo) {
        // Update user name
        const userNameElement = document.getElementById('userName');
        if (userNameElement) {
            userNameElement.textContent = userInfo.username || 'User';
        }

        // Update user role
        const userRoleElement = document.getElementById('userRole');
        if (userRoleElement) {
            userRoleElement.textContent = userInfo.role || 'Admin';
        }

        // Update avatar with initials
        const userAvatar = document.getElementById('userAvatar');
        if (userAvatar && userInfo.username) {
            const initials = userInfo.username.charAt(0).toUpperCase();
            userAvatar.innerHTML = `<span>${initials}</span>`;
        }
    }

    async logout() {
        try {
            const token = Utils.getToken();
            if (token) {
                await api.logout(token);
            }
        } catch (error) {
            console.error('Logout API error:', error);
        } finally {
            // Clear local storage
            Utils.removeToken();
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('userInfo');

            // Redirect to login
            window.location.href = 'index.html';
        }
    }
}

// Initialize auth handler when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.authHandler = new AuthHandler();
});
// In handleLogin method, update to match your API response structure
async handleLogin(event) {
    event.preventDefault();

    const form = event.target;
    const username = form.username.value.trim();
    const password = form.password.value;

    // Validate inputs
    if (!username || !password) {
        Utils.showToast('Please enter username and password', 'error');
        return;
    }

    // Get selected sales organization
    const salesOrg = document.querySelector('input[name="salesOrg"]:checked');
    if (!salesOrg) {
        Utils.showToast('Please select a sales organization', 'error');
        return;
    }

    // Show loading state
    const loginBtn = document.getElementById('loginButton');
    const loginText = document.getElementById('loginText');
    const loginSpinner = document.getElementById('loginSpinner');

    if (loginBtn && loginText && loginSpinner) {
        loginBtn.disabled = true;
        loginText.textContent = 'Signing in...';
        loginSpinner.classList.remove('hidden');
    }

    try {
        console.log('Logging in with:', { username, salesOrg: salesOrg.value });

        // Make API call to your login endpoint
        const response = await fetch('http://localhost:8080/api/admin/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        console.log('Response status:', response.status);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Login failed');
        }

        const responseData = await response.json();
        console.log('Login response:', responseData);

        // Handle different response formats
        let loginData;
        if (responseData.data) {
            loginData = responseData.data; // Your API format
        } else if (responseData.accessToken) {
            loginData = responseData; // Direct token format
        } else {
            throw new Error('Invalid response format');
        }

        // Save token
        if (loginData.accessToken) {
            Utils.saveToken(loginData.accessToken);
            console.log('Token saved');
        }

        // Save user info
        localStorage.setItem('userInfo', JSON.stringify({
            username: loginData.username || username,
            role: loginData.role || 'ADMIN',
            adminId: loginData.adminId || '1',
            email: loginData.email || '',
            salesOrg: salesOrg.value
        }));

        // Save refresh token if available
        if (loginData.refreshToken) {
            localStorage.setItem('refreshToken', loginData.refreshToken);
        }

        Utils.showToast('Login successful! Redirecting to dashboard...', 'success');

        // Redirect to dashboard after delay
        setTimeout(() => {
            window.location.href = '/dashboard';
        }, 1500);

    } catch (error) {
        console.error('Login error:', error);
        Utils.showToast(error.message || 'Login failed. Please check credentials.', 'error');
    } finally {
        // Reset button state
        if (loginBtn && loginText && loginSpinner) {
            loginBtn.disabled = false;
            loginText.textContent = 'Sign In';
            loginSpinner.classList.add('hidden');
        }
    }
}