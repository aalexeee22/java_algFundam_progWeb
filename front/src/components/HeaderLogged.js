import { useNavigate } from 'react-router-dom';
function Header() {
    const navigate = useNavigate();
    return(
        <div>
            <button onClick={() => navigate('/problema1')}>Problema1</button>
            <button onClick={() => navigate('/problema2')}>Problema2</button>
            <button onClick={() => navigate('/problema3')}>Problema3</button>
        </div>
    );
}

export default Header;
