import React from "react";

const TicketDetails = ({ ticket }) => {
  return (
    <section className='ticket-section'>
      <p className='section-title'>Ticket Details</p>
      <dl className='details-list'>
        <dt>ID:</dt>
        <p>{ticket?.id || "-"}</p>

        <dt>Status:</dt>
        <p className={`status-${ticket?.ticketStatus?.toLowerCase() || ""}`}>
          {ticket?.ticketStatus || "-"}
        </p>

        <dt>Ref Number:</dt>
        <h4 className='text-info'>{ticket?.referenceNumber || "-"}</h4>

        <dt>Complaint ID:</dt>
        <p>{ticket?.conversation?.id || "-"}</p>

        <dt>Created At:</dt>
        <h6>
          {ticket?.createdAt
            ? new Date(ticket.createdAt).toLocaleString()
            : "-"}
        </h6>

        <dt>Resolved At:</dt>
        <h6>
          {ticket?.resolvedAt
            ? new Date(ticket.resolvedAt).toLocaleString()
            : "-"}
        </h6>
      </dl>
    </section>
  );
};

export default TicketDetails;
