import React from "react";
import './styles.css';
import 'bootstrap/dist/css/bootstrap.css';
import "bootstrap-icons/font/bootstrap-icons.css";

export default function Footer(){
    return(
        <footer className="fixed-bottom text-muted  cor-destaque shadow rounded-pill">
            <div className="container">
                <div className="row p-2">
                    <div className="col-4 col-md-4 text-center text-md-left"> 
                        <a 
                        href="https://github.com/Joao-lucas-felix" 
                        target="_blank" 
                        className= "span cor-tema  text-decoration-none"
                        rel="noreferrer">
                            &copy; 2024 - site test
                        </a>
                    </div>
                    <div className="col-4 col-md-4 text-center text-md-right">
                        <a 
                        href="https://github.com/Joao-lucas-felix" 
                        target="_blank" 
                        className="span cor-tema text-decoration-none"
                        rel="noreferrer">
                            Authors
                        </a>
                    </div>

                    <div className="col-4 col-md-4 text-center text-md-right">
                        <a 
                        href="#" 
                        target="_blank" 
                        class="cor-tema text-decoration-none span"
                        rel="noreferrer">
                            Administar
                        </a>
                    </div>
                </div>
            </div>
        </footer>
    )
}