import React, {useEffect, useState} from "react";
import {Configuration, ProductApi, ProductResponse} from "../generated/api";
import Product from "./Product";

const Products = ({selectedCategoryId}: ProductsProps) => {
    useEffect(() => {
        if (selectedCategoryId > 0) {
            new ProductApi(new Configuration({}), "http://localhost:8080")
                .getProductsByCategory({categoryId: selectedCategoryId})
                .then(products => {
                    setProducts(toArray(products.data));
                }).catch(err => console.log(err));
        }
    }, [selectedCategoryId])

    function toArray(products?: Set<ProductResponse>) {
        return Array.from(products || []);
    }

    const [products, setProducts] = useState(new Array<ProductResponse>());

    const productComponents = products.map((product) =>
        <Product product={product}/>);

    return (
        <div className="product-list">
            {productComponents}
        </div>
    );
};

interface ProductsProps {
    selectedCategoryId: number;
}

export default Products;
