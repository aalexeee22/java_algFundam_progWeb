import FileUpload from "../components/FileUpload";
import Header from "../components/Header";
import '../assets/probleme.css';
import HeaderLogged from "../components/HeaderLogged";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
function Problema2(){
    const isLoggedIn = !!localStorage.getItem("authToken");
    const navigate = useNavigate();
    useEffect(() => {
        if (!isLoggedIn) {
            navigate("/");
        }
    }, [isLoggedIn, navigate]);

    if (!isLoggedIn) {
        return null; // Optionally render nothing while redirecting
    }
    return(
        <>
            <Header />
            <div className="problema">
                <HeaderLogged/>
                <h1>Problema 2</h1>
                <h2>Cerința</h2>
                <p>Se dau n numere naturale. Să se afișeze al k-lea cel mai mic element din șir.</p>
                <h2>Date de intrare</h2>
                <p>Fișierul de intrare statisticiordine.txt conține pe prima linie numerele n si k, iar pe a doua linie n numere naturale separate prin spații.</p>
                <h2>Date de ieșire</h2>
                <p>Fișierul de ieșire statisticiordine.txt va conține pe prima linie numărul căutat.</p>
                <h2>Restricții și precizări</h2>
                <ul>
                    <li>1 ≤ k ≤ n ≤ 4.000</li>
                    <li>numerele de pe a doua linie a fișierului de intrare vor fi mai mici decât 4.000.000</li>
                </ul>
                <h2>Exemplu:</h2>
                <p>Date de intrare:</p>
                <div className="example">
                    <p>4 5</p>
                    <p>6 4</p>
                    <p>1 58 4 3 24 50</p>
                </div>
                <p>Date de ieșire:</p>
                <div className="example">
                    <p>24</p>
                </div>
                <h2>Explicație</h2>
                <p>24 este al patrulea cel mai mic element din sir.</p>
                <FileUpload
                    uploadUrl="http://localhost:8081/upload-problema2"
                    outputFileName="statisticiordine.txt"
                />

            </div>
        </>
    );
}

export default Problema2;