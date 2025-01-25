import {BrowserRouter,Route, Routes} from "react-router-dom";
import NotFound from './pages/NotFound';
import Problema1 from "./pages/Problema1";
import Problema2 from "./pages/Problema2";
import Problema3 from "./pages/Problema3";
import Problema from "./pages/Problema";
import Acasa from './pages/Acasa';
import LogIn from './pages/LogIn';
import Register from './pages/Register';
function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<Acasa />} ></Route>
            <Route path="/log-in" element={<LogIn />} ></Route>
            <Route path="/register" element={<Register />} ></Route>
            <Route path="/problema1" element={<Problema1 />} ></Route>
            <Route path="/problema2" element={<Problema2 />} ></Route>
            <Route path="/problema3" element={<Problema3 />} ></Route>
            <Route path="/problema" element={<Problema />} ></Route>
            <Route path="*" element={<NotFound />} ></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}


export default App;
