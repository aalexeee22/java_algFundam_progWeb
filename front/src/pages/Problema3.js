import FileUpload from "../components/FileUpload";
import Header from "../components/Header";
import '../assets/probleme.css';
import HeaderLogged from "../components/HeaderLogged";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
function Problema3(){
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
                <h1>Problema 3</h1>
                <h2>Cerința</h2>
                <p>Se dau n șiruri de numere întregi ordonate crescător, de dimensiuni d[1], d[2], …, d[n]. Dacă se interclasează șirurile de dimensiuni d[i] și d[j] atunci se efectuează d[i]+d[j] operații și se obține un șir de dimensiuni d[i]+d[j]. Trebuie interclasate toate cele n șiruri. Pentru aceasta sunt necesari exact n - 1 pași. La fiecare pas se iau două șiruri, se interclasează și cele două șiruri se înlocuiesc cu noul șir. Scopul este să se obțină un singur șir ordonat efectuând un număr minim de operații. De exemplu, dacă n=4 și șirurile au dimensiunile 1, 5, 2 și 5, atunci se poate interclasa mai întâi 1 și 5, se fac 6 operații și rămân 3 șiruri de lungimi 6, 2, 5. Se interclasează apoi 2 cu 5 cu un cost 7 și rămân două șiruri: 6 și 7. Se interclasează aceasta două cu un cost de 13 și a rămas un singur șir. În total s-au efectuat 6 + 7 + 13 = 26 operații, dar acesta nu este numărul minim posibil.</p>
                <p>Să se determine numărul minim de operații necesare pentru a interclasa cele n șiruri.</p>
                <h2>Date de intrare</h2>
                <p>Fișierul de intrare interclasari.txt conține pe prima linie numărul n, iar pe a doua linie dimensiunile celor n șiruri.</p>
                <h2>Date de ieșire</h2>
                <p>Fișierul de ieșire interclasari.txt va conține pe prima linie numărul minim de operații.</p>
                <h2>Restricții și precizări</h2>
                <ul>
                    <li>1 ≤ n ≤ 100.000</li>
                    <li>cele n numere citite vor fi nenule și mai mici decât 1.000</li>
                </ul>
                <h2>Exemplu:</h2>
                <p>Date de intrare:</p>
                <div className="example">
                    <p>4</p>
                    <p>1 5 2 5</p>
                </div>
                <p>Date de ieșire:</p>
                <div className="example">
                    <p>24</p>
                </div>
                <FileUpload
                    uploadUrl="http://localhost:8081/upload-problema3"
                    outputFileName="interclasari.txt"
                />

            </div>
        </>
    );
}

export default Problema3;