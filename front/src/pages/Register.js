import { useState,  useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Ensure you are using react-router-dom
import Header from "../components/Header";
import "../assets/register.css"; // Adjust the path as needed

const Register = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate(); // Hook for programmatic navigation

    const handleRegister = async () => {
        if (password !== confirmPassword) {
            setMessage("Passwords do not match.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.text();

            if (response.ok) {
                // Successful registration
                setMessage("Registration successful. Redirecting to login...");
                setTimeout(() => {
                    navigate("/log-in"); // Redirect to /log-in after 5 seconds
                }, 3000); // 5000ms = 5 seconds
            } else {
                // Handle failure
                setMessage(data || "Error registering user.");
            }
        } catch (error) {
            console.error("Error:", error);
            setMessage("Error registering user.");
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
        <>
            <Header />
            <div className="register-container">
                <h2>Register</h2>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
                <button onClick={handleRegister}>Register</button>
                {message && <p>{message}</p>}
            </div>
        </>
    );
};

export default Register;
