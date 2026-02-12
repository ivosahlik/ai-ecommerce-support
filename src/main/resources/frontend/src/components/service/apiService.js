import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL
});


export const sendMessage = async (sessionId, message) => {
  try {
    const response = await api.post("/chat", {
      sessionId,
      message,
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};


// Fetch all tickets
export const getAllTickets = async () => {
  try {
    const response = await api.get("/tickets");  
    return response.data;
  } catch (error) {
    throw error;
  }
};


// Resolve a ticket by ID
export const resolveTicket = async (ticketId, resolutionDetails) => {
  try {
    const response = await api.put(`/tickets/${ticketId}/resolve`, {
      resolutionDetails,
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

//Get a ticket by ID
export const getTicketById = async (ticketId) => {
  try {
    const response = await api.get(`/tickets/${ticketId}/ticket`);   
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const fetchResolutionSuggestions = async (complaintSummary) => {
  const response = await api.post("/ai/resolution-suggestions", {
    complaintSummary,
  });
  return response.data;
};
