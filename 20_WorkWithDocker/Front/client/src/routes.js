import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./components/Pages/Login";
import Book from "./components/Pages/Books";
import AddBook from "./components/Pages/AddBook";
import RemoveBook from "./components/Pages/DeleteBook";
import EditBook from "./components/Pages/UpdateBook";

export default function Rotas(){
    return(
        <BrowserRouter>
            <Routes>
                <Route path="/"  exact element={<Login/>}/>
                <Route path="/book"  element={<Book/>}/>
                <Route path="/book/delete/:bookId"  element={<RemoveBook/>}/>
                <Route path="/book/edit/:bookId"  element={<EditBook/>}/>
                <Route path="/add/book" exact element={<AddBook/>}/>

            </Routes>
        </BrowserRouter>
    );
}
