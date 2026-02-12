import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom';
import { getAllTickets } from '../service/apiService';

const Tickets = ({searchRef}) => {
    const [tickets, setTickets] = useState([]);
    const [filteredTickets, setFilteredTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    useEffect(() => {
      const loadTickets = async () => {
        try {
          const data = await getAllTickets();
          setTickets(data);
          setFilteredTickets(data);
        } catch (err) {
          setError(err.message);
        } finally {
          setLoading(false);
        }
      };
      loadTickets();
    }, []);



    useEffect(() => {
      if (searchRef && searchRef.trim() !== "") {
        const filtered = tickets.filter(
          (ticket) =>
            ticket.referenceNumber &&
            ticket.referenceNumber.toLowerCase() === searchRef.toLowerCase()
        );
        setFilteredTickets(filtered);
      } else {
        setFilteredTickets(tickets);
      }
    }, [searchRef, tickets]);








  return (
    <div>
      {filteredTickets.length === 0 ? (
        <p>No tickets found.</p>
      ) : (
        <table className='table table-hover table-bordered'>
          <thead>
            <tr style={{ borderBottom: "1px solid #ddd" }}>
              <th>ID</th>
              <th>Ecommerce Issue</th>
              <th>Reference Number</th>
              <th>Status</th>
              <th>Created At</th>
              <th>Resolved At</th>
              <th>Conversation ID</th>
              <th>Details</th>
            </tr>
          </thead>

          <tbody>
            {filteredTickets.map((ticket) => (
              <tr key={ticket.id}>
                <td>{ticket.id}</td>
                <td>{ticket.conversation?.conversationTitle}</td>
                <td>{ticket.referenceNumber}</td>
                <td>{ticket.ticketStatus}</td>
                <td>{new Date(ticket.createdAt).toLocaleString()}</td>
                <td>
                  {ticket.resolvedAt
                    ? new Date(ticket.resolvedAt).toLocaleString()
                    : "-"}
                </td>
                <td>{ticket.conversation ? ticket.conversation.id : "-"}</td>
                <td>
                  <Link
                    to={`/tickets/${ticket.id}/ticket`}
                    className='btn btn-outline-primary'>
                    View Details
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Tickets
