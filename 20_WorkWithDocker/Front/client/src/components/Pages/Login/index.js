import React, {useState} from "react";
import api from "../../../services/api";
import { useNavigate } from "react-router-dom";
import './styles.css';
import 'bootstrap/dist/css/bootstrap.css';

export default function Login(){

    const [userName, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    async function login(e){
        e.preventDefault();

        const data = {
            userName,
            password,
        };

        try {
            const response = await api.post('auth/signin', data);

            localStorage.setItem('userName', userName);
            localStorage.setItem('accessToken', response.data.accessToken);
            localStorage.setItem('refreshToken', response.data.refreshToken);

            navigate('/book')
        } catch (err) {
            alert('Login failed! Try again!');
        }
    };

    return (
        <div className="container">
            <div className="row">

                <div className="col-md m-md-4 mb-sm-3">
                    <h1 className="title ">Wellcome</h1>
                    <p className="cor-tema paragrafo">
                        The website to manegement people and books. 
                    </p>
                </div>

                <div className="col col-lg-4 col-md-6   m-5 rounded p-4 cor-destaque">
                    <div className="container-fluid">
                        <form onSubmit={login}>
                            <div className="form-group cor-tema m-2 p-2 ">
                                <label className="m-2 texto-btn" htmlFor="exampleInputEmail1">Username</label>
                                <input type="" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter Username"
                                value={userName}
                                onChange={e => setUsername(e.target.value)} />
                            </div>
                            <div className="form-group cor-tema m-2 p-2">
                                <label  className="m-2 texto-btn" htmlFor="exampleInputPassword1">Password</label>
                                <input type="password" className="form-control" id="exampleInputPassword1" placeholder="Password"
                                value={password}
                                onChange={e => setPassword(e.target.value)} />
                            </div>

                            <button type="submit" className="btn btn-dark p-2 m-2  align-self-end">
                                <span className="texto-btn cor-tema " >Register</span>
                            </button>
                            
                           <button type="submit" className=" btn  p-2 m-2  align-self-end">
                                <div className="container rounded shadow cor-tema-bg p-2 m-2"><span className="texto-btn ">Login</span></div>
                           </button>
                        </form>
            
                    </div>
                </div>
            </div>
        </div>
    )
}