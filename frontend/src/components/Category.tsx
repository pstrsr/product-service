import React from 'react';
import {CategoryResponse} from "../generated/api";

const Category = ({category, clickCategory}: CategoryProps) => {
    return (
        <div>
            <li onClick={() => clickCategory(category.id)}>
                {category.name}
            </li>
        </div>
    );
};

interface CategoryProps {
    category: CategoryResponse;
    clickCategory: any;
}

export default Category;

