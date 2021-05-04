import React, {useEffect, useState} from "react";
import CategoryTree from "./CategoryTree";
import Products from "./Products";
import {CategoryApi, CategoryResponse, Configuration} from "../generated/api";

const Content = () => {
    useEffect(() => {
        new CategoryApi(new Configuration({}), "http://localhost:8080").getAllCategories().then(cats => {
            setCategories(cats.data);
        }).catch(err => console.log(err));
    }, [])

    const [selectedCategoryId, setSelectedCategoryId] = useState(-1);
    const [categories, setCategories] = useState(new Array<CategoryResponse>());

    function setSelected(id: number) {
        console.log(id);
        setSelectedCategoryId(id);
    }

    return (
        <div className="content center">
            <div className="">
                <CategoryTree clickCategory={setSelected} categories={categories} selectedCategoryId={selectedCategoryId}/></div>
            <div className="white-border">
                <Products selectedCategoryId={selectedCategoryId}/>
            </div>
        </div>
    );
};


export default Content;

