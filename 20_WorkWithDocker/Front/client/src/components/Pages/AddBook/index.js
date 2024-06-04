import React, {useState} from "react";
import api from "../../../services/api"
import { Link, useNavigate } from "react-router-dom";
import './styles.css';
import 'bootstrap/dist/css/bootstrap.css';
import "bootstrap-icons/font/bootstrap-icons.css";


export default function AddBook(){
    const [id, setId] = useState(null)
    const [title, setTitle] = useState("")
    const [author, setAuthor] = useState("")
    const [price, setPrice] = useState("")
    const [launchDate, setLaunchDate] = useState("")
    const navigate = useNavigate()

    async function AddBook( e ){
        e.preventDefault();
        const data = {
            id,
            author, 
            launchDate,
            price,
            title,
        }
        console.log(data)
        const userName = localStorage.getItem("userName")
        const accessToken = localStorage.getItem("accessToken")

        try {
            await api.post("api/books/v1", data,
                {
                    headers:{
                        Authorization: `Bearer ${accessToken}`
                    }
                }
            )
            navigate("/book")

        } catch (error) {
            alert("Erro while trying to adds a new book!")
        }
    }

    return (
        <div>
            <main className="">
                <form   onSubmit={AddBook}
                className="">
                    <div class="col-md-12 card tamanho-card cor-destaque cor-tema shadow" >
                                <div class="card-body">
                                <div class="card-header span">
                                    <Link to={"/book"}>
                                        <a href="#" className="card-link cor-tema span btn">
                                        <i class="bi bi-arrow-left-circle-fill"></i>
                                        </a>
                                    </Link>
                                    <strong className="m-4">Enter the book information!</strong>

                                </div>
                                    <div className="form-group cor-tema m-2 p-2">
                                        <label className="m-2 texto-btn" for="exampleInputEmail1">Title</label>
                                        <input type="" className="form-control" id="titleForm" aria-describedby="titleForm" placeholder="Title"
                                        value={title}
                                        onChange={e => setTitle(e.target.value)}/>
                            
                                    </div>
                                    <div className="form-group cor-tema m-2 p-2">
                                        <label className="m-2 texto-btn" for="exampleInputEmail1">Author</label>
                                        <input type="" className="form-control" id="AuthorForm" aria-describedby="AuthorForm" placeholder="Author"
                                        value={author}
                                        onChange={e => setAuthor(e.target.value)}/>
                                    </div>

                                    <div className="form-group cor-tema m-2 p-2">
                                        <label className="m-2 texto-btn" for="exampleInputEmail1">Price</label>
                                        <input type="" className="form-control" id="PriceForm" aria-describedby="PriceForm" placeholder="Price"
                                        value={price}
                                        onChange={e => setPrice(e.target.value)}/>
                                    </div>

                                    <div className="form-group cor-tema m-2 p-2">
                                    <label className="m-2 texto-btn" for="exampleInputEmail1">Release Date</label>
                                        <input type="date" className="form-control" id="titleForm" aria-describedby="titleForm" placeholder="Title"
                                        value={launchDate}
                                        onChange={e => setLaunchDate(e.target.value)}/>
                                    </div>
                                </div>

                                <div className="card-body">
                                    <button type="submit"  className="card-link cor-tema span btn">
                                    <i class="bi bi-plus-circle-fill m-2"></i>
                                        <span>Add</span>
                                    </button>
                                </div>
                            </div>

                </form>
            </main>
        </div>
    )
}