import FileUpload from "../components/FileUpload";
import Header from "../components/Header";
import '../assets/probleme.css';
import HeaderLogged from "../components/HeaderLogged";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
function Problema1(){
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
                <h1>Problema 1</h1>
                <h2>Cerința</h2>
                <p>Se dă o matrice cu n linii și m coloane. Pentru k poziții date, se cere să se determine drumul de lungime minimă care pleacă de la poziția i1 și j1 și trece prin toate cele k poziții (nu contează în ce ordine), ajungând în final în poziția i2 si j2.</p>
                <h2>Date de intrare</h2>
                <p>Fișierul de intrare lee1.txt conține pe prima linie numerele n și m. Pe următoarele n linii vor fi câte m numere, 0 sau 1, 0 însemnând că se poate trece prin poziția i și j și 1 însemnând că la poziția i și j este un obstacol și nu se poate trece prin poziția respectivă. Pe următoarea linie se vor afla patru numere: i1, j1, i2 si j2 cu semnificația din enunț. Pe următoarea linie se află numărul k, urmat de k perechi de numere i și j după cum reiese și din enunț.</p>
                <h2>Date de ieșire</h2>
                <p>Fișierul de ieșire lee1.txt va conține pe prima linie numărul C reprezentând numărul minim de poziții din matrice prin care se va trece începând cu i1 și j1, pentru a trece prin toate cele k poziții și terminând în poziția i2 și j2. Pe următoarele linii se vor afișa k + 2 perechi de numere i și j reprezentând ordinea în care sunt parcurse cele k poziții, inclusiv poziția inițială și cea finală. Indicii pozițiilor se separă prin virgulă. Dacă există mai multe trasee de aceeași lungime minimă, atunci se va afișa traseul minim lexicografic (după indicele liniei și cel al coloanei).</p>
                <h2>Restricții și precizări</h2>
                <ul>
                    <li>1 ≤ n, m ≤ 100</li>
                    <li>1 ≤ k ≤ 5</li>
                    <li>1 ≤ i1, i2, ik ≤ n</li>
                    <li>1 ≤ j1, j2, jk ≤ m</li>
                    <li>Se garantează că există soluție pentru toate testele</li>
                </ul>
                <h2>Exemplu:</h2>
                <p>Date de intrare:</p>
                <div className="example">
                    <p>4 5</p>
                    <p>0 0 0 0 0</p>
                    <p>1 0 1 1 1</p>
                    <p>1 0 0 0 1</p>
                    <p>1 1 0 0 0</p>
                    <p>1 1 4 4</p>
                    <p>3</p>
                    <p>1 5</p>
                    <p>3 4</p>
                    <p>3 2</p>
                </div>
                <p>Date de ieșire:</p>
                <div className="example">
                    <p>12</p>
                    <p>1,1</p>
                    <p>1,5</p>
                    <p>3,2</p>
                    <p>3,4</p>
                    <p>4,4</p>
                </div>
                <h2>Explicatie:</h2>
                <p>Se pleacă din poziția 1,1 și se merge în poziția 1,5 apoi în 3,2 apoi în 3,4 și apoi ajunge la destinație în 4,4.</p>
                <FileUpload
                    uploadUrl="http://localhost:8081/upload-problema1"
                    outputFileName="lee1.txt"
                />

            </div>
        </>
    );
}

export default Problema1;