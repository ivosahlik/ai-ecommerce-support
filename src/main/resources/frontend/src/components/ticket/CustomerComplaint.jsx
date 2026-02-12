import React from "react";

const CustomerComplaint = ({ summary, ticket }) => {
  return (
    <section className='ticket-section'>
      <p className='section-title'>Ecommerce's Complaint</p>
      <p>{summary || "No conversation summary available"}</p>

      {ticket?.ticketStatus?.toLowerCase() === "resolved" && (
        <>
          <p className='section-title'>Resolution Details</p>
          <p>
            {ticket?.resolutionDetails || "No resolution details available."}
          </p>
        </>
      )}
    </section>
  );
};

export default CustomerComplaint;
