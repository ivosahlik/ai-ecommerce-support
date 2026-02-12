import React from "react";
import { IoReloadSharp } from "react-icons/io5";
import { FcCancel } from "react-icons/fc";
import { LiaTimesSolid } from "react-icons/lia";
import { FaCheck } from "react-icons/fa6";

const ResolutionInput = ({
  resolutionDetails,
  setResolutionDetails,
  suggestions,
  loadingSuggestions,
  suggestionsError,
  onSubmit,
  onCancel,
  loading,
  onSuggestionClick,
  onClearTextArea,
  onReloadSuggestions,
}) => {
  return (
    <div className='resolution-input-container mt-3 mb-5'>
      <div>
        <p className='text-muted mb-2'>AI Suggestions based on complaint:</p>
        {loadingSuggestions && (
          <div className='text-center'>
            <div className='spinner-border spinner-border-sm' role='status' />
            <span className='ms-2 text-info'>Loading suggestions...</span>
          </div>
        )}
        {suggestionsError && (
          <div className='alert alert-warning py-2'>{suggestionsError}</div>
        )}
        {!loadingSuggestions && suggestions.length > 0 && (
          <div className='suggestions-list'>
            {suggestions.map((suggestion, index) => (
              <div
                key={index}
                className='suggestion-item bg-light text-dark me-2 mb-2 cursor-pointer'
                onClick={() => onSuggestionClick(suggestion)}>
                <p className='text-info'>{suggestion}</p>
              </div>
            ))}
          </div>
        )}
      </div>

      <textarea
        className='form-control'
        rows={5}
        placeholder='Describe how the issue was resolved...'
        value={resolutionDetails}
        onChange={(e) => setResolutionDetails(e.target.value)}
        disabled={loading}
      />

      <div className='mt-2 d-flex justify-content-start gap-2'>
        <button
          className='btn'
          onClick={onSubmit}
          disabled={loading}
          title='Set Ticket as Resolved'>
          {loading ? <Spinner /> : <FaCheck size={30} />}
        </button>
        <button
          className='btn'
          onClick={onCancel}
          disabled={loading}
          title='Cancel Resolution'>
          <LiaTimesSolid size={30} />
        </button>
        <button
          className='btn '
          onClick={onClearTextArea}
          title='Clear Text Area'>
          <FcCancel size={30} />
        </button>
        <button
          className='btn'
          onClick={onReloadSuggestions}
          title='Reload AI Suggestions'>
          <IoReloadSharp size={30} />
        </button>
      </div>
    </div>
  );
};

export default ResolutionInput;
