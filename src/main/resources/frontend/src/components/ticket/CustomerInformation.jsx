import React from "react";

const CustomerInformation = ({ customer }) => {
  return (
    <section className='ticket-section'>
      <p className='section-title'>Ecommerce's Information</p>
      <dl className='details-list'>
        <dt>Full Name :</dt>
        <p>{customer?.fullName || "-"}</p>

        <dt>Email :</dt>
        <p>{customer?.emailAddress || "-"}</p>

        <dt>Phone Number :</dt>
        <p>{customer?.phoneNumber || "-"}</p>
      </dl>
    </section>
  );
};

export default CustomerInformation;
