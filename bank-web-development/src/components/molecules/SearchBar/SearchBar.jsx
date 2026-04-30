import React, { useState } from "react";
import InputField from "../../atoms/InputField/InputField";
import Button from "../../atoms/Button/Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import "./SearchBar.css";

const SearchBar = ({
  placeholder = "Search...",
  inputType = "text",
  onSearch,
  min,
  required = false,
  preventRefresh = true,
}) => {
  const [query, setQuery] = useState("");

  const handleSearch = (e) => {
    e.preventDefault();
    if (preventRefresh) {
      preventRefresh = true;
    }
    if (query.trim() !== "") {
      onSearch(query);
    }
  };

  return (
    <form className="searchbar-container" onSubmit={handleSearch}>
      <InputField
        type={inputType}
        className="custom-input"
        placeholder={placeholder}
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        min={min}
        required={required}
      />
      <Button type="submit" className="custom-button">
        <FontAwesomeIcon icon={faSearch} />
      </Button>
    </form>
  );
};

export default SearchBar;
