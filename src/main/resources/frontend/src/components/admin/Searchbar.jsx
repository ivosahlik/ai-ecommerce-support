import React, { useState } from "react";

const Searchbar = ({ onSearch }) => {
  const [refNumber, setRefNumber] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!refNumber.trim()) {
      alert("Please enter a reference number.");
      return;
    }
    onSearch(refNumber.trim());
  };

  const handleClear = (e) => {
    e.preventDefault();
    setRefNumber("");
    onSearch("");
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className='input-group'>
        <input
          type='text'
          placeholder='Enter ticket ref number'
          value={refNumber}
          onChange={(e) => setRefNumber(e.target.value)}
          className='form-control ticket-search-input'
        />
        <button type='submit' className='btn btn-info ticket-search-button'>
          Search
        </button>
      </div>
      <div className='mt-2'>
        <button
          type='button'
          onClick={handleClear}
          className='ticket-clear-search-button'>
          Clear Search
        </button>
      </div>
    </form>
  );
};

export default Searchbar;
