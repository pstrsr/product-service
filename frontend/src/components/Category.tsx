import React from 'react';
import {CategoryResponse} from "../generated/api";
import CategoryTree from "./CategoryTree";

const Category = ({category, clickCategory, selectedCategoryId}: CategoryProps) => {
    function toArray(categories?: Set<CategoryResponse>) {
        return Array.from(categories || []);
    }

    return (
        <>
            <li className={selectedCategoryId === category.id ? "green-text" : ""}
                onClick={() => clickCategory(category.id)}>
                {category.name?.toUpperCase()}
            </li>
            <CategoryTree
                clickCategory={clickCategory}
                categories={toArray(category.children)}
                selectedCategoryId={selectedCategoryId}/>
        </>
    );
};

interface CategoryProps {
    category: CategoryResponse;
    clickCategory: any;
    selectedCategoryId: number;
}

export default Category;

