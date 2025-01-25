import React, {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import "../assets/login.css"; // Adjust the path as needed

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async () => {
        setIsLoading(true); // Start loading
        setMessage(""); // Clear previous messages

        try {
            const response = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                const token = await response.text(); // Assuming the server returns a token as plain text
                localStorage.setItem("authToken", token); // Save the token in localStorage
                window.dispatchEvent(new Event("storage")); // Trigger storage event for UI updates
                setMessage("Login successful!");
                navigate("/problema"); // Redirect to the protected route
            } else {
                const errorMessage = await response.text();
                setMessage(errorMessage || "Invalid username or password.");
            }
        } catch (error) {
            console.error("Error:", error);
            setMessage("Error logging in. Please try again later.");
        } finally {
            setIsLoading(false); // Stop loading
        }
    };
    const isLoggedIn = !!localStorage.getItem("authToken");
    useEffect(() => {
        if (!!isLoggedIn) {
            navigate("/problema");
        }
    }, [isLoggedIn, navigate]);

    if (!!isLoggedIn) {
        return null; // Optionally render nothing while redirecting
    }
    return (
        <div>
            <Header />
            <div className="login-container">
                <h2>Login</h2>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    disabled={isLoading}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    disabled={isLoading}
                />
                <button onClick={handleLogin} disabled={isLoading}>
                    {isLoading ? "Logging in..." : "Login"}
                </button>
                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
};

export default Login;
