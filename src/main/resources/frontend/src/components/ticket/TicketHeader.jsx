import React from "react";

const TicketHeader = ({ title }) => {
  return (
    <header className='ticket-header'>
      <h2 className='ticket-center'>{title || "No Title"}</h2>
    </header>
  );
};

export default TicketHeader;
