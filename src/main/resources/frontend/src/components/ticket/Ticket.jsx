import React, { useState, useEffect } from "react";
import TicketDetails from "./TicketDetails";
import TicketHeader from "./TicketHeader";
import CustomerComplaint from "./CustomerComplaint";
import CustomerInformation from "./CustomerInformation";
import ResolutionInput from "./ResolutionInput";
import TicketFooter from "./TicketFooter";
import { useParams } from "react-router-dom";
import {
  getTicketById,
  resolveTicket,
  fetchResolutionSuggestions,
} from "../service/apiService";

const Ticket = () => {
  const [ticket, setTicket] = useState(null);
  const { ticketId } = useParams();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  const [showResolutionInput, setShowResolutionInput] = useState(false);
  const [resolutionDetails, setResolutionDetails] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [loadingSuggestions, setLoadingSuggestions] = useState(false);

  useEffect(() => {
    const fetchTicket = async () => {
      setLoading(true);
      try {
        const response = await getTicketById(ticketId);
        setTicket(response);
        setError(null);
      } catch (err) {
        setError(err.message || "Failed to fetch ticket");
      } finally {
        setLoading(false);
      }
    };
    fetchTicket();
  }, [ticketId]);

  const handleFetchSuggestions = async () => {
    const complaintSummary = ticket?.conversation?.conversationSummary;
    if (showResolutionInput && complaintSummary) {
      setLoadingSuggestions(true);
      setError(null);
      try {
        const data = await fetchResolutionSuggestions(complaintSummary);
        setSuggestions(data);
      } catch (err) {
        setError("Failed to load suggestions");
      } finally {
        setLoadingSuggestions(false);
        setTimeout(() => setError(null), 10000);
      }
    }
  };

  const handleMarkAsResolvedClick = () => {
    setShowResolutionInput(true);
  };

  useEffect(() => {
    handleFetchSuggestions();
  }, [showResolutionInput, ticket]);

  const handleResolveTicket = async () => {
    if (!resolutionDetails.trim()) {
      setError("Please provide some details on how the issue was resolved.");
      return;
    }
    setLoading(true);
    try {
      const response = await resolveTicket(ticketId, resolutionDetails);
      setTicket(response);
      setShowResolutionInput(false);
      setResolutionDetails("");
      setSuccessMessage(
        `Ticket with Id No ${ticketId} has been resolved successfully.`
      );
    } catch (err) {
      setError(err.message || "Failed to resolve ticket.");
    } finally {
      setLoading(false);
      setTimeout(() => {
        setError(null);
        setSuccessMessage("");
      }, 5000);
    }
  };

  const handleCancel = () => {
    setShowResolutionInput(false);
    setResolutionDetails("");
    setError(null);
    setSuggestions([]);
  };

  const handleSuggestionClick = (suggestion) => {
    setResolutionDetails((prev) =>
      prev ? `${prev}\n${suggestion}` : suggestion
    );
  };

  const handleClearTextArea = () => {
    setResolutionDetails("");
    setError(null);
  };

  if (loading) return <div className='loading'>Loading ticket...</div>;

  return (
    <div className='ticket-container'>
      <TicketHeader title={ticket?.conversation?.conversationTitle} />
      {successMessage && (
        <div className='alert alert-success'>{successMessage}</div>
      )}

      <TicketDetails ticket={ticket} />
      <CustomerComplaint
        summary={ticket?.conversation?.conversationSummary}
        ticket={ticket}
      />

      <CustomerInformation customer={ticket?.conversation?.customer} />

      {showResolutionInput && (
        <ResolutionInput
          resolutionDetails={resolutionDetails}
          setResolutionDetails={setResolutionDetails}
          suggestions={suggestions}
          loadingSuggestions={loadingSuggestions}
          suggestionsError={error}
          onSubmit={handleResolveTicket}
          onCancel={handleCancel}
          loading={loading}
          onSuggestionClick={handleSuggestionClick}
          onClearTextArea={handleClearTextArea}
          onReloadSuggestions={handleFetchSuggestions}
        />
      )}

      <TicketFooter
        ticketStatus={ticket?.ticketStatus}
        onMarkAsResolvedClick={handleMarkAsResolvedClick}
        showResolutionInput={showResolutionInput}
      />
    </div>
  );
};

export default Ticket;
