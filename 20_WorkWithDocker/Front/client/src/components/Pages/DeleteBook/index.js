import React, {useState} from "react";
import api from "../../../services/api"
import { Link, useNavigate, useParams } from "react-router-dom";
import './styles.css';
import 'bootstrap/dist/css/bootstrap.css';
import "bootstrap-icons/font/bootstrap-icons.css";


export default function RemoveBook(){
    const navigate = useNavigate()
    let { bookId } = useParams();
    async function DeleteBook( e ){
        const accessToken = localStorage.getItem("accessToken")
        try {
            await api.delete(`api/books/v1/${bookId}`,
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
                <form   onSubmit={DeleteBook}
                className="">
                    <div class="col-md-12 card tamanho-card cor-destaque cor-tema shadow" >
                                <div class="card-header span">
                                   <Link to={"/book"}>
                                        <a href="#" className="card-link cor-tema span btn">
                                        <i class="bi bi-arrow-left-circle-fill"></i>
                                        </a>
                                    </Link>
                                    <strong>Delete a book with id: {}</strong>
                                </div>
                                <div class="card-body">
                                    <p>
                                        This operation cannot be undone. Are you sure you want to <strong className="text-danger">Delete</strong> it?
                                    </p>
                                </div>
                                <div className="card-body">
                                    <button type="submit"  className="card-link btn-danger span btn">
                                        <i className="bi bi-trash3-fill"></i>
                                        <span> Delete</span>
                                    </button>
                                </div>
                            </div>

                </form>
            </main>
        </div>
    )
}