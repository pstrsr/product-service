import React from "react";
import {ProductResponse} from "../generated/api";

const Products = ({product}: ProductProps) => {
    return (
        <div className="product">
            <div className="product-name">
                <div>{product.name}</div>

                <div className="product-price">
                    {product.price?.amount?.toFixed(2)}
                    <div className="spacer"></div>
                    {product.price?.currency}
                </div>
            </div>
            <div className="product-description">
                {product.description}
            </div>
        </div>
    );
};

interface ProductProps {
    product: ProductResponse;
}

export default Products;
