import React,{ useState ,useEffect }from "react";
import Header from "../../Header";
import './styles.css';
import 'bootstrap/dist/css/bootstrap.css';
import "bootstrap-icons/font/bootstrap-icons.css";
import Footer from "../../Footer";
import api from "../../../services/api"
import { useNavigate } from "react-router-dom";



export default function Book(){
    const [books, setBooks] = useState([])
    const userName = localStorage.getItem("userName")
    const accessToken = localStorage.getItem("accessToken")
    const navigate = useNavigate(); 
    const [page,setPage] = useState(0)

    useEffect(
        () => {
            api.get("/api/books/v1",                 
            {
                headers:{
                    Authorization: `Bearer ${accessToken}`
                },
                params:{
                    page: page, 
                    size: 6,
                    direction: 'asc'
                }
            }).then(
                response => setBooks(response.data._embedded.bookDtoList)
            )

        }, [page]
        
    )

    async function NextPage(){
        setPage(page + 1)
    }

    async function PreviousPage(){
        if( page > 0 )
        setPage(page - 1)
    }

    async function editBook( id ){
        try {
            navigate(`/book/edit/${id}`)
        } catch (error) {
            alert("Error while trying edits a book.")
        }
    }
    async function deleteBook( id ){
        try {
            navigate(`/book/delete/${id}`)
        } catch (error) {
            alert("Error while trying deletes a book.")
        }
    }


    return (
        <div>
            <Header/>
            <main className="container m-5 p-5">

                <div className="container align-itens-end mt-5">
                    <nav className="">
                        <ul className="pagination">
                                <li className="page-item m-2">
                                    <button onClick={PreviousPage} className="btn cor-destaque span cor-tema" href="#">Previous</button>
                                </li>
                                <li className="page-item m-2">
                                    <button onClick={NextPage} className="btn cor-destaque span cor-tema " href="#">Next</button>
                                </li>
                        </ul>
                    </nav>
                </div>
                <div className="container m-5">
                    <div className="row">
                        {
                        books.map(
                            books =>
                        <div class="col-sm-12 card tamanho-card cor-destaque cor-tema shadow m-4" >
                                <div class="card-body">
                                <div class="card-header span">
                                    <strong>{books.title}</strong>
                                </div>
                                    <strong className="card-text span">Author: </strong>
                                    <p className="card-text m-2 fs-5">{books.author}</p>
                                    <strong className="card-text span">Price: </strong>
                                    <p className="card-text m-2 fs-5"> {Intl.NumberFormat("pt-BR" ,{ style: 'currency', currency:'BRL'}).format(books.price)} </p>
                                    <strong className="card-text span">Realese Date: </strong>
                                    <p className="card-text m-2 fs-5"> {Intl.DateTimeFormat('pt-BR').format(new Date(books.launchDate))} </p>
                                </div>
                                <div className="card-body">
                                    <button onClick={() => editBook(books.id)}
                                            className="card-link cor-tema span btn">
                                        <i class="bi bi-pen-fill"></i>
                                    </button>
                                    <button onClick={() => deleteBook(books.id)} className="card-link cor-tema span btn">
                                        <i className="bi bi-trash3-fill"></i>
                                    </button>
                                </div>
                        </div> 
                    )
                    
                    }
                    </div>
                </div>

            </main>
            
            
            <Footer/>
        </div>
    )
}