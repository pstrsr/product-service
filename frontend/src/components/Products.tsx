import React, {useEffect} from "react";
import {Configuration, ProductApi} from "../generated/api";

const Products = ({selectedCategoryId}: ProductsProps) => {
    useEffect(() => {
        new ProductApi(new Configuration({}), "http://localhost:8080").getProductsByCategory().then(cats => {

        }).catch(err => console.log(err));
    }, [])
    return (
        <div>

        </div>
    );
};

interface ProductsProps {
    selectedCategoryId: number;
}


export default Products;
