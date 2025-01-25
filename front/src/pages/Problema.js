import {useEffect} from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import "../assets/probleme.css";
import HeaderLogged from "../components/HeaderLogged";
/*
import Problema1 from "./Problema1";
import Problema2 from "./Problema2";
import Problema3 from "./Problema3";
*/
function Problema() {
    const isLoggedIn = !!localStorage.getItem("authToken");
    const navigate = useNavigate();
    /*
    const [problema1, setProblema1] = useState(false);
    const [problema2, setProblema2] = useState(false);
    const [problema3, setProblema3] = useState(false);
    */
    useEffect(() => {
        if (!isLoggedIn) {
            navigate("/");
        }
    }, [isLoggedIn, navigate]);

    if (!isLoggedIn) {
        return null; // Optionally render nothing while redirecting
    }

    return (
        <>
            <Header />
            <div className="problema">
                <HeaderLogged />
                {/* Add other content for logged-in users here */}
            </div>
        </>
    );
}

export default Problema;
