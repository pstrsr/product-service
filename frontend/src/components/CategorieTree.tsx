import React, {useEffect, useState} from 'react';
import {CategoryApi, CategoryResponse, Configuration} from "../generated/api";
import Category from "./Category";


const CategorieTree = ({clickCategory}: CategoryTreeProps) => {

    const [treeNodes, setTreeNodes] = useState(new Array<CategoryResponse>());

    useEffect(() => {
        new CategoryApi(new Configuration({}), "http://localhost:8080").getAllCategories().then(cats => {
            setTreeNodes(cats.data);
        }).catch(err => console.log(err));
    }, [])


    const list = treeNodes.map((node) =>
        <Category category={node} clickCategory={clickCategory}/>);

    return (
        <div>
            <p>Categories</p>
            <ul>
                {list}
            </ul>
        </div>
    );
};

interface CategoryTreeProps {
    clickCategory: any;
}


export default CategorieTree;

