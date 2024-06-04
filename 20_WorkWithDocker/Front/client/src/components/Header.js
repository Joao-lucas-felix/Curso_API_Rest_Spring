import React from "react";
import { Link, useNavigate } from "react-router-dom";
import './styles.css';
import 'bootstrap/dist/css/bootstrap.css';
import "bootstrap-icons/font/bootstrap-icons.css";

export default function Header(){
    const navigate = useNavigate()
    async function logout(){
        try {
            localStorage.clear()
            navigate('/')
            
        } catch (error) {
            alert("Unespected erro while trying logout")
        }
    }
    return(
        <header className="fixed-top cor-destaque rounded-pill shadow">
            <div className="container cor-tema">
                <nav className="navbar">

                    <span className="navbar-brand m-1 p-1 title fs-4">
                        Books Page
                    </span>

                    <span className="navbar-text cor-tema">

                        <Link to={"/add/book"}>
                            <a href="" className="p-2 m-2 btn" >
                                <i class="bi bi-file-earmark-plus-fill cor-tema"></i>
                                <span className="span cor-tema" > Add Book </span>
                            </a>
                        </Link>

                        <button onClick={logout} className="p-2 m-2 btn">
                            <i class="bi bi-power cor-tema"></i>  
                            <span className="span cor-tema">Logout</span> 
                        </button>
                    </span>
                </nav>
            </div>
        </header>
    )
}