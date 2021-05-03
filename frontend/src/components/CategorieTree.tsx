import React, {useEffect, useState} from 'react';
import TreeMenu, {TreeNodeInArray} from 'react-simple-tree-menu';
import {CategoryApi, CategoryResponse, Configuration} from "../generated/api";

interface MyTreeNode extends TreeNodeInArray {
    label: string;
}

function convertCategory(cat: CategoryResponse): MyTreeNode {
    let categoryResponses = toArray(cat.children);
    let myTreeNodes: MyTreeNode[] = categoryResponses.map(child => convertCategory(child));

    return {
        key: cat.id + "",
        label: cat.name || "Fehler",
        nodes: myTreeNodes
    }
}

function toArray(cat: Set<CategoryResponse> | undefined): Array<CategoryResponse> {
    return Array.from((cat || []).values());
}

const CategorieTree = () => {

    const [treeNodes, setTreeNodes] = useState(new Array<MyTreeNode>());

    useEffect(() => {
        new CategoryApi(new Configuration({}), "http://localhost:8080").getAllCategories().then(cats => {
            setTreeNodes(cats.data.map(cat => convertCategory(cat)))
        }).catch(err => console.log(err));
    }, [])


    return (
        <div>
            <p>Categories</p>
            <TreeMenu data={treeNodes}
                      onClickItem={({key, label, ...props}) => {
                          console.log("Id " + key + " label " + label);
                      }}
            />
        </div>
    );
};

export default CategorieTree;

