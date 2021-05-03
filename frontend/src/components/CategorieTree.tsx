import React, {useEffect} from 'react';

import {BaseAPI} from "../generated/api/base";
import {CategoryApi, ProductApi} from "../generated/api";

const CategorieTree = () => {
    return (
        <div>

        </div>
    );
};

const products = props => {
    useEffect(() => {
        new CategoryApi();
    })
}

export default CategorieTree;
