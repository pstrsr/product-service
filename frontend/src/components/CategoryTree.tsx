import React from 'react';
import {CategoryResponse} from "../generated/api";
import Category from "./Category";


const CategoryTree = ({clickCategory, categories, selectedCategoryId}: CategoryTreeProps) => {
    const categorieTree = categories.map((node) =>
        <Category selectedCategoryId={selectedCategoryId} category={node}
                  clickCategory={clickCategory}/>);

    return (
        <>
            <ul>
                {categorieTree}
            </ul>
        </>
    );
};

interface CategoryTreeProps {
    clickCategory: any;
    categories: CategoryResponse[];
    selectedCategoryId: number;
}

export default CategoryTree;

