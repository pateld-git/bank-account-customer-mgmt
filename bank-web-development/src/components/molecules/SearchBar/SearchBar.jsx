import React from "react";
import InputField from "../../atoms/InputField/InputField";
import Button from "../../atoms/Button/Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import "./SearchBar.css";

/**
 * SearchBar molecule component.
 * Combines an InputField and a Button for a unified search interface.
 */
const SearchBar = () => {
  const handleSearch = () => {
    console.log("Search button clicked");
  };

  return (
    <div className="searchbar-container">
      <InputField
        type="text"
        className="custom-input"
        placeholder="Search..."
      />
      <Button className="custom-button" onClick={handleSearch}>
        <FontAwesomeIcon icon={faSearch} />
      </Button>
    </div>
  );
};

export default SearchBar;
