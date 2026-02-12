import React from "react";
import { Link } from "react-router-dom";
import { FaArrowLeftLong } from "react-icons/fa6";

const TicketFooter = ({
  ticketStatus,
  onMarkAsResolvedClick,
  showResolutionInput,
}) => {
  return (
    <footer className='ticket-footer'>
      <Link className='btn btn-outline-info' to='/tickets'>
        <FaArrowLeftLong /> Back to tickets
      </Link>

      {ticketStatus !== "RESOLVED" && !showResolutionInput && (
        <button className='btn btn-warning' onClick={onMarkAsResolvedClick}>
          Mark as Resolved
        </button>
      )}
    </footer>
  );
};

export default TicketFooter;
