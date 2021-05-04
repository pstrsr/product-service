import React, {useState} from "react";
import CategorieTree from "./CategorieTree";
import Products from "./Products";

const Content = () => {
    const [selectedCategoryId, setSelectedCategoryId] = useState({});

    function setSelected(id: number) {
        console.log(id);
        setSelectedCategoryId(id);
    }

    return (
        <div>
            <CategorieTree clickCategory={setSelected}/>
            <Products selectedCategoryId={selectedCategoryId}/>
        </div>
    );
};


export default Content;

