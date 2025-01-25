const authService = {
    isAuthenticated: () => {
        const token = localStorage.getItem('authToken');
        return !!token; // Returns true if token exists, false otherwise
    },
    getToken: () => {
        return localStorage.getItem('authToken'); // Returns the token
    },
    logout: () => {
        localStorage.removeItem('authToken'); // Removes the token
    },
};

export default authService;
