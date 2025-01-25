import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/header.css';
import { ReactComponent as LogoIcon } from '../assets/icon.svg';

function Header() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('authToken'));

    useEffect(() => {
        const updateLoginState = () => {
            setIsLoggedIn(!!localStorage.getItem('authToken'));
        };

        // Check login state on mount
        updateLoginState();

        // Listen for changes to localStorage
        window.addEventListener('storage', updateLoginState);

        return () => {
            window.removeEventListener('storage', updateLoginState);
        };
    }, []);

    const handleLogout = async () => {
        const token = localStorage.getItem('authToken');
        if (!token || token.trim() === '') {
            console.error('Invalid token.');
            alert('Invalid session. Please log in again.');
            setIsLoggedIn(false);
            localStorage.removeItem('authToken');
            return;
        }

        // Add a timeout to the fetch request
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 5000); // 5 seconds timeout

        try {
            const response = await fetch('http://localhost:8080/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ token }),
                signal: controller.signal,
            });

            clearTimeout(timeoutId); // Clear timeout if response is received

            if (response.ok) {
                console.log("Logout successful.");
            } else if (response.status === 401) {
                console.error('Unauthorized: Invalid token.');
                alert('Logout failed: Unauthorized.');
            } else if (response.status === 400) {
                console.error('Bad Request: Missing or invalid token.');
                alert('Logout failed: Bad Request.');
            } else {
                const errorMessage = await response.text();
                console.error('Backend error message:', errorMessage);
                alert(`Logout failed: ${errorMessage}`);
            }
        } catch (error) {
            if (error.name === 'AbortError') {
                console.error('Request timeout.');
                alert('Logout request timed out. Please try again.');
            } else {
                console.error('Error during logout:', error);
                alert('Error during logout. Please try again later.');
            }
        } finally {
            // Ensure token removal regardless of logout success
            localStorage.removeItem('authToken');
            console.log('authToken removed from localStorage.');
            setIsLoggedIn(false);
            window.dispatchEvent(new Event('storage')); // Notify other components
            navigate('/'); // Redirect to home
        }
    };

    return (
        <div>
            <header className="header">
                <div className="left" onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>
                    <div className="logo">
                        <LogoIcon />
                    </div>
                    <h2>Learn Java</h2>
                </div>
                <div className="buttons">
                    {isLoggedIn ? (
                        <button className="logout" onClick={handleLogout}>Logout</button>
                    ) : (
                        <>
                            <button className="login" onClick={() => navigate('/log-in')}>Login</button>
                            <button className="register" onClick={() => navigate('/register')}>Register</button>
                        </>
                    )}
                </div>
            </header>
        </div>
    );
}

export default Header;
